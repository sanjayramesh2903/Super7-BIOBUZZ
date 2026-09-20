package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoTesting extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Servo s = hardwareMap.servo.get("testing");
        s.setPosition(0.5);
        waitForStart();

        while(opModeIsActive()){

            if(gamepad1.dpad_right){
                s.setPosition(s.getPosition() + 0.01);
            }else if(gamepad1.dpad_left){
                s.setPosition(s.getPosition() - 0.01);
            }

            telemetry.addData("Servo", s.getPosition());
            telemetry.update();
        }



    }
}
