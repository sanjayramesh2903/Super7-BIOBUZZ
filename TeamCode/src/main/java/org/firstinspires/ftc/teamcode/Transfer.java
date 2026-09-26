package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Transfer{
    CRServo transfer;
    public Transfer(CRServo transfer){
        this.transfer = transfer;
    }
    public void runTransferToShoot(){
        transfer.setPower(1);
    }
    public void runTransferAwayShoot(){
        transfer.setPower(1);
    }
    public void stopTransfer(){
        transfer.setPower(0);
    }
}
