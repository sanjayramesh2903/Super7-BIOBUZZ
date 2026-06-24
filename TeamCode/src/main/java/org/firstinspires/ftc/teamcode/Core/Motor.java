package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Motor {
    DcMotor motor;
    int multiplier = 1;

    public Motor(HardwareMap hardwareMap, String name){
        this.motor = hardwareMap.dcMotor.get(name);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        try{
            resetEncoder(true);
        } catch(Exception e){
            noEncoder();
        }
    }

    public void setDirection(DcMotorSimple.Direction d) {motor.setDirection(d); }

    public DcMotorSimple.Direction getDirection() {return motor.getDirection();}

    public void coast(){motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);}

    public void negateEncoder() {multiplier = -1;}

    public int encoderReading() {return motor.getCurrentPosition() * multiplier;}

    public void setPower(double power) {this.motor.setPower(power);}

    public void resetEncoder(boolean useEncoder){
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(useEncoder) {
            useEncoder();
        }else{
            noEncoder();
        }
    }

    public void noEncoder() {this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);}

    public void useEncoder() {this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);}

    public void toPosition() {this.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);}

    public DcMotorEx retMotorEx() {return (DcMotorEx) motor;}
}