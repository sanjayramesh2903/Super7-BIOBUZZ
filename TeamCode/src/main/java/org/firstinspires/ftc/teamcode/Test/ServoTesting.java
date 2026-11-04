package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoTesting extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Servo s = hardwareMap.get(Servo.class, "s");
        double startPos = 0;
        s.setPosition(0);
        waitForStart();

        while(opModeIsActive()){
            if(gamepad1.dpadUpWasPressed()){
                startPos -= 0.01;
                s.setPosition(startPos);
            }
            if(gamepad1.dpadDownWasPressed()){
                startPos += 0.01;
                s.setPosition(startPos);
            }
            telemetry.addData("Servo pos", s.getPosition());
            telemetry.update();
        }



    }
}
