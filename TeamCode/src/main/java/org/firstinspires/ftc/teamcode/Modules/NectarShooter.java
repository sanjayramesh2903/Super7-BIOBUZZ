package org.firstinspires.ftc.teamcode.Modules;

import org.firstinspires.ftc.teamcode.Core.Motor;

public class NectarShooter {
    Motor nectar;

    //pid coefficients;
    public static double kv;

    public static double kp;
    public static double ki;
    public static double kd;

    double targetVelo = 800;
    double TPS = 0;
    double error = 0;
    public void powerTest(double d){
        nectar.setPower(d);
    }
    /*public void setPowers(double targetVelo){
        TPS = nectar.retMotorEx().getVelocity();

        error = targetVelo - nectar.retMotorEx().getVelocity();

        double p = kp * error;
        double feedforward = kv * targetVelo;

        double pow = p + feedforward;

        nectar.setPower(pow);
    }*/

    public NectarShooter(Motor pollen){
        this.nectar = nectar;
    }

}