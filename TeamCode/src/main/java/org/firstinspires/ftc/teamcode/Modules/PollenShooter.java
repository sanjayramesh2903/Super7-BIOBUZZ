package org.firstinspires.ftc.teamcode.Modules;

import org.firstinspires.ftc.teamcode.Core.Motor;

public class PollenShooter {
    Motor pollen;
    //pid coefficients;
    public static double kv = 0.0005;

    public static double kp = 0.01;
    public static double ki;
    public static double kd;

    double targetVelo = 800;
    double TPS = 0;
    double error = 0;
    public void powerTest(double d){
        pollen.setPower(d);
    }

    /*public void drive(double targetVelo){
        TPS = pollen.retMotorEx().getVelocity();

        error = targetVelo - pollen.retMotorEx().getVelocity();

        double p = kp * error;
        double feedforward = kv * targetVelo;

        double pow = p + feedforward;

        pollen.setPower(pow);
    }*/

    public PollenShooter(Motor pollen){
        this.pollen = pollen;
    }

}
