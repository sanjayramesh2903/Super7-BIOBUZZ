package org.firstinspires.ftc.teamcode.Modules;

import org.firstinspires.ftc.teamcode.Core.Motor;

public class Shooter {
    Motor shooterOne, shooterTwo;

    //pid coefficients;
    public static double kv;

    public static double kp;
    public static double ki;
    public static double kd;
    public void setPowers(Motor shooterOne, Motor shooterTwo){

    }

    public Shooter(Motor shooterOne, Motor shooterTwo){
        this.shooterTwo = shooterTwo;
        this.shooterOne = shooterOne;
    }

}
