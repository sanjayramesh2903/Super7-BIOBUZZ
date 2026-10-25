package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Core.Motor;

public class NectarShooter {
    DcMotorEx nectar;

    //pid coefficients
    public static double kv;

    public static double kp;
    public static double ki;
    public static double kd;
    //Target velo before scale: 1100; After -> 1300 ish;
    double TPS = 0;
    double error = 0;
    public void powerTest(double d){
        nectar.setPower(d);
    }
    public void drive(double targetVelo){
        TPS = nectar.getVelocity();

        error = targetVelo - nectar.getVelocity();

        double p = kp * error;
        double feedforward = kv * targetVelo;

        double pow = p + feedforward;

        nectar.setPower(pow);
    }

    public void stopShooter(){
        nectar.setPower(0);
    }

    public NectarShooter(DcMotorEx nectar){
        this.nectar = nectar;
    }


}