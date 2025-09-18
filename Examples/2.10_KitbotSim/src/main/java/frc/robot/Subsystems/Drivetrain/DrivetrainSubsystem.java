// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Drivetrain;

import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class DrivetrainSubsystem extends SubsystemBase {
  public static final int LEFT_TALON_ID = 0;
  public static final int RIGHT_TALON_ID = 1;

  DrivetrainIO io = Robot.isReal() ? new DrivetrainIOReal() : new DrivetrainIOSim();
  DrivetrainIOInputsAutoLogged inputs = new DrivetrainIOInputsAutoLogged();

  // Odometry keeps track of our position on the field
  // We can upgrade this to a pose estimator, which fuses vision readings to
  // correct our estimate
  DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d(), 0, 0);

  /** Creates a new Drivetrain. */
  public DrivetrainSubsystem() {
  }

  private void setVoltages(double left, double right) {
    io.setVolts(left, right);
  }

  public Command setVoltagesCommand(DoubleSupplier left, DoubleSupplier right) {
    return this.run(() -> this.setVoltages(left.getAsDouble(), right.getAsDouble()));
  }

  public Command setVoltagesArcadeCommand(DoubleSupplier drive, DoubleSupplier steer) {
    return this.run(() -> {
      var speeds = DifferentialDrive.arcadeDriveIK(drive.getAsDouble(), steer.getAsDouble(), false);
      this.setVoltages(speeds.left * 12, speeds.right * 12);
    });
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Drivetrain", inputs);

    odometry.update(
        odometry.getPoseMeters().getRotation()
            // Use differential drive kinematics to find the rotation rate based on the
            // wheel speeds and distance between wheels
            .plus(Rotation2d.fromRadians((inputs.leftVelocityMetersPerSecond - inputs.rightVelocityMetersPerSecond)
                * 0.020 / Units.inchesToMeters(26))),
        inputs.leftPositionMeters, inputs.rightPositionMeters);
    Logger.recordOutput("Drivetrain Pose", odometry.getPoseMeters());
  }
}
