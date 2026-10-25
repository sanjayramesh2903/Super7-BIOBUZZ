/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Pradeep;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/*
 * This file contains a minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="ShooterPIDTuning", group="Linear OpMode")
public class PIDTuningTeleOp extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx pollenShooter = null;
    private DcMotorEx nectarShooter = null;
    private FlyWheelPIDF shooterPID;
    private FlyWheelPIDF nectarPID;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        pollenShooter = hardwareMap.get(DcMotorEx.class, "pollen");
        pollenShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pollenShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        nectarShooter = hardwareMap.get(DcMotorEx.class, "nectar");
        nectarShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        nectarShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        shooterPID = new FlyWheelPIDF(0.0025, 0.0001, 0.0002, 0.00038);
        nectarPID = new FlyWheelPIDF(0.0025, 0.0001, 0.0002, 0.00038);

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        //pollenShooter.setDirection(DcMotor.Direction.FORWARD);
        //nectarShooter.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        double shooterPower = 0.5;
        double pollenTargetVelocity = 1600;
        double nectarTargetVelocity = 1600;
        double targetVelocityChange = 10;

        telemetry.addData("Status", "Systems Synchronized. Ready.");
        telemetry.update();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if(gamepad1.circleWasPressed()) {
                //shooterPower += 0.1;
                pollenTargetVelocity += targetVelocityChange;
            }

            if(gamepad1.squareWasPressed()) {
                //shooterPower -= 0.1;
                pollenTargetVelocity -= targetVelocityChange;
            }

            if(gamepad1.triangleWasPressed()) {
                nectarTargetVelocity += targetVelocityChange;
            }

            if(gamepad1.crossWasPressed()) {
                nectarTargetVelocity -= targetVelocityChange;
            }

            //shooterPower = Range.clip(shooterPower, 0, 1);
            pollenTargetVelocity = Range.clip(pollenTargetVelocity, 500, 2700);
            nectarTargetVelocity = Range.clip(nectarTargetVelocity, 500, 2700);

            shooterPID.setTarget(pollenTargetVelocity);
            double currentFlywheelSpeed = Math.abs(pollenShooter.getVelocity());
            pollenShooter.setPower(shooterPID.update(currentFlywheelSpeed, runtime.seconds()));

            nectarPID.setTarget(nectarTargetVelocity);
            double currentNectarSpeed = Math.abs(nectarShooter.getVelocity());
            nectarShooter.setPower(nectarPID.update(currentNectarSpeed, runtime.seconds()));

            telemetry.addData("Pollen Target Velocity", pollenTargetVelocity);
            telemetry.addData("Pollen Actual Velocity", currentFlywheelSpeed);
            telemetry.addData("Nectar Target Velocity", nectarTargetVelocity);
            telemetry.addData("Nectar Actual Velocity", currentNectarSpeed);
            telemetry.update();
        }
    }
}
