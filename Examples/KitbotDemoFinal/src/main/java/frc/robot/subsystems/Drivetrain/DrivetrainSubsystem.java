// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Drivetrain;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DrivetrainSubsystem extends SubsystemBase {
  // Odometry keeps track of our position on the field
  // We can upgrade this to a pose estimator, which fuses vision readings to
  // correct our estimate
  DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d(), 0, 0);

  // We could make this select an io type based off of if the robot was real or
  // not
  // Robot.isReal() ? new DrivetrainIOReal() : new DrivetrainIOSim()
  DrivetrainIO io = new DrivetrainIOSim();
  DrivetrainIOInputsAutoLogged inputs = new DrivetrainIOInputsAutoLogged();

  /** Creates a new Drivetrain. */
  public DrivetrainSubsystem() {
  }

  // Method to set the driving speed and turning rate of the robot
  public void drive(double speed, double angle, boolean isClosedLoop) {
    var wheelSpeeds = DifferentialDrive.arcadeDriveIK(speed, angle, false);
    if (isClosedLoop) {
      io.setMetersPerSecond(wheelSpeeds.left, wheelSpeeds.right);
    } else {
      io.setVolts(wheelSpeeds.left * 12, wheelSpeeds.right * 12);
    }
  }

  // Command that wraps drive method
  public CommandBase driveCommand(DoubleSupplier speed, DoubleSupplier angle, BooleanSupplier isClosedLoop) {
    return this.run(drive(speed.getAsDouble(), angle.getAsDouble(), isClosedLoop.getAsBoolean()));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    io.updateInputs(inputs);
    Logger.getInstance().processInputs("Drivetrain", inputs);

    odometry.update(
      // Here we have to do a little hack to get rotation to work in sim
      // Normally we use a gyro to find rotation, but we don't have a simulated gyro set up now
        odometry.getPoseMeters().getRotation()
            // Use differential drive kinematics to find the rotation rate based on the wheel speeds and distance between wheels
            .plus(Rotation2d.fromRadians((inputs.leftSpeedMetersPerSecond - inputs.rightSpeedMetersPerSecond) * 0.020 / Units.inchesToMeters(26))),
        inputs.leftPositionMeters, inputs.rightPositionMeters);
    Logger.getInstance().recordOutput("Drivebase Pose", odometry.getPoseMeters());
  }
}
