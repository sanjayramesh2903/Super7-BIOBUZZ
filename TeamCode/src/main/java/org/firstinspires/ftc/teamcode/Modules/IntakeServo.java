package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.hardware.Servo;

public class IntakeServo {
    Servo intakeServo;
    double start = 0.4;
    double feed = 0.83;
    public void moveToStart(){
        intakeServo.setPosition(start);
    }
    public void moveToFeed(){
        intakeServo.setPosition(feed);
    }
    public IntakeServo(Servo intakeServo){
        this.intakeServo = intakeServo;
    }
}
