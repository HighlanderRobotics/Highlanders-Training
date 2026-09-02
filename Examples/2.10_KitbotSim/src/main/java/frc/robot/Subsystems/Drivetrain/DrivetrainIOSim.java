// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Drivetrain;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.sim.TalonFXSimState;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotGearing;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotMotor;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim.KitbotWheelSize;

public class DrivetrainIOSim extends DrivetrainIO {
    
    private DifferentialDrivetrainSim physicsSim = DifferentialDrivetrainSim.createKitbotSim(
            KitbotMotor.kSingleFalcon500PerSide,
            KitbotGearing.k8p45,
            KitbotWheelSize.kSixInch,
            null
    );

    private TalonFXSimState leftSimState;
    private TalonFXSimState rightSimState;

    private double lastLoopTime = 0.0;
    private Notifier notifier;

    public DrivetrainIOSim(CANBus canBus) {
        super(canBus);

        leftSimState = leftTalon.getSimState();
        rightSimState = rightTalon.getSimState();   
        
        notifier = new Notifier(() -> {
            double currentTime = Timer.getTimestamp();
            double deltaTime = currentTime - lastLoopTime;
            lastLoopTime = currentTime;

            leftSimState.setSupplyVoltage(RobotController.getBatteryVoltage());
            rightSimState.setSupplyVoltage(RobotController.getBatteryVoltage());

            physicsSim.setInputs(leftSimState.getMotorVoltage(), rightSimState.getMotorVoltage());
            physicsSim.update(deltaTime);

            leftSimState.setRawRotorPosition(physicsSim.getLeftPositionMeters());
            leftSimState.setRotorVelocity(physicsSim.getLeftVelocityMetersPerSecond());

            rightSimState.setRawRotorPosition(physicsSim.getRightPositionMeters());
            rightSimState.setRotorVelocity(physicsSim.getRightVelocityMetersPerSecond());
        });
        notifier.startPeriodic(0.002);
    }
}
