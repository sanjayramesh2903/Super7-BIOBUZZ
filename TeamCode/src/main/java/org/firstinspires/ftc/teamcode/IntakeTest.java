package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Core.Motor;
@TeleOp
public class IntakeTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{
        Motor intake = new Motor(hardwareMap, "intake");
        Motor shooterOne = new Motor(hardwareMap, "shooterOne");
        Motor shooterTwo = new Motor(hardwareMap, "shooterTwo");
        double currentThreshold = 8.9;
        waitForStart();
        while(opModeIsActive()){
            if(gamepad2.dpad_up){
                intake.setPower(-0.5);
            } else if(gamepad2.dpad_down){
                intake.setPower(0.5);
            } else{
                intake.setPower(0);
            }
            if(gamepad1.right_trigger > 0.05){
                shooterTwo.setPower(0.5);
                shooterOne.setPower(0.5);
            }
            if(intake.getCurrent()>currentThreshold){
                intake.setPower(-1);
            }
        }
    }

}
