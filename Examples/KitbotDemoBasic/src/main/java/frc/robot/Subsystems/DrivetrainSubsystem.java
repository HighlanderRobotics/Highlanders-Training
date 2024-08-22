// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenixpro.controls.VoltageOut;
import com.ctre.phoenixpro.hardware.TalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DrivetrainSubsystem extends SubsystemBase {
  TalonFX leftFalcon = new TalonFX(Constants.drivetrainLeftFalconID);
  TalonFX rightFalcon = new TalonFX(Constants.drivetrainRightFalconID);

  VoltageOut leftVoltage = new VoltageOut(0);
  VoltageOut rightVoltage = new VoltageOut(0);

  /** Creates a new Drivetrain. */
  public DrivetrainSubsystem() {
  }

  private void setVoltages(double left, double right) {
    leftFalcon.setControl(leftVoltage.withOutput(left));
    rightFalcon.setControl(rightVoltage.withOutput(left));
  }

  public CommandBase setVoltagesCommand(DoubleSupplier left, DoubleSupplier right) {
    return this.run(this.setVoltages(left.getAsDouble(), right.getAsDouble()));
  }

  public CommandBase setVoltagesArcadeCommand(DoubleSupplier drive, DoubleSupplier steer) {
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
