package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Core.Motor;
@TeleOp
public class ShooterTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{
        Motor s1 = new Motor(hardwareMap, "s1");
        Motor s2 = new Motor(hardwareMap, "s2");
        Motor intake = new Motor(hardwareMap, "intake");
        double shootPow = 0.5;
        s1.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();

        while(opModeIsActive()){

            if(gamepad1.right_trigger > 0.05){
                intake.setPower(0.8);
            }
            shootPow = Range.clip(shootPow, 0, 1);
            s1.setPower(shootPow);
            s2.setPower(shootPow);
            telemetry.addData("Power", shootPow);
            telemetry.update();
        }
    }

}
