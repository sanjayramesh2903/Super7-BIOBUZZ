package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Core.Point;

import java.util.ArrayList;
import java.util.Arrays;

public class Sample_Auto extends Base{

    ArrayList<Point> point1, point2, point3;


    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(hardwareMap, 0);

        point1 = new ArrayList<>();
        point1.addAll(
                new ArrayList<>(
                        Arrays.asList(
                                new Point(8, -10, 0, 1)
                        )
                )
        );

        waitForStart();

        ChaseTheCarrotConstantHeading(point1, 9, 3, 30, 3, 1, 0.05, 0.05, 0.03, 0.0005, 0, 2500, 1);
        sleep(250);

        ChaseTheCarrotConstantHeading(point1, 9, 3, 30, 3, 1, 0.05, 0.05, 0.03, 0.0005, 0, 2500, 1);

        stopDrive();

        //Skip -> Lookahead
        //Choppy ->Increase Skip
        //Not accurate path -> Decrease Skip



    }
}
