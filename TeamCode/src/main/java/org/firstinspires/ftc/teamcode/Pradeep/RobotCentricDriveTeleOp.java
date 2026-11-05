package org.firstinspires.ftc.teamcode.Pradeep;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Robot Centric Drive", group="Main")
public class RobotCentricDriveTeleOp extends LinearOpMode {

    // Constants
    private final double INTAKE_POWER = 0.45;
    private final double SHOOTER_TARGET_VELOCITY = 1480.0;

    // Hardware
    private DcMotor leftFront, rightFront, leftRear, rightRear;
    private DcMotor sweep;
    private DcMotorEx pollenShooter, nectarShooter;

    // Systems
    private FlyWheelPIDF pollenPID, nectarPID;
    private ElapsedTime runtime;

    @Override
    public void runOpMode() {
        // Initialize Drivetrain Motors
        leftFront  = hardwareMap.get(DcMotor.class, "fLeft");
        rightFront = hardwareMap.get(DcMotor.class, "fRight");
        leftRear   = hardwareMap.get(DcMotor.class, "bLeft");
        rightRear  = hardwareMap.get(DcMotor.class, "bRight");

        // Motor directions: Reverse right side motors so positive power moves robot forward
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftRear.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);

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

        //nectarShooter = hardwareMap.get(DcMotorEx.class, "nectar");
        //nectarShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //nectarShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        pollenPID = new FlyWheelPIDF(0.0025, 0.0001, 0.0002, 0.00038);
        //nectarPID = new FlyWheelPIDF(0.0025, 0.0001, 0.0002, 0.00038);
        runtime = new ElapsedTime();

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized. Robot Centric Drive Ready.");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Get inputs from gamepad1
            double y = -gamepad1.left_stick_y; // Y stick value is reversed (UP is positive)
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Apply deadzone to eliminate joystick centering drift
            if (Math.abs(y) < 0.05) y = 0;
            if (Math.abs(x) < 0.05) x = 0;
            if (Math.abs(rx) < 0.05) rx = 0;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            // Calculate motor powers
            double frontLeftPower  = (y + x + rx) / denominator;
            double backLeftPower   = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower  = (y + x - rx) / denominator;

            // Apply motor powers
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
                sweep.setPower(0.0);  // Stop when neither is pressed
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
            //double currentNectarTarget = 0;
            //if (gamepad1.right_trigger > 0.1) {
            //    currentNectarTarget = SHOOTER_TARGET_VELOCITY;
            //}
            //nectarPID.setTarget(currentNectarTarget);
            //double currentNectarVelocity = Math.abs(nectarShooter.getVelocity());
            //nectarShooter.setPower(nectarPID.update(currentNectarVelocity, runtime.seconds()));

            // Telemetry Output
            telemetry.addData("Mode", "Robot Centric");
            telemetry.addData("Sweep Power", sweep.getPower());
            telemetry.addData("Pollen Velocity", "Target: %.0f | Actual: %.0f", currentPollenTarget, currentPollenVelocity);
            //telemetry.addData("Nectar Velocity", "Target: %.0f | Actual: %.0f", currentNectarTarget, currentNectarVelocity);
            telemetry.addData("Stick Inputs", "Y: %.2f, X: %.2f, RX: %.2f", y, x, rx);
            telemetry.addData("Motor Powers", "FL: %.2f, FR: %.2f, BL: %.2f, BR: %.2f",
                    frontLeftPower, frontRightPower, backLeftPower, backRightPower);
            telemetry.update();
        }
    }
}
