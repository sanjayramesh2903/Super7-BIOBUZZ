package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

public class Camera {
    private OpenCvWebcam webcam;
    private HardwareMap hardwareMap;
    private Pipeline p1; // sample pipeline
    private Pipeline p2; // another sample pipeline

    public Camera(HardwareMap hw) { // hardware map from the base class is a parameter
        p1 = new Pipeline(); // initialize your pipeline classes
        p2 = new Pipeline();

        this.hardwareMap = hw;
        int cameraMonitorViewId =
                hardwareMap
                        .appContext
                        .getResources()
                        .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        // Get camera from hardware map, replace 'camera' with what is in your controlhub
        webcam =
                OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "camera"), cameraMonitorViewId);

        webcam.setPipeline(p1); // Setting the intial pipeline

        webcam.setMillisecondsPermissionTimeout(2500);

        // Streaming Frames
        webcam.openCameraDeviceAsync(
                new OpenCvCamera.AsyncCameraOpenListener() {
                    @Override
                    public void onOpened() {
                        webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                    }

                    @Override
                    public void onError(int errorCode) {}
                });
    }

    // Switching Between Pipelines
    public void switchToSecondPipeline(){
        webcam.setPipeline(p2);
    }

    public void switchToFirstPipeline(){
        webcam.setPipeline(p1);
    }

    // Get information from pipeline
    public String getPipeline1Output(){
        return p1.getOutput();
    }

    // call stop at the end of the opMode.
    public void stop() {
        webcam.stopStreaming();
    }
}