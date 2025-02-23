// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class ElevatorSubsystem extends SubsystemBase {
  private final SparkMax elevator;
  private final SparkMaxConfig config;
  private final SparkClosedLoopController pidController;
  private final RelativeEncoder encoder;
  private ElevatorState currentState;
  private ElevatorState intendedState;
  /** Creates a new ElevatorSubsystem. */

  public ElevatorSubsystem() {
    elevator = new SparkMax(MotorConstants.ELEVATOR, MotorType.kBrushless);
    config = new SparkMaxConfig();
    pidController = elevator.getClosedLoopController();
    currentState = ElevatorState.STAGE_ONE;
    intendedState = ElevatorState.STAGE_ONE;

  
    config
      .idleMode(IdleMode.kBrake);
    config.closedLoop
      .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
      .pid(0, 0, 0)
      .outputRange(-1, 1);
    config.closedLoop.maxMotion
      .maxVelocity(2.5)
      .maxAcceleration(1);
    config.encoder
      .positionConversionFactor(getConversionFactor(48, 2.074));

      elevator.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

      encoder = elevator.getEncoder();
   }

  public void goToStageOne(){
    intendedState = ElevatorState.STAGE_ONE;
    pidController.setReference(0, ControlType.kPosition);
  }

  public void goToStageTwo(){
    intendedState = ElevatorState.STAGE_TWO;
    pidController.setReference(12, ControlType.kPosition);
  }

  public void goToStageThree(){
    intendedState = ElevatorState.STAGE_THREE;
    pidController.setReference(24, ControlType.kPosition);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(currentState != intendedState){
      if(intendedState == ElevatorState.STAGE_ONE && encoder.getPosition() <= 0){
        currentState = ElevatorState.STAGE_ONE;
      } else if (intendedState == ElevatorState.STAGE_TWO && withinBounds(12)){
        currentState = ElevatorState.STAGE_TWO;
      }else if (intendedState == ElevatorState.STAGE_THREE && withinBounds(24)){
        currentState = ElevatorState.STAGE_THREE;
        } else{
          currentState = ElevatorState.MOVING;
        }
    }
  }

  public enum ElevatorState{
    STAGE_ONE,
    STAGE_TWO,
    STAGE_THREE,
    MOVING
  }
  private boolean withinBounds(double setpoint){
    return encoder.getPosition() >= (setpoint - .25) && encoder.getPosition() <= (setpoint + .25);
  }

  public double getConversionFactor(
    double gearRatio,
    double pitchDiameter
    ) {
      double circumference = pitchDiameter * Math.PI;
      return circumference / gearRatio;
  }
}