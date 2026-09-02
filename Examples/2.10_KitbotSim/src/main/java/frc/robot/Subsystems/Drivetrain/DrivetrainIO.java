// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Drivetrain;

import org.littletonrobotics.junction.AutoLog;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;

/** Add your docs here. */
public class DrivetrainIO {
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

    protected TalonFX leftTalon;
    protected TalonFX rightTalon;

    VoltageOut leftVoltage = new VoltageOut(0);
    VoltageOut rightVoltage = new VoltageOut(0);

    private final StatusSignal<Voltage> leftAppliedVoltage;
    private final StatusSignal<Voltage> rightAppliedVoltage;
    private final StatusSignal<AngularVelocity> leftAngularVelocityRPS;
    private final StatusSignal<AngularVelocity> rightAngularVelocityRPS;

    // A little hacky - the units don't match, but that would typically be handled in the 
    // SensorToMechanismRatio config. For the purposes of this lesson, YOU DO NOT NEED TO
    // WORRY ABOUT THIS, but ask a lead if you have questions!
    private final StatusSignal<Angle> leftPositionMeters; 
    private final StatusSignal<Angle> rightPositionMeters; 

    private final StatusSignal<Current> leftSupplyCurrent;
    private final StatusSignal<Current> rightSupplyCurrent;
    private final StatusSignal<Temperature> leftTempCelsius;
    private final StatusSignal<Temperature> rightTempCelsius;

    public DrivetrainIO(CANBus canbus) {
        leftTalon = new TalonFX(DrivetrainSubsystem.LEFT_TALON_ID, canbus);
        rightTalon = new TalonFX(DrivetrainSubsystem.RIGHT_TALON_ID, canbus);

        leftAppliedVoltage = leftTalon.getMotorVoltage();
        rightAppliedVoltage = rightTalon.getMotorVoltage();

        leftAngularVelocityRPS = leftTalon.getVelocity();
        rightAngularVelocityRPS = rightTalon.getVelocity();
        
        rightPositionMeters = rightTalon.getPosition(); 
        leftPositionMeters = leftTalon.getPosition(); 

        rightSupplyCurrent = rightTalon.getSupplyCurrent();
        leftSupplyCurrent = leftTalon.getSupplyCurrent();
        leftTempCelsius = leftTalon.getDeviceTemp();
        rightTempCelsius = rightTalon.getDeviceTemp();

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
        leftTalon.optimizeBusUtilization();
        rightTalon.optimizeBusUtilization();
    }

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

    public void setVolts(double left, double right) {
        leftTalon.setControl(leftVoltage.withOutput(left));
        rightTalon.setControl(rightVoltage.withOutput(left));
    }
    
}
