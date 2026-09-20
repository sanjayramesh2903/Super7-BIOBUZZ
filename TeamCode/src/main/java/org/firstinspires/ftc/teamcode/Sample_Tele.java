package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class Sample_Tele extends Base {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(hardwareMap, 0);
        waitForStart();

        double drive = 0, turn = 0, strafe = 0;
        Servo one, two;
        one = hardwareMap.servo.get("one");
        two = hardwareMap.servo.get("two");

        double OPEN = 0, CLOSE = 1;
        boolean closed = false;
        //Button Variable
        boolean lastCycle = false, currCycle = false;
        boolean lastCycle2 = false, currCycle2 = false;
        double powerCap = 1;

        boolean timerVar = false;
        boolean timerVar2 = false;

        ElapsedTime timer = new ElapsedTime();
        ElapsedTime timer2 = new ElapsedTime();

        while (opModeIsActive()) {
            resetCache();

            LLResult result = limelight.getLatestResult();
            if (result != null) {
                resetCache();
                if (result.isValid()) {

                    int tagIDR = result.getFiducialResults().get(0).getFiducialId();
                    Pose3D botpose = result.getBotpose();
                    telemetry.addData("ID", tagIDR);
                    telemetry.update();

                }

                drive = gamepad1.left_stick_y;
                turn = gamepad1.left_stick_x;
                strafe = gamepad1.right_stick_x;

                if (gamepad1.right_trigger > 0.05) {
                    powerCap = 0.4;
                }

                lastCycle = currCycle;
                currCycle = gamepad1.a;
                if (!lastCycle && currCycle) {
                    closed = !closed;
                    if (closed) {
                        one.setPosition(CLOSE);
                    } else {
                        one.setPosition(OPEN);
                    }
                }

                lastCycle2 = currCycle2;
                currCycle2 = gamepad1.b;
                if (!lastCycle && currCycle) { //Step 1
                    one.setPosition(0.5);
                    timer.reset();
                    timerVar = true;

                }

                if (timerVar && timer.milliseconds() > 350) {  //Step 2
                    two.setPosition(0.5);
                    timerVar = false;
                    timer2.reset();
                    timerVar2 = true;
                }

                if (timerVar2 && timer2.milliseconds() > 250) {
                    //Step 3
                    timerVar2 = false;
                }


                driveFieldCentric(drive, turn, strafe, powerCap);


            }

        }
    }
}