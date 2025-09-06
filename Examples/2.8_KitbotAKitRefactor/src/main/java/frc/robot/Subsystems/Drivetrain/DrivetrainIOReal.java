// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Drivetrain;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;

/** Add your docs here. */
public class DrivetrainIOReal implements DrivetrainIO {

    TalonFX leftTalon = new TalonFX(DrivetrainSubsystem.LEFT_TALON_ID);
    TalonFX rightTalon = new TalonFX(DrivetrainSubsystem.RIGHT_TALON_ID);

    VoltageOut leftVoltage = new VoltageOut(0);
    VoltageOut rightVoltage = new VoltageOut(0);

    private final StatusSignal<Voltage> leftAppliedVoltage = leftTalon.getMotorVoltage();
    private final StatusSignal<Voltage> rightAppliedVoltage = rightTalon.getMotorVoltage();
    private final StatusSignal<AngularVelocity> leftAngularVelocityRPS = leftTalon.getVelocity();
    private final StatusSignal<AngularVelocity> rightAngularVelocityRPS = rightTalon.getVelocity();

    // A little hacky - the units don't match, but that would typically be handled in the 
    // SensorToMechanismRatio config. For the purposes of this lesson, YOU DO NOT NEED TO
    // WORRY ABOUT THIS, but ask a lead if you have questions!
    private final StatusSignal<Angle> leftPositionMeters = leftTalon.getPosition(); 
    private final StatusSignal<Angle> rightPositionMeters = rightTalon.getPosition(); 

    private final StatusSignal<Current> leftSupplyCurrent = leftTalon.getSupplyCurrent();
    private final StatusSignal<Current> rightSupplyCurrent = rightTalon.getSupplyCurrent();
    private final StatusSignal<Temperature> leftTempCelsius = leftTalon.getDeviceTemp();
    private final StatusSignal<Temperature> rightTempCelsius = rightTalon.getDeviceTemp();

    public DrivetrainIOReal() {
        // Sets the following status signals to be updated at a frequency of 50hz
        BaseStatusSignal.setUpdateFrequencyForAll(
            50.0, // update every 20ms
            leftAppliedVoltage, 
            rightAppliedVoltage, 
            leftAngularVelocityRPS, 
            rightAngularVelocityRPS, 
            leftPositionMeters, 
            rightPositionMeters, 
            leftSupplyCurrent, 
            rightSupplyCurrent, 
            leftTempCelsius, 
            rightTempCelsius);
    }

    @Override
    public void updateInputs(DrivetrainIOInputs inputs) {

        BaseStatusSignal.refreshAll(leftAppliedVoltage, 
            rightAppliedVoltage, 
            leftAngularVelocityRPS, 
            rightAngularVelocityRPS, 
            leftPositionMeters, 
            rightPositionMeters, 
            leftSupplyCurrent, 
            rightSupplyCurrent, 
            leftTempCelsius, 
            rightTempCelsius);

            inputs.leftOutputVolts = leftAppliedVoltage.getValueAsDouble();
            inputs.rightOutputVolts = rightAppliedVoltage.getValueAsDouble();
        
            inputs.leftVelocityMetersPerSecond = leftAngularVelocityRPS.getValueAsDouble();
            inputs.rightVelocityMetersPerSecond = rightAngularVelocityRPS.getValueAsDouble();
        
            inputs.leftPositionMeters = leftPositionMeters.getValueAsDouble();
            inputs.rightPositionMeters = rightPositionMeters.getValueAsDouble();
        
            inputs.leftCurrentAmps = leftSupplyCurrent.getValueAsDouble();
            inputs.leftTempCelsius = rightSupplyCurrent.getValueAsDouble();
            inputs.rightCurrentAmps = rightSupplyCurrent.getValueAsDouble();
            inputs.rightTempCelsius = rightTempCelsius.getValueAsDouble();
    }

    @Override
    public void setVolts(double left, double right) {
        leftTalon.setControl(leftVoltage.withOutput(left));
        rightTalon.setControl(rightVoltage.withOutput(left));
    }
    
}
