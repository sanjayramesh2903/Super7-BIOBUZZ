package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Core.Motor;

public class PollenShooter {
    DcMotorEx pollen;

    public static double kv = 0.01212;

    public static double kp = 0.00923;
    double ki;
    double kd;

    //double targetVelo = 1320 (before scale) after -> 1620 ish;
    double TPS = 0;
    double error = 0;
    public void drive(double targetVelo){
        TPS = Math.abs(pollen.getVelocity());

        error = pollen.getVelocity();

        double p = kp * error;
        double feedforward = (kv * targetVelo);
        double pow = p + feedforward;

        pollen.setPower(pow);
    }
    public PollenShooter(DcMotorEx pollen){
        this.pollen = pollen;
    }
    public void stopShooter(){
        pollen.setPower(0);
    }

    }
