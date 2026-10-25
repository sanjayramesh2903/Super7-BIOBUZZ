package org.firstinspires.ftc.teamcode.Pradeep;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="Field Centric Drive", group="Main")
public class FieldCentricDriveTeleOp extends LinearOpMode {

    // Constants
    private final double INTAKE_POWER = 0.7;
    private final double SHOOTER_TARGET_VELOCITY = 1600.0;

    // Hardware
    private DcMotor leftFront, rightFront, leftRear, rightRear;
    private DcMotor sweep;
    private DcMotorEx pollenShooter, nectarShooter;
    private IMU imu;

    // Systems
    private FlyWheelPIDF pollenPID, nectarPID;
    private ElapsedTime runtime;

    // Heading tracking
    private double headingOffset = 0.0;

    @Override
    public void runOpMode() {
        // Initialize Drivetrain Motors
        leftFront  = hardwareMap.get(DcMotor.class, "fLeft");
        rightFront = hardwareMap.get(DcMotor.class, "fRight");
        leftRear   = hardwareMap.get(DcMotor.class, "bLeft");
        rightRear  = hardwareMap.get(DcMotor.class, "bRight");

        // Motor directions: Reverse left side motors so positive power moves robot forward
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightRear.setDirection(DcMotor.Direction.FORWARD);

        // Set BRAKE mode so motors stop sharply instead of coasting!
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize Sweep Intake
        sweep = hardwareMap.get(DcMotor.class, "sweep");

        // Initialize Shooters
        pollenShooter = hardwareMap.get(DcMotorEx.class, "pollen");
        pollenShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pollenShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        nectarShooter = hardwareMap.get(DcMotorEx.class, "nectar");
        nectarShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        nectarShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Initialize Control Hub IMU ("imu")
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
            new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
            )
        );
        imu.initialize(parameters);
        imu.resetYaw();

        pollenPID = new FlyWheelPIDF(0.0025, 0.0001, 0.0002, 0.00038);
        nectarPID = new FlyWheelPIDF(0.0025, 0.0001, 0.0002, 0.00038);
        runtime = new ElapsedTime();

        telemetry.addData("Status", "Initialized with Hub IMU. Ready.");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Get current robot heading from Control Hub IMU (in Radians)
            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - headingOffset;

            // Reset heading offset when Options / Start / Back button is pressed
            if (gamepad1.options || gamepad1.start || gamepad1.back) {
                imu.resetYaw();
                headingOffset = 0;
            }

            // Get raw gamepad inputs
            double y = -gamepad1.left_stick_y; // Y stick value is reversed (UP is positive)
            double x = -gamepad1.left_stick_x * 1.1; // Reversed to fix Left/Right strafing
            double rx = -gamepad1.right_stick_x;     // Reversed to fix Left/Right turning

            // Apply deadzone to eliminate joystick centering drift
            if (Math.abs(y) < 0.05) y = 0;
            if (Math.abs(x) < 0.05) x = 0;
            if (Math.abs(rx) < 0.05) rx = 0;

            // Rotate joystick vector by negative robot heading to make it field-centric
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            // Calculate Mecanum power distribution
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower  = (rotY + rotX + rx) / denominator;
            double backLeftPower   = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower  = (rotY + rotX - rx) / denominator;

            // Apply powers to drivetrain motors
            leftFront.setPower(frontLeftPower);
            leftRear.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightRear.setPower(backRightPower);

            // Sweep Intake Control (Run while held)
            if (gamepad1.x || gamepad1.cross) {
                sweep.setPower(INTAKE_POWER);  // Intake on
            } else if (gamepad1.y || gamepad1.triangle) {
                sweep.setPower(-INTAKE_POWER); // Intake reverse
            } else {
                sweep.setPower(0.0);  // Stop
            }

            // Pollen Shooter Control (Left Trigger)
            double currentPollenTarget = 0;
            if (gamepad1.left_trigger > 0.1) {
                currentPollenTarget = SHOOTER_TARGET_VELOCITY;
            }
            pollenPID.setTarget(currentPollenTarget);
            double currentPollenVelocity = Math.abs(pollenShooter.getVelocity());
            pollenShooter.setPower(pollenPID.update(currentPollenVelocity, runtime.seconds()));

            // Nectar Shooter Control (Right Trigger)
            double currentNectarTarget = 0;
            if (gamepad1.right_trigger > 0.1) {
                currentNectarTarget = SHOOTER_TARGET_VELOCITY;
            }
            nectarPID.setTarget(currentNectarTarget);
            double currentNectarVelocity = Math.abs(nectarShooter.getVelocity());
            nectarShooter.setPower(nectarPID.update(currentNectarVelocity, runtime.seconds()));

            // Telemetry Output
            telemetry.addData("Mode", "Field Centric (Hub IMU)");
            telemetry.addData("Heading (Deg)", "%.1f", Math.toDegrees(botHeading));
            telemetry.addData("Rotated Inputs", "Y: %.2f, X: %.2f", rotY, rotX);
            telemetry.addData("Motor Powers", "FL: %.2f, FR: %.2f, BL: %.2f, BR: %.2f", 
                    frontLeftPower, frontRightPower, backLeftPower, backRightPower);
            telemetry.update();
        }
    }
}
