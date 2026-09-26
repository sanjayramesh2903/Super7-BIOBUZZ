package org.firstinspires.ftc.teamcode.Modules;
import org.firstinspires.ftc.teamcode.Core.Motor;
public class Intake {
    Motor sweep;

    public Intake(Motor sweep){
        this.sweep = sweep;
    }
    public void sweepIn(){
        sweep.setPower(-1);
    }
    public void sweepOut(){
        sweep.setPower(1);
    }
    public void stopSweep(){
        sweep.setPower(0);
    }

}
