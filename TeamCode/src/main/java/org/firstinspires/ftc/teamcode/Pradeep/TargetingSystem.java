package org.firstinspires.ftc.teamcode.Pradeep;

public class TargetingSystem {
    // BioBuzz Goal Coordinates (Modify these values per the official manual layout)
    private static final double GOAL_X = 72.0;
    private static final double GOAL_Y = 72.0;

    public double distanceToGoal = 0;
    public double headingError = 0;
    public double targetVelocity = 0;

    public void update(double robotX, double robotY, double robotHeading) {
        double deltaX = GOAL_X - robotX;
        double deltaY = GOAL_Y - robotY;
        
        // Calculate hypotenuse vector distance
        this.distanceToGoal = Math.hypot(deltaX, deltaY);
        
        // Calculate absolute angle to goal target
        double targetAbsoluteHeading = Math.atan2(deltaY, deltaX);
        
        // Compute relative error offset
        double error = targetAbsoluteHeading - robotHeading;
        
        // Normalize angle within boundary bounds (-PI to +PI)
        while (error > Math.PI) error -= 2 * Math.PI;
        while (error < -Math.PI) error += 2 * Math.PI;
        this.headingError = error;

        // Fetch interpolated speed map metrics
        this.targetVelocity = getTargetVelocityForDistance(this.distanceToGoal);
    }

    private double getTargetVelocityForDistance(double distance) {
        if (distance <= 30) return 1600;
        if (distance >= 100) return 2500;
        
        if (distance > 30 && distance <= 50) {
            return 1600 + (distance - 30) * (1950 - 1600) / (50 - 30);
        }
        if (distance > 50 && distance <= 75) {
            return 1950 + (distance - 50) * (2200 - 1950) / (75 - 50);
        }
        if (distance > 75 && distance < 100) {
            return 2200 + (distance - 75) * (2500 - 2200) / (100 - 75);
        }
        return 1600;
    }
}
