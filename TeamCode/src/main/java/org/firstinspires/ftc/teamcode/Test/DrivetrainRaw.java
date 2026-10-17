package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Base;
import org.firstinspires.ftc.teamcode.Core.Motor;

@TeleOp
public class DrivetrainRaw extends Base {
    @Override
    public void runOpMode() throws InterruptedException{
        Motor fLeft = new Motor(hardwareMap, "fLeft");
        Motor bLeft = new Motor(hardwareMap, "bLeft");
        Motor bRight = new Motor(hardwareMap, "bRight");
        Motor fRight = new Motor(hardwareMap, "fRight");
        waitForStart();

        double powerCap = 0.9;
        final double TRIGGER = 0.05;

        while(opModeIsActive()){

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            fLeft.setPower(frontLeftPower);
            bLeft.setPower(backLeftPower);
            fRight.setPower(frontRightPower);
            bRight.setPower(backRightPower);
        }
    }
}
