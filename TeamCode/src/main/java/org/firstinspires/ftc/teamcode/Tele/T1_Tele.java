
package org.firstinspires.ftc.teamcode.Tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Base;
import org.firstinspires.ftc.teamcode.Modules.Intake;
import org.firstinspires.ftc.teamcode.Modules.PollenShooter;
import org.firstinspires.ftc.teamcode.Modules.NectarShooter;

@TeleOp
public class T1_Tele extends Base {
    @Override
    public void runOpMode() throws InterruptedException{
        initHardware(hardwareMap);

        Intake intake = new Intake(sweep);

        waitForStart();
        double kv = 0.01212;

        double kp = 0.00923;
        double ki;
        double kd;
        double targetVelo = 1320;
        double TPS = 0;
        double error = 0;
        double powerCap = 0.9;
        double currentThreshold = 8.9;

        while(opModeIsActive()){
            //Field-Centric Driving
            double drive = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;
            double strafe = -gamepad1.left_stick_x;

            driveFieldCentric(drive, turn, strafe, powerCap);

            //Shooter
            if(gamepad2.right_trigger > 0.05) {
                TPS = Math.abs(pollen.getVelocity());

                error = targetVelo - TPS;

                double p = kp * error;
                double feedforward = (kv * targetVelo);
                double pow = p + feedforward;

                pollen.setPower(pow);
            } else {
                pollen.setPower(0);
            }

            if(gamepad2.left_trigger > 0.05) {
                //nectarShooter.drive(1200?)
                nectar.setPower(0.6);
            } else {
                nectar.setPower(0);
            }

            //Intake
            if(gamepad1.dpad_up) {
                intake.sweepOut();
            } else if(gamepad1.dpad_down) {
                intake.sweepIn();
            } else {
                intake.stopSweep();
            }

            //Jam resistance
            if(sweep.getCurrent()>currentThreshold) {
                sweep.setPower(-1);
            }
            
            telemetry.addData("Pollen Shooter", pollen.getVelocity());
            telemetry.addData("Nectar Shooter", nectar.getVelocity());
            telemetry.addData("Sweeper Current", sweep.getCurrent());
            telemetry.addData("X", getX());
            telemetry.addData("Y", getY());
            telemetry.update();



        }
    }
}
