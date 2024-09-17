// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Drivetrain;

import com.ctre.phoenixpro.controls.VelocityDutyCycle;
import com.ctre.phoenixpro.controls.VoltageOut;
import com.ctre.phoenixpro.hardware.TalonFX;

import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotGearing;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotMotor;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotWheelSize;

/** Sim implementation of Kitbot drivetrain. */
public class DrivetrainIOSim implements DrivetrainIO {
    // Here we have 2 motors, one for each side
    // Since we have 2 motors per side we would need to add "follower" motors
    // Since this is a sim we can pretend those motors don't exist
    TalonFX leftFalcon = new TalonFX(0);
    TalonFX rightFalcon = new TalonFX(1);

    // ControlRequest objects represent what we want our motor to do
    // There might be a cleaner way to have both voltage and velocity control than making two sets of objects
    // This is part of the Phoenix v6/pro api, old code uses the v5 api which looks different
    VoltageOut leftVoltageControl = new VoltageOut(0);
    VoltageOut rightVoltageControl = new VoltageOut(0);

    VelocityDutyCycle leftVelocityControl = new VelocityDutyCycle(0);
    VelocityDutyCycle rightVelocityControl = new VelocityDutyCycle(0);

    // Track if we're using voltage (open loop) or velocity (closed loop)
    boolean isClosedLoop = false;

    // Physical constants for converting from motor speed to chassis speed
    double gearRatio = DifferentialDrivetrainSim.KitbotGearing.k8p45.value;
    double wheelDiameter = DifferentialDrivetrainSim.KitbotWheelSize.kSixInch.value;

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
        var leftSimState = leftFalcon.getSimState();
        leftSimState.setSupplyVoltage(RoboRioSim.getVInVoltage());

        var rightSimState = rightFalcon.getSimState();
        rightSimState.setSupplyVoltage(RoboRioSim.getVInVoltage());

        // Use the motor output voltage to update the sim
        physicsSim.setInputs(leftSimState.getMotorVoltage(), rightSimState.getMotorVoltage());

        inputs.leftSpeedMetersPerSecond = physicsSim.getLeftVelocityMetersPerSecond();
        inputs.leftPositionMeters = physicsSim.getLeftPositionMeters();
        inputs.rightSpeedMetersPerSecond = physicsSim.getRightVelocityMetersPerSecond();
        inputs.rightPositionMeters = physicsSim.getRightPositionMeters();

        inputs.leftOutputVolts = leftSimState.getMotorVoltage();
        inputs.leftGoalMetersPerSecond = (leftVelocityControl.Velocity * Math.PI * wheelDiameter) / gearRatio;
        inputs.rightOutputVolts = rightSimState.getMotorVoltage();
        inputs.rightGoalMetersPerSecond = (rightVelocityControl.Velocity * Math.PI * wheelDiameter) / gearRatio;
        inputs.isClosedLoop = isClosedLoop;

        // If we had multiple motors per side, we would add all of them to the array
        inputs.leftCurrentAmps = new double[] {leftSimState.getTorqueCurrent()}; 
        inputs.leftTempCelsius = new double[] {0.0}; // For sim, we're going to ignore temparature
        // If we had multiple motors per side, we would add all of them to the array
        inputs.rightCurrentAmps = new double[] {rightSimState.getTorqueCurrent()}; 
        inputs.rightTempCelsius = new double[] {0.0}; // For sim, we're going to ignore temparature
    }

    @Override
    public void setVolts(double left, double right) {
        isClosedLoop = false;
        leftFalcon.setControl(leftVoltageControl.withOutput(left));
        rightFalcon.setControl(rightVoltageControl.withOutput(right));
    }

    @Override
    public void setMetersPerSecond(double left, double right) {
        isClosedLoop = true;
        leftFalcon.setControl(leftVelocityControl.withVelocity((left * gearRatio) / (Math.PI * wheelDiameter)));
        rightFalcon.setControl(rightVelocityControl.withVelocity((right * gearRatio) / (Math.PI * wheelDiameter)));
    }

}
