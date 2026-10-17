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
public class PIDTest extends Base {
    @Override
    public void runOpMode() throws InterruptedException {
        PollenShooter pollenShooter = new PollenShooter(pollen);
        double kv = 0.0005;

        double kp = 0.01;
        double ki;
        double kd;

        double targetVelo = 800;
        double TPS = 0;
        double error = 0;
        waitForStart();
        while (opModeIsActive()) {
            TPS = pollen.retMotorEx().getVelocity();

            error = targetVelo - pollen.retMotorEx().getVelocity();

            double p = kp * error;
            double feedforward = kv * targetVelo;

            double pow = p + feedforward;

            pollen.setPower(pow);
        }
    }
}
