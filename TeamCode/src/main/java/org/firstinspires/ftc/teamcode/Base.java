package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

public abstract class Base extends LinearOpMode {
    public List<LynxModule> allHubs;
    public void initHardware(){

    }

    public void resetCache(){
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }
}
