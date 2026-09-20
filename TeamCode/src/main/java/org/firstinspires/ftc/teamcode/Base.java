package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Core.Angle;
import org.firstinspires.ftc.teamcode.Core.Motor;
import org.firstinspires.ftc.teamcode.Core.Point;


import java.util.ArrayList;
import java.util.List;

public abstract class Base extends LinearOpMode{

    public List<LynxModule> allHubs;
    public Motor fLeft, bLeft, fRight, bRight;


    GoBildaPinpointDriver odo;




    public void initHardware(HardwareMap hardwareMap){

        allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }


        //Drive Motors

        fLeft = new Motor(hardwareMap, "fLeft");
        fRight = new Motor(hardwareMap, "fRight");
        bLeft = new Motor(hardwareMap, "bLeft");
        bRight = new Motor(hardwareMap, "bRight");

        fLeft.noEncoder();
        fRight.noEncoder();
        bLeft.noEncoder();
        bRight.noEncoder();

        bLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        fLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        //Color Sensor Slot Detection
        //Odometry Initialization

        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo"); //2.75, 1.75

        odo.setOffsets(-1.75, 2.75, DistanceUnit.INCH);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.resetPosAndIMU();
//
        odo.setOffsets(-1.75, 2.75, DistanceUnit.INCH);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.resetPosAndIMU();


    }

    public void initHardware(HardwareMap hardwareMap, Boolean useCase){

        allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }


        //Drive Motors
        fLeft = new Motor(hardwareMap, "fLeft");
        fRight = new Motor(hardwareMap, "fRight");
        bLeft = new Motor(hardwareMap, "bLeft");
        bRight = new Motor(hardwareMap, "bRight");

        fLeft.noEncoder();
        fRight.noEncoder();
        bLeft.noEncoder();
        bRight.noEncoder();

        bLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        fLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //Initialize Modules


        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo"); //2.75, 1.75

    }

    //Clear Cache

    public void resetCache(){
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }

    //Basic Drive Controls

    public void driveFieldCentric(double drive, double turn, double strafe, double powerCap){
        Pose2D pos = odo.getPosition();

        double botHeading = pos.getHeading(AngleUnit.RADIANS);

        double rotX = strafe * Math.cos(-botHeading) - drive * Math.sin(-botHeading);
        double rotY = strafe * Math.sin(-botHeading) + drive * Math.cos(-botHeading);



        rotX = rotX * 1.1;

        double denominator = Math.max( Math.abs(rotY) + Math.abs(rotX) + Math.abs(turn) , 1);
        double fLeftPow = (rotY + rotX + turn) / denominator;
        double bLeftPow = (rotY - rotX + turn) / denominator;
        double fRightPow = (rotY - rotX - turn) / denominator;
        double bRightPow = (rotY + rotX - turn) / denominator;

        setDrivePowers(fLeftPow, fRightPow, bLeftPow, bRightPow, powerCap);
    }

    public void setDrivePowers(double fLeftPow, double fRightPow, double bLeftPow, double bRightPow, double powerCap){
        fLeft.setPower(fLeftPow * powerCap);
        fRight.setPower(fRightPow * powerCap);
        bLeft.setPower(bLeftPow * powerCap);
        bRight.setPower(bRightPow * powerCap);
    }

    public void stopDrive(){
        fLeft.setPower(0);
        fRight.setPower(0);
        bLeft.setPower(0);
        bRight.setPower(0);
    }



    //Position Getters

    public double getY(){
        Pose2D pos = odo.getPosition();

        return pos.getY(DistanceUnit.INCH);
    }

    public double getX(){
        Pose2D pos = odo.getPosition();

        return pos.getX(DistanceUnit.INCH);
    }

    public double getAngle(){
        odo.update();
        Pose2D pos = odo.getPosition();
        double angle = pos.getHeading(AngleUnit.DEGREES);
        return angle;
    }


    //STATIONARY MOVEMENT FUNCTIONS - MAKE ANY NECESSARY CHANGES ON A NEW OVERLOADED METHOD

    //Incorporates CarrotChase Movement Algorithm with constantly running Shooter PID
    //TODO: Add parameter for shooter power, instead of setting to fixed velocity
    //Classic Chase the Carrot Movement Function

    public void ChaseTheCarrot(
            ArrayList<Point> wp,
            int switchTolerance,
            int skip,
            boolean followSplineHeading,
            boolean invertSplineHeading,
            double heading,
            double error,
            double angleError,
            double normalMovementConstant,
            double finalMovementConstant,
            double turnConstant,
            double movementD,
            double turnD,
            double timeout, double powerCap) {
        ElapsedTime time = new ElapsedTime();
        resetCache();
        odo.update();
        Pose2D pos = odo.getPosition();


        double xDiff = Integer.MAX_VALUE, yDiff = Integer.MAX_VALUE, angleDiff = Integer.MAX_VALUE, prevTime = 0, prevXDiff = 0, prevYDiff = 0, prevAngleDiff = 0, splineHeading = 0, maxSpeed = 1;
        double finalSplineHeading = Angle.normalize(Math.toDegrees(Math.atan2(wp.get(wp.size() - 1).yP, wp.get(wp.size() - 1).xP)));
        int pt = 0;
        time.reset();
        while ((pt < wp.size() - 1
                || (Math.abs(pos.getX(DistanceUnit.INCH) - wp.get(wp.size() - 1).xP) > error
                || Math.abs(pos.getY(DistanceUnit.INCH) - wp.get(wp.size() - 1).yP) > error
                || ( followSplineHeading ?  ( invertSplineHeading ? Math.abs(Angle.normalize(finalSplineHeading - (-Angle.normalize(180 - getAngle())))) > angleError :
                                              Math.abs(Angle.normalize(finalSplineHeading - getAngle())) > angleError)
                : (heading == Double.MAX_VALUE
                   ? Math.abs(angleDiff) > 0
                   : Math.abs(Angle.normalize(heading - getAngle())) > angleError) )))

                && time.milliseconds() < timeout && opModeIsActive()) {

            odo.update();
            pos = odo.getPosition();



            resetCache();


            double x = pos.getX(DistanceUnit.INCH);
            double y = pos.getY(DistanceUnit.INCH);
            double theta = pos.getHeading(AngleUnit.DEGREES);


            if (getRobotDistanceFromPoint(wp.get(pt)) <= switchTolerance && pt != wp.size() - 1) {
                odo.update();

                resetCache();
                pt = Math.min(wp.size()-1, pt+skip);
            }

            Point destPt = wp.get(pt);
            xDiff = destPt.xP - x;
            yDiff = destPt.yP - y;
            splineHeading = Angle.normalize(Math.toDegrees(Math.atan2(yDiff, xDiff)));
            maxSpeed = destPt.speed;

            if(followSplineHeading) {
                if(pt == wp.size() - 1){
                    if(invertSplineHeading){
                        angleDiff = Angle.normalize(finalSplineHeading - (-Angle.normalize(180 - getAngle())));
                    }else {
                        angleDiff = Angle.normalize(finalSplineHeading - theta);
                    }
                }else {
                    if(invertSplineHeading){
                        angleDiff = Angle.normalize(splineHeading - (-Angle.normalize(180 - getAngle())));
                    }else {
                        angleDiff = Angle.normalize(splineHeading - theta);
                    }
                }
            }else{
                if(heading == Double.MAX_VALUE){
                    if(wp.get(pt).invertSpline){
                        if(pt == wp.size() - 1){
                            angleDiff = Angle.normalize(finalSplineHeading - (-Angle.normalize(180 - getAngle())));
                        }else {
                            angleDiff = Angle.normalize(splineHeading - (-Angle.normalize(180 - getAngle())));
                        }
                    }else if(wp.get(pt).spline){
                        if(pt == wp.size() - 1){
                            angleDiff = Angle.normalize(finalSplineHeading - theta);
                        }else {
                            angleDiff = Angle.normalize(splineHeading - theta);
                        }
                    }else{
                        angleDiff = Angle.normalize(wp.get(wp.size() - 1).ang - theta);
                    }
                }else{
                    angleDiff = Angle.normalize(heading - theta);
                }
            }

            double xPow=0, yPow=0, turnPow=0;

            turnPow += angleDiff * turnConstant;
            if (pt == wp.size() - 1) {
                xPow += xDiff * finalMovementConstant;
                yPow += yDiff * finalMovementConstant;
                xPow += movementD * (xDiff - prevXDiff) / (time.seconds() - prevTime);
                yPow += movementD * (yDiff - prevYDiff) / (time.seconds() - prevTime);
                turnPow += turnD * (angleDiff - prevAngleDiff) / (time.seconds() - prevTime);
                System.out.println((movementD * (xDiff - prevXDiff) / (time.seconds() - prevTime)));
            } else {
                xPow += xDiff * normalMovementConstant;
                yPow += yDiff * normalMovementConstant;
            }

            turnPow = Range.clip(turnPow, -1, 1);
            xPow = Range.clip(xPow, -maxSpeed, maxSpeed);
            yPow = Range.clip(yPow, -maxSpeed, maxSpeed);
            System.out.println(maxSpeed);


            prevTime = time.seconds();
            prevXDiff = xDiff;
            prevYDiff = yDiff;
            prevAngleDiff = angleDiff;

            //driveFieldCentric(-yPow, -turnPow, xPow);
            telemetry.addData("target: ", destPt);
            telemetry.update();
            driveFieldCentric(-xPow, turnPow, yPow, powerCap);

        }
        stopDrive();
    }


    public void driveFieldCentricAuto(double drive, double strafe, double angle, double speedCap){
        Pose2D pos = odo.getPosition();

        double botHeading = pos.getHeading(AngleUnit.RADIANS);

        double rotX = strafe * Math.cos(-botHeading) - drive * Math.sin(-botHeading);
        double rotY = strafe * Math.sin(-botHeading) + drive * Math.cos(-botHeading);
        double angleError = normalizeAngle(angle - pos.getHeading(AngleUnit.DEGREES));
        double anglePow = -0.01 * angleError;

        rotX *= 1.1;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(anglePow), 1);
        double fLeftPow = (rotY + rotX + anglePow) / denominator;
        double bLeftPow = (rotY - rotX + anglePow) / denominator;
        double fRightPow = (rotY - rotX - anglePow) / denominator;
        double bRightPow = (rotY + rotX - anglePow) / denominator;

        setDrivePowers(fLeftPow, fRightPow, bLeftPow, bRightPow, speedCap);




    }



    public double normalizeAngle(double rawAngle) {
        double scaledAngle = rawAngle % 360;
        if (scaledAngle < 0) {
            scaledAngle += 360;
        }

        if (scaledAngle > 180) {
            scaledAngle -= 360;
        }

        return scaledAngle;
    }

    public double getRobotDistanceFromPoint(Point p2) {
        return Math.sqrt((p2.yP - getY()) * (p2.yP - getY()) + (p2.xP - getX()) * (p2.xP - getX()));
    }

    //Overloaded Movement Methods

    public void ChaseTheCarrotConstantHeading(ArrayList<Point> wp,
                                              int switchTolerance,
                                              int skip,
                                              double heading,
                                              double error,
                                              double angleError,
                                              double normalMovementConstant,
                                              double finalMovementConstant,
                                              double turnConstant,
                                              double movementD,
                                              double turnD,
                                              double timeout, double powerCap) {
        ChaseTheCarrot(wp, switchTolerance, skip, false, false, heading, error, angleError, normalMovementConstant, finalMovementConstant, turnConstant, movementD, turnD, timeout, powerCap);
    }

    public void ChaseTheCarrotConstantHeading(ArrayList<Point> wp, double heading, double timeout, double powerCap){

        ChaseTheCarrot(wp, 9, 3, false, false, heading,3, 1,  0.05, 0.05, 0.03, 0.0005, 0, timeout, powerCap);
    }










}