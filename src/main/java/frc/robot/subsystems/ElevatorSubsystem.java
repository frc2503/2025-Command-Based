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
  // configures the encoders to brake when not moving
    config.closedLoop
      .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
      .pid(0, 0, 0)
      .outputRange(-1, 1);
  // configures PID controllers
    config.closedLoop.maxMotion
      .maxVelocity(2.5)
      .maxAcceleration(1);
  //sets max velocity and acceleration for the elevator motor
    config.encoder
      .positionConversionFactor(getConversionFactor(48, 2.074));
  // Sets conversion factor for the motor using the gearbox's gear ratio and the pitch of the elevator sprocket
    elevator.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
   // Sets defaults for the elevator motor (don't touch these)

      encoder = elevator.getEncoder();
   }

  public void goToStageOne(){
    intendedState = ElevatorState.STAGE_ONE;
    pidController.setReference(0, ControlType.kPosition);
  }
  // sets the intended state to stage 1 and starts movement to stage 1

  public void goToStageTwo(){
    intendedState = ElevatorState.STAGE_TWO;
    pidController.setReference(12, ControlType.kPosition);
  }
  // sets the intended state to stage 2 and starts movement to stage 2

  public void goToStageThree(){
    intendedState = ElevatorState.STAGE_THREE;
    pidController.setReference(24, ControlType.kPosition);
  }
  // sets the intended state to stage 3 and starts movement to stage 3
  

  public void testElevatorMotorUp() {
    elevator.set(-0.50);
  }

  public void testElevatorMotorDown() {
    elevator.set(0.30);
  }

  public void stopMotor() {
    elevator.set(0.0);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(currentState != intendedState){
      if(intendedState == ElevatorState.STAGE_ONE && encoder.getPosition() <= 1.5){
        currentState = ElevatorState.STAGE_ONE;
        //Sets the current position to stage 1 when it's at stage 1
      } else if (intendedState == ElevatorState.STAGE_TWO && withinBounds(12)){
        currentState = ElevatorState.STAGE_TWO;
        //Sets the current position to stage 2 when it's at stage 2
      }else if (intendedState == ElevatorState.STAGE_THREE && withinBounds(24)){
        currentState = ElevatorState.STAGE_THREE;
        //Sets the current position to stage 3 when it's at stage 3
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

//This whole function at the bottom sets the conversion factor so it sets the right height
  public double getConversionFactor(
    double gearRatio,
    double pitchDiameter
    ) {
      double circumference = pitchDiameter * Math.PI;
      return circumference / gearRatio;
  }
}