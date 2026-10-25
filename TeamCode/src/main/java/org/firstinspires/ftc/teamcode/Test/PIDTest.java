package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Base;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.NectarShooter;
import org.firstinspires.ftc.teamcode.Modules.PollenShooter;
import org.firstinspires.ftc.teamcode.Core.Motor;
@TeleOp
public class PIDTest extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx pollen;
        pollen = hardwareMap.get(DcMotorEx.class, "pollen");

        double kv = 0.01212;

        double kp = 0.00923;
        double ki;
        double kd;

        double targetVelo = 1320;
        double TPS = 0;
        double error = 0;
        waitForStart();
        while (opModeIsActive()) {
            TPS = Math.abs(pollen.getVelocity());

            error = targetVelo - TPS;

            double p = kp * error;
            double feedforward = (kv * targetVelo);
            double pow = p + feedforward;

            pollen.setPower(pow);
            telemetry.addData("Velo", pollen.getVelocity());
            telemetry.update();
            if(gamepad1.circleWasPressed()){
                targetVelo += 50;
            }
            if(gamepad1.squareWasPressed()){
                targetVelo-=50;
            }
            telemetry.addData("Target velo", targetVelo);
        }
    }
}
