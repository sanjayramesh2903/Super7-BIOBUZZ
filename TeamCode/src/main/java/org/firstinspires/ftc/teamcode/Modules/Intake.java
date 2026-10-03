package org.firstinspires.ftc.teamcode.Modules;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Core.Motor;
public class Intake {
    Motor sweep;
    double currentThreshold = 8.9;
    public Intake(Motor sweep){
        this.sweep = sweep;
    }
    public void sweepIn(){
        sweep.setPower(-0.5);
    }
    public void sweepOut(){sweep.setPower(0.5);}
    public void stopSweep(){
        sweep.setPower(0);
    }


}
