package org.firstinspires.ftc.teamcode.Modules;


import com.qualcomm.robotcore.hardware.CRServo;

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
