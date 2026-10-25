package org.firstinspires.ftc.teamcode.Pradeep;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TeleOp1", group="Main")
public class Test1TeleOp extends LinearOpMode {

    // Hardware
    private DcMotor leftFront, rightFront, leftRear, rightRear;
    private DcMotorEx shooterMotor;
    private GoBildaPinpointDriver pinpoint; 

    // Systems
    private FlyWheelPIDF shooterPID;
    private TargetingSystem targetingSystem;
    private ElapsedTime timer;

    // States
    private boolean shooterOn = false;
    private boolean previousGamepad1A = false;

    // Drivetrain Tuner Constant
    private final double TURN_KP = 1.2;

    @Override
    public void runOpMode() {
        // Initialize Drivetrain
        leftFront  = hardwareMap.get(DcMotor.class, "fLeft");
        rightFront = hardwareMap.get(DcMotor.class, "fRight");
        leftRear   = hardwareMap.get(DcMotor.class, "bLeft");
        rightRear  = hardwareMap.get(DcMotor.class, "bRight");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
        
        // Initialize Flywheel Shooter
        shooterMotor = hardwareMap.get(DcMotorEx.class, "pollen");
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Initialize Pinpoint Odometry Hub
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "odo");
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);

        // Instantiating Modular Objects
        shooterPID = new FlyWheelPIDF(0.0025, 0.0001, 0.0002, 0.00038);
        targetingSystem = new TargetingSystem();
        timer = new ElapsedTime();

        telemetry.addData("Status", "Systems Synchronized. Ready.");
        telemetry.update();

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {
            // Update sensor state tracking calculations
            pinpoint.update();
            targetingSystem.update(pinpoint.getPosX(), pinpoint.getPosY(), pinpoint.getHeading());

            // Flywheel Toggle Input Logic
            if (gamepad1.a && !previousGamepad1A) {
                shooterOn = !shooterOn;
            }
            previousGamepad1A = gamepad1.a;

            // Compute and apply Flywheel Speed
            if (shooterOn) {
                shooterPID.setTarget(targetingSystem.targetVelocity);
            } else {
                shooterPID.setTarget(0);
            }
            double currentFlywheelSpeed = shooterMotor.getVelocity();
            shooterMotor.setPower(shooterPID.update(currentFlywheelSpeed, timer.seconds()));

            // Mecanum Drivetrain Movement Processing
            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            // Auto-align Override Loop Mode
            if (gamepad1.left_bumper) {
                // If angle error is outside 1-degree tolerance window
                if (Math.abs(targetingSystem.headingError) > 0.017) {
                    turn = Math.max(-0.5, Math.min(0.5, targetingSystem.headingError * TURN_KP));
                } else {
                    turn = 0;
                }
            }

            // Kinematics Mixer Matrix
            double lfPower = drive + strafe + turn;
            double rfPower = drive - strafe - turn;
            double lrPower = drive - strafe + turn;
            double rrPower = drive + strafe - turn;

            leftFront.setPower(lfPower);
            rightFront.setPower(rfPower);
            leftRear.setPower(lrPower);
            rightRear.setPower(rrPower);

            // Inform Driver Profiles
            telemetry.addData("Flywheel", shooterOn ? "ON" : "OFF");
            telemetry.addData("Distance to Goal", "%.2f in", targetingSystem.distanceToGoal);
            telemetry.addData("Heading Error", "%.1f deg", Math.toDegrees(targetingSystem.headingError));
            telemetry.addData("Actual Flywheel Velocity", currentFlywheelSpeed);
            telemetry.update();
        }
    }
}
