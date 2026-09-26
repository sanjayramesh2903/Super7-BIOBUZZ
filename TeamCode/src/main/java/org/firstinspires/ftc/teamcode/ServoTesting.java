package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp
public class ServoTesting extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        CRServo s = hardwareMap.get(CRServo.class, "s");

        waitForStart();

        while(opModeIsActive()){
            if(gamepad1.left_bumper){
                s.setPower(1);
            } else if(gamepad1.right_bumper){
                s.setPower(-1);
            }
        }



    }
}
