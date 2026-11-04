package org.firstinspires.ftc.teamcode.Pradeep;

public class FlyWheelPIDF {
    private double kp, ki, kd, kF;
    private double targetVelocity = 0;
    private double integralSum = 0;
    private double lastError = 0;
    private double lastTime = 0;
    
    private double filteredVelocity = 0;
    private final double filterGain = 0.8; // Low-pass filter smoothing parameter

    public FlyWheelPIDF(double kp, double ki, double kd, double kF) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.kF = kF;
    }

    public void setTarget(double target) {
        if (target == 0 && this.targetVelocity != 0) {
            // Reset controller state when target is cleared
            integralSum = 0;
            lastError = 0;
            filteredVelocity = 0;
        }
        this.targetVelocity = target;
    }

    public double getTarget() {
        return targetVelocity;
    }

    public double update(double currentRawVelocity, double currentTimeSeconds) {
        if (targetVelocity <= 0) {
            integralSum = 0;
            lastError = 0;
            filteredVelocity = 0;
            lastTime = currentTimeSeconds;
            return 0.0;
        }

        // Low-pass filter to reject sensor noise
        filteredVelocity = (filterGain * filteredVelocity) + ((1 - filterGain) * currentRawVelocity);

        double error = targetVelocity - filteredVelocity;
        double deltaTime = currentTimeSeconds - lastTime;
        
        if (deltaTime <= 0 || deltaTime > 0.5) {
            deltaTime = 0.02; // Guard against initial frame jump or pauses
        }

        // Integral window to avoid windup
        double integralZone = 150;
        if (Math.abs(error) < integralZone) {
            integralSum += error * deltaTime;
        } else {
            integralSum = 0;
        }

        double derivative = (error - lastError) / deltaTime;

        lastError = error;
        lastTime = currentTimeSeconds;

        double outP = error * kp;
        double outI = integralSum * ki;
        double outD = derivative * kd;
        double outF = targetVelocity * kF;

        // Flywheels only run forward: clamp output power to [0.0, 1.0]
        return Math.max(0.0, Math.min(1.0, outP + outI + outD + outF));
    }
}
