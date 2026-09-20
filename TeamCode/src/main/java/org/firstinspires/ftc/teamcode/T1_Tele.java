package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Modules.Intake;

public class T1_Tele extends Base {
    @Override
    public void runOpMode() throws InterruptedException{
        initHardware(hardwareMap);
        Intake intake = new Intake(sweep);

        waitForStart();

        double powerCap = 0.9;
        final double TRIGGER = 0.05;

        while(opModeIsActive()){
            //Field-Centric Driving
            double drive = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x ;
            double strafe = -gamepad1.left_stick_x;

            driveFieldCentric(drive, turn, strafe, powerCap);

            //Intake logic lol
            if(gamepad2.dpad_up){
                intake.sweepOut();
            } else if(gamepad2.dpad_down){
                intake.sweepIn();
            } else{
                intake.stopSweep();
            }

        }
    }
}
