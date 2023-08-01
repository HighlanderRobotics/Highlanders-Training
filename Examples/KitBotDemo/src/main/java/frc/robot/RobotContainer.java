// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Drivetrain.DrivetrainSubsystem;

public class RobotContainer {
  DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();

  CommandXboxController controller = new CommandXboxController(0);

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    // Default Command
    drivetrainSubsystem
        .setDefaultCommand(drivetrainSubsystem.driveCommand(
            () -> modifyJoystickAxis(controller.getLeftY()),
            () -> modifyJoystickAxis(controller.getLeftX()), 
            () -> false));
  }

  private double modifyJoystickAxis(double joystick) {
    return -(Math.abs(Math.pow(joystick, 2)) + 0.05)
        * Math.signum(joystick);
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
