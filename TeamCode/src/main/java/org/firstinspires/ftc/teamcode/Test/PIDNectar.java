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
public class PIDNectar extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx nectar;
        nectar = hardwareMap.get(DcMotorEx.class, "nectar");

        double kv = 0.012;

        double kp = 0.009;
        double ki;
        double kd;

        double targetVelo = 600;
        double TPS = 0;
        double error = 0;
        waitForStart();
        while (opModeIsActive()) {
            TPS = Math.abs(nectar.getVelocity());

            error = targetVelo - TPS;

            double p = kp * error;
            double feedforward = (kv * targetVelo);
            double pow = p + feedforward;

            nectar.setPower(pow);
            telemetry.addData("Velo", nectar.getVelocity());
            telemetry.update();
            if(gamepad1.circleWasPressed()){
                targetVelo += 50;
            }
            if(gamepad1.squareWasPressed()){
                targetVelo-=50;
            }
            telemetry.addData("Target velo", targetVelo);
            telemetry.addData("Nectar Velo", nectar.getVelocity());
        }
    }
}
