package org.firstinspires.ftc.teamcode.Core;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class Pipeline extends OpenCvPipeline {

    private String output = "nothing";

    public Pipeline() {
    }

    // Mat is the image matrix that should be processed.
    @Override
    public Mat processFrame(Mat input) {
        output = "Sample Pipeline Is Running!";
        return input;
    }

    public String getOutput() {
        return output;
    }
}