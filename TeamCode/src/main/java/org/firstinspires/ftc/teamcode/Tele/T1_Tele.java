
package org.firstinspires.ftc.teamcode.Tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Base;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.IntakeServo;
import org.firstinspires.ftc.teamcode.Pradeep.FlyWheelPIDF;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
@TeleOp
public class T1_Tele extends Base {
    @Override
    public void runOpMode() throws InterruptedException{
        initHardware(hardwareMap);
        DcMotorEx pollen;
        pollen = hardwareMap.get(DcMotorEx.class, "pollen");
        Intake intake = new Intake(sweep);
        IntakeServo feeder = new IntakeServo(intakeServo);
        ElapsedTime feederTimer = new ElapsedTime();
        double kv = 0.01212;

        double kp = 0.00923;
        double ki;
        double kd;
        double targetVelo = 670; // scale
        double TPS = 0;
        double error = 0;
        double powerCap = 0.9;
        double currentThreshold = 8.9;
        boolean isServoUp = true;

        //FlyWheelPIDF pollenPID = new FlyWheelPIDF(0.0025, 0.0001, 0.0002, 0.00038);
        //ElapsedTime runtime = new ElapsedTime();
        //runtime.reset();

        intakeServo.setPosition(0);
        waitForStart();

        while(opModeIsActive()){
            //Field-Centric Driving
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            double strafe = gamepad1.left_stick_x;

            driveFieldCentric(drive, turn, strafe, powerCap);

            TPS = Math.abs(pollen.getVelocity());

            error = targetVelo - TPS;

            double p = kp * error;
            double feedforward = (kv * targetVelo);
            double pow = p + feedforward;

            //Shooter
            //double currentPollenTarget = 0;
            if(gamepad2.right_trigger > 0.05) {
                targetVelo = 670;
                pollen.setPower(pow);
            }
            else {
               pollen.setPower(0);
            }
            //pollenPID.setTarget(currentPollenTarget);
            //double currentPollenVelocity = Math.abs(pollen.getVelocity());
            //double pollenPower = pollenPID.update(currentPollenVelocity, runtime.seconds());
            //pollen.setPower(pollenPower);

            //Intake
            if(gamepad1.triangle) {
                intake.sweepOut();
            } else if(gamepad1.cross) {
                intake.sweepIn();
            } else {
                intake.stopSweep();
            }

            //Jam resistance
            if(sweep.getCurrent()>currentThreshold) {
                sweep.setPower(-1);
            }

            if(gamepad2.triangle){
                intakeServo.setPosition(0.83);
             } else{
                intakeServo.setPosition(0.4);
             }


            telemetry.addData("Pollen Shooter", pollen.getVelocity());
            telemetry.addData("Sweeper Current", sweep.getCurrent());
            telemetry.addData("X", getX());
            telemetry.addData("Y", getY());
            telemetry.addData("Feeder pos", intakeServo.getPosition());
            telemetry.update();



        }
    }
}