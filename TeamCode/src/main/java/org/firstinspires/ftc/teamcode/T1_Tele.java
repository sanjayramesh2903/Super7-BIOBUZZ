package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.Shooter;

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

        while(opModeIsActive()){
            //Field-Centric Driving
            //double drive = gamepad1.left_stick_y;
            //double turn = -gamepad1.right_stick_x ;
            //double strafe = -gamepad1.left_stick_x;

            //driveFieldCentric(drive, turn, strafe, powerCap);
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
