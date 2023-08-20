// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Drivetrain;

import org.littletonrobotics.junction.AutoLog;

/** Add your docs here. */
public interface DrivetrainIO {
    @AutoLog
    public static class DrivetrainIOInputs {
        // Speed and position for odometry
        public double leftSpeedMetersPerSecond = 0.0;
        public double leftPositionMeters = 0.0;
        public double rightSpeedMetersPerSecond = 0.0;
        public double rightPositionMeters = 0.0;
        // Output logging, volts for open loop, rpm for closed loop
        public double leftOutputVolts = 0.0;
        public double leftGoalMetersPerSecond = 0.0;
        public double rightOutputVolts = 0.0;
        public double rightGoalMetersPerSecond = 0.0;
        public boolean isClosedLoop = false;
        // Current and temparature logging to monitor motor performance
        public double[] leftCurrentAmps = new double[0];
        public double[] leftTempCelsius = new double[0];
        public double[] rightCurrentAmps = new double[0];
        public double[] rightTempCelsius = new double[0];
    }

    public abstract void updateInputs(DrivetrainIOInputs inputs);

    public abstract void setVolts(double left, double right);

    public abstract void setMetersPerSecond(double left, double right);
}
