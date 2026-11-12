package org.firstinspires.ftc.teamcode.Tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Base;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.IntakeServo;

@TeleOp
public class LM1TeleopRealNotFake extends Base {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(hardwareMap);
        DcMotorEx pollen;
        pollen = hardwareMap.get(DcMotorEx.class, "pollen");
        Intake intake = new Intake(sweep);
        IntakeServo feeder = new IntakeServo(intakeServo);
        double kv = 0.01009;
        double kp = 0.00856;
        double ki;
        double kd;
        double powerCap = 0.9;
        double currentThreshold = 8.9;
        boolean isServoUp = true;

        double targetVelo = 670;
        double TPS = 0;
        double error = 0;
        intakeServo.setPosition(0.4);
        waitForStart();
        while (opModeIsActive()) {
            //SHOOTER{
            TPS = Math.abs(pollen.getVelocity());

            error = targetVelo - TPS;

            double p = kp * error;
            double feedforward = (kv * targetVelo);
            double pow = p + feedforward;

            if(gamepad2.left_trigger > 0.05){
                pollen.setPower(-pow);
            } else{
                pollen.setPower(0);
            }

            if(gamepad2.circleWasPressed()){
                targetVelo += 15;
            }
            if(gamepad2.squareWasPressed()){
                targetVelo-=15;
            }
            if(gamepad2.dpad_down){
                pollen.setPower(0.3);
            }
            //}

            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            double strafe = gamepad1.left_stick_x;

            driveFieldCentric(drive, turn, strafe, 1);
            //Intake
            if(gamepad1.triangle) {
                sweep.setPower(0.45);
            } else if(gamepad1.cross) {
                sweep.setPower(-0.45);
            } else {
                intake.stopSweep();
            }


            //if(sweep.getCurrent()>currentThreshold) {
              //  sweep.setPower(-1);
            //}

            //Servo
            if(gamepad2.triangle){
                intakeServo.setPosition(0.4);
            } else{
                intakeServo.setPosition(0.83);
            }


            telemetry.addData("Sweeper Current", sweep.getCurrent());
            telemetry.addData("X", getX());
            telemetry.addData("Y", getY());
            telemetry.addData("Feeder pos", intakeServo.getPosition());
            telemetry.addData("Target velo", targetVelo);
            telemetry.addData("Velo", pollen.getVelocity());
            telemetry.update();

        }
    }
}
