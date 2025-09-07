// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Subsystems.Drivetrain.DrivetrainSubsystem;

public class Robot extends LoggedRobot {

  CommandXboxController controller = new CommandXboxController(0);

  DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();


  public Robot() {
    Logger.recordMetadata("ProjectName", "KitbotExample"); // Set a metadata value

    if (isReal()) {
      Logger.addDataReceiver(new WPILOGWriter("/media/sda1/")); // Log to a USB stick
      Logger.addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
      new PowerDistribution(1, ModuleType.kRev); // Enables power distribution logging
    } else {
      Logger.addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
    }

    Logger.start(); // Start logging! No more data receivers, replay sources, or metadata values may be added.
    
    drivetrainSubsystem.setDefaultCommand(
      drivetrainSubsystem.setVoltagesArcadeCommand(
        () -> modifyJoystick(controller.getLeftY()),
        () -> modifyJoystick(controller.getRightX())));
  }

  private double modifyJoystick(double in) {
    if (Math.abs(in) < 0.05) {
      return 0;
    }
    return in * in * Math.signum(in);
  }

  @Override
  public void robotInit() {
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
