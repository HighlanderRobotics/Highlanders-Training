// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DrivetrainSubsystem extends SubsystemBase {
  public static final int LEFT_TALON_ID = 0;
  public static final int RIGHT_TALON_ID = 1;

  TalonFX leftTalon = new TalonFX(LEFT_TALON_ID);
  TalonFX rightTalon = new TalonFX(RIGHT_TALON_ID);

  VoltageOut leftVoltage = new VoltageOut(0);
  VoltageOut rightVoltage = new VoltageOut(0);

  /** Creates a new Drivetrain. */
  public DrivetrainSubsystem() {
  }

  private void setVoltages(double left, double right) {
      leftTalon.setControl(leftVoltage.withOutput(left));
      rightTalon.setControl(rightVoltage.withOutput(left));
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
    // This method will be called once per scheduler run
  }
}
