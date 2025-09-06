// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Drivetrain;

import org.littletonrobotics.junction.AutoLog;

public interface DrivetrainIO {
    @AutoLog
    public static class DrivetrainIOInputs {
        public double leftOutputVolts = 0.0;
        public double rightOutputVolts = 0.0;

        public double leftVelocityMetersPerSecond = 0.0;
        public double rightVelocityMetersPerSecond = 0.0;

        public double leftPositionMeters = 0.0;
        public double rightPositionMeters = 0.0;

        public double leftCurrentAmps = 0.0;
        public double leftTempCelsius = 0.0;
        public double rightCurrentAmps = 0.0;
        public double rightTempCelsius = 0.0;
    }

    public void updateInputs(DrivetrainIOInputs inputs);

    public void setVolts(double left, double right);
}
