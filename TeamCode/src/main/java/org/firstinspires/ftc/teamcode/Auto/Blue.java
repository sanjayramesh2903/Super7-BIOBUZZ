package org.firstinspires.ftc.teamcode.Auto;
import java.util.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Base;
import org.firstinspires.ftc.teamcode.Core.Point;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Core.Motor;
import org.firstinspires.ftc.teamcode.Core.Point;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.IntakeServo;

@Autonomous
public class Blue extends Base {
    ArrayList<Point> park;
    @Override
    //ChaseTheCarrotConstantHeading(folksandem, 9, 3, 0, 3, 1, 0.05, 0.05, 0.03, 0.0005, 0.0005, 2500, 0.7);
    public void runOpMode() throws InterruptedException {
        double kv = 0.01009;
        double kp = 0.00856;
        double ki;
        double kd;
        double targetVelo = 710;
        double TPS = 0;
        double error = 0;
        boolean shooterOn = true;
        initHardware(hardwareMap);
        DcMotorEx pollen;
        pollen = hardwareMap.get(DcMotorEx.class, "pollen");
        Intake intake = new Intake(sweep);
        IntakeServo feeder = new IntakeServo(intakeServo);
        ElapsedTime firstShoot = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        ElapsedTime firstIntake = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        park = new ArrayList<>();
        park.addAll(
                new ArrayList<>(
                        Arrays.asList(
                                new Point(3,0,0,0.8)
                        )
                )
        );
        intakeServo.setPosition(0.4);
        waitForStart();
        firstShoot.reset();
        firstIntake.reset();
        //moveToPosition(10,-30,0,2,2,3000);
        //ChaseTheCarrotConstantHeading(park, 9, 3, 0, 3, 1, 0.05, 0.05, 0.03, 0.0005, 0.0005, 2500, 0.7);

        firstShoot.reset();
        while(firstShoot.time() < 9000){
            if(firstShoot.time() > 1000 && firstShoot.time() < 1500){
                intakeServo.setPosition(0.83);
            }
            if(firstShoot.time() > 2000 && firstShoot.time() < 2500){
                intakeServo.setPosition(0.4);
            }
            if(firstShoot.time() > 3000 && firstShoot.time() < 3500){
                intakeServo.setPosition(0.83);
            }
            if(firstShoot.time() > 4000 && firstShoot.time() < 4500){
                intakeServo.setPosition(0.4);
            }
            if(firstShoot.time() > 5000 && firstShoot.time() < 5500){
                intakeServo.setPosition(0.83);
            }
            if(firstShoot.time() > 6000 && firstShoot.time() < 6500){
                intakeServo.setPosition(0.4);
            }
            if(firstShoot.time() > 7000 && firstShoot.time() < 7500){
                intakeServo.setPosition(0.83);
            }
            if(firstShoot.time() > 8000 && firstShoot.time() < 8500){
                intakeServo.setPosition(0.4);
            }
            if(firstShoot.time() > 2200){
                sweep.setPower(-0.45);
            } else{
                sweep.setPower(0);
            }
            TPS = Math.abs(pollen.getVelocity());

            error = targetVelo - TPS;

            double p = kp * error;
            double feedforward = (kv * targetVelo);
            double pow = p + feedforward;

            pollen.setPower(-pow);
        }
        pollen.setPower(0);
        intake.stopSweep();
        sleep(500);
        firstIntake.reset();
        while(firstIntake.time() < 75){
            driveFieldCentricAuto(-0.8,0,0,0.8);
        }
        sleep(500);
        firstIntake.reset();
        while(firstIntake.time() < 500){
            driveFieldCentricAuto(0,-0.8,0,0.8);
        }
        sleep(500);
        firstIntake.reset();
        while(firstIntake.time() < 2000){
            driveFieldCentricAuto(-0.7,0,0,0.7);
        }




    }
}
