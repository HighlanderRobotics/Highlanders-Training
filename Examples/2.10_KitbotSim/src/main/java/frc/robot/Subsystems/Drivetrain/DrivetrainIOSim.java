// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Drivetrain;

import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotGearing;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotMotor;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotWheelSize;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;

public class DrivetrainIOSim implements DrivetrainIO {
    // Here we have 2 motors, one for each side
    // Since we have 2 motors per side we would need to add "follower" motors
    // Since this is a sim we can pretend those motors don't exist
    TalonFX leftTalon = new TalonFX(DrivetrainSubsystem.LEFT_TALON_ID);
    TalonFX rightTalon = new TalonFX(DrivetrainSubsystem.RIGHT_TALON_ID);

    // ControlRequest objects represent what we want our motor to do
    // There might be a cleaner way to have both voltage and velocity control than making two sets of objects
    // This is part of the Phoenix v6/pro api, old code uses the v5 api which looks different
    VoltageOut leftVoltage = new VoltageOut(0);
    VoltageOut rightVoltage = new VoltageOut(0);

    // This is a physics sim object
    // This will calculate the movement of the drivetrain based off of a mathmatical model
    // To learn more about how these models work, look at the book linked in the state space course
    DifferentialDrivetrainSim physicsSim = DifferentialDrivetrainSim.createKitbotSim(
        KitbotMotor.kDoubleFalcon500PerSide, 
        // This is the default gearing for the kitbot
        // If this was a real robot, we would check with mechanical for actual numbers
        // Note that the default gearbox is not compatible with falcons so this configuration is not realistic
        KitbotGearing.k8p45, 
        // Default wheels
        KitbotWheelSize.kSixInch, 
        // This is a way for us to model noise from our measurements
        // We can leave it null to pretend our simulation is perfect for this exercise
        null);

    @Override
    public void updateInputs(DrivetrainIOInputs inputs) {
        // Start by updating our physics model with the default loop time of 20 ms
        physicsSim.update(0.020);

        // Update the voltage available to each motor based off of a simulated robot battery
        // This accounts for "voltage sag" when motors are running
        var leftSimState = leftTalon.getSimState();
        leftSimState.setSupplyVoltage(RoboRioSim.getVInVoltage());

        var rightSimState = rightTalon.getSimState();
        rightSimState.setSupplyVoltage(RoboRioSim.getVInVoltage());

        // Use the motor output voltage to update the sim
        physicsSim.setInputs(leftSimState.getMotorVoltage(), rightSimState.getMotorVoltage());

        inputs.leftOutputVolts = leftSimState.getMotorVoltage();
        inputs.rightOutputVolts = rightSimState.getMotorVoltage();

        inputs.leftVelocityMetersPerSecond = physicsSim.getLeftVelocityMetersPerSecond();
        inputs.rightVelocityMetersPerSecond = physicsSim.getRightVelocityMetersPerSecond();

        inputs.leftPositionMeters = physicsSim.getLeftPositionMeters();
        inputs.rightPositionMeters = physicsSim.getRightPositionMeters();

        inputs.leftCurrentAmps = leftSimState.getTorqueCurrent();
        inputs.leftTempCelsius = 0.0;
        inputs.rightCurrentAmps = rightSimState.getTorqueCurrent();
        inputs.rightTempCelsius = 0.0;
    }

    @Override
    public void setVolts(double left, double right) {
        leftTalon.setControl(leftVoltage.withOutput(left));
        rightTalon.setControl(rightVoltage.withOutput(right));
    }
}
