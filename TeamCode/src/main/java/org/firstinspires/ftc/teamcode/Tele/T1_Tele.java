package org.firstinspires.ftc.teamcode.Tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Base;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.PollenShooter;
import org.firstinspires.ftc.teamcode.Modules.NectarShooter;

@TeleOp
public class T1_Tele extends Base {
    @Override
    public void runOpMode() throws InterruptedException{
        initHardware(hardwareMap);
        Intake intake = new Intake(sweep);
        PollenShooter pollenShooter = new PollenShooter(pollen);
        NectarShooter nectarShooter = new NectarShooter(nectar);
        waitForStart();
        double nPow = 0.1;
        double pPow = 0.1;
        double powerCap = 0.9;
        final double TRIGGER = 0.05;
        double currentThreshold = 8.9;

        while(opModeIsActive()){
            //Field-Centric Driving
            double drive = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x ;
            double strafe = -gamepad1.left_stick_x;

            driveFieldCentric(drive, turn, strafe, powerCap);
            if(gamepad1.right_trigger > 0.05){
                pollen.setPower(pPow);
                nectar.setPower(nPow);
            } else{
                nectar.setPower(0);
                pollen.setPower(0);
            }
            if(gamepad1.squareWasPressed()){
                pPow-=0.1;
                nPow-=0.1;
            }
            if(gamepad1.circleWasPressed()){
                pPow+=0.1;
                nPow+=0.1;
            }
            telemetry.addData("Pollen", pPow);
            telemetry.addData("Nectar", nPow);
            telemetry.update();
            //Intake logic lol
            if(gamepad2.dpad_up){
                intake.sweepOut();
            } else if(gamepad2.dpad_down){
                intake.sweepIn();
            } else{
                intake.stopSweep();
            }
            //Jam resistance
            if(sweep.getCurrent()>currentThreshold){
                sweep.setPower(-1);
            }



        }
    }
}
