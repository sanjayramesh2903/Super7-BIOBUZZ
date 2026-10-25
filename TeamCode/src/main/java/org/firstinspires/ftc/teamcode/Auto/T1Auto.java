package org.firstinspires.ftc.teamcode.Auto;
import java.util.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Base;
import org.firstinspires.ftc.teamcode.Core.Point;

@Autonomous
public class T1Auto extends Base {
    ArrayList<Point> intake1, backToStart, shoot2, flower1;
    @Override
    //ChaseTheCarrotConstantHeading(folksandem, 9, 3, 0, 3, 1, 0.05, 0.05, 0.03, 0.0005, 0.0005, 2500, 0.7);
    public void runOpMode() throws InterruptedException {
        intake1 = new ArrayList<>();
        intake1.addAll(
                new ArrayList<>(
                        Arrays.asList(
                                new Point(0,0,0,0)
                        )
                )
        );
    }
}
