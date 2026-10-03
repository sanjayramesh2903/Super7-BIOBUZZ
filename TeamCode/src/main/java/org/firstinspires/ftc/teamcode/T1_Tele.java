package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.Shooter;
import org.firstinspires.ftc.teamcode.Modules.Transfer;

public class T1_Tele extends Base {
    @Override
    public void runOpMode() throws InterruptedException{
        initHardware(hardwareMap);
        Intake intake = new Intake(sweep);
        Shooter shooter = new Shooter(shooterOne, shooterTwo);
        Transfer transfer = new Transfer(transferServo);
        waitForStart();

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
                shooterOne.setPower(0.6);
                shooterTwo.setPower(0.6);
            } else{
                shooterOne.setPower(0);
                shooterTwo.setPower(0);
            }

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


            //transgfer
            if(gamepad1.left_bumper){
                transfer.runTransferToShoot();
            } else if(gamepad1.right_bumper){
                transfer.runTransferAwayShoot();
            } else{
                transfer.stopTransfer();
            }

        }
    }
}
