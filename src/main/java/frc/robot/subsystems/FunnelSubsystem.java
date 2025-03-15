// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import javax.sound.sampled.Clip;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.FunnelEncoderConstants;
import frc.robot.Constants.MotorConstants;

public class FunnelSubsystem extends SubsystemBase {
  private final SparkMax leftMotor;
  private final SparkMax rightMotor;
  private final SparkMaxConfig configLeft;
  private final SparkMaxConfig configRight;
  private final RelativeEncoder leftEncoder;
  private final RelativeEncoder rightEncoder;
  private final SparkClosedLoopController leftPID;
  private final SparkClosedLoopController rightPID;
  private FunnelState intendedState;
  private FunnelState currentState;

  public FunnelSubsystem() {
    leftMotor = new SparkMax(MotorConstants.FUNNELLEFT, MotorType.kBrushless);
    rightMotor = new SparkMax(MotorConstants.FUNNELRIGHT, MotorType.kBrushless);
    configLeft = new SparkMaxConfig();
    configRight = new SparkMaxConfig();
    leftEncoder = leftMotor.getEncoder();
    rightEncoder = rightMotor.getEncoder();
    leftPID = leftMotor.getClosedLoopController();
    rightPID = rightMotor.getClosedLoopController();
    intendedState = FunnelState.NEUTRAL;
    currentState = FunnelState.NEUTRAL;
    
     configLeft
      .idleMode(IdleMode.kBrake);
    // configures the encoders to brake when not moving
    configLeft.closedLoop
      .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
      .pid(.1, 0, 0)
      .outputRange(-1, 1);
    // configures PID controllers
    configLeft.closedLoop.maxMotion
      .maxVelocity(2.5)
      .maxAcceleration(1);
    //sets max velocity and acceleration for the Left motor
    leftMotor.configure(configLeft, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    // Sets defaults for the elevator motor (don't touch these)

    configRight
      .idleMode(IdleMode.kBrake);
    // configures the encoders to brake when not moving
    configRight.closedLoop
      .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
      .pid(.1, 0, 0)
      .outputRange(-1, 1);
    // configures PID controllers
    configRight.closedLoop.maxMotion
      .maxVelocity(2.5)
      .maxAcceleration(1);
    //sets max velocity and acceleration for the Right motor
    rightMotor.configure(configRight, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    // Sets defaults for the elevator motor (don't touch these)
  }

  public void alignPosition(){
    intendedState = FunnelState.ALIGN;
  }

  public void neutralPosition(){
    intendedState = FunnelState.NEUTRAL;
  }

  @Override
  public void periodic() {
    System.out.println(leftEncoder.getPosition());
    System.out.println(rightEncoder.getPosition());
    /**
    if(currentState != intendedState){
      switch (intendedState) {
        case ALIGN:
          if(withinLeftBounds(FunnelEncoderConstants.LEFT_ALIGN) && withinRightBounds(FunnelEncoderConstants.RIGHT_ALIGN)){
            currentState = FunnelState.ALIGN;
          } else {
            currentState = FunnelState.MOVING;
            if (!withinLeftBounds(FunnelEncoderConstants.LEFT_ALIGN)) {
              leftMoveTo(FunnelEncoderConstants.LEFT_ALIGN);
            }
            if (!withinRightBounds(FunnelEncoderConstants.RIGHT_ALIGN)) {
              rightMoveTo(FunnelEncoderConstants.RIGHT_ALIGN);
            }
          }
        break;
        case NEUTRAL:
          if(withinLeftBounds(FunnelEncoderConstants.LEFT_NEUTRAL) && withinRightBounds(FunnelEncoderConstants.RIGHT_NEUTRAL)){
            currentState = FunnelState.NEUTRAL;
          } else {
            currentState = FunnelState.MOVING;
            if (!withinLeftBounds(FunnelEncoderConstants.LEFT_NEUTRAL)) {
              leftMoveTo(FunnelEncoderConstants.LEFT_NEUTRAL);
            }
            if (!withinRightBounds(FunnelEncoderConstants.RIGHT_NEUTRAL)) {
              rightMoveTo(FunnelEncoderConstants.RIGHT_NEUTRAL);
            }
          }
        break;
        case CLIMB:
          if(withinLeftBounds(FunnelEncoderConstants.LEFT_CLIMB) && withinRightBounds(FunnelEncoderConstants.RIGHT_CLIMB)){
            currentState = FunnelState.CLIMB;
          } else {
            currentState = FunnelState.MOVING;
            if (!withinLeftBounds(FunnelEncoderConstants.LEFT_CLIMB)) {
              leftMoveTo(FunnelEncoderConstants.LEFT_CLIMB);
            }
            if (!withinRightBounds(FunnelEncoderConstants.RIGHT_CLIMB)) {
              rightMoveTo(FunnelEncoderConstants.RIGHT_CLIMB);
            }
          }
        break;
        case POSTCLIMB:
          if(withinLeftBounds(FunnelEncoderConstants.LEFT_POSTCLIMB) && withinRightBounds(FunnelEncoderConstants.RIGHT_POSTCLIMB)){
            currentState = FunnelState.POSTCLIMB;
          } else {
            currentState = FunnelState.MOVING;
            if (!withinLeftBounds(FunnelEncoderConstants.LEFT_POSTCLIMB)) {
              leftMoveTo(FunnelEncoderConstants.LEFT_POSTCLIMB);
            }
            if (!withinRightBounds(FunnelEncoderConstants.RIGHT_POSTCLIMB)) {
              rightMoveTo(FunnelEncoderConstants.RIGHT_POSTCLIMB);
            }
          }
        break;
      }
    }
    */
  }

  private boolean withinLeftBounds(double setpoint){
    return leftEncoder.getPosition() >= (setpoint - .25) && leftEncoder.getPosition() <= (setpoint + .25);
  }

  private boolean withinRightBounds(double setpoint){
    return rightEncoder.getPosition() >= (setpoint - .25) && rightEncoder.getPosition() <= (setpoint + .25);
  }

  private void leftMoveTo(double setpoint){
    leftPID.setReference(setpoint, ControlType.kPosition);
  }

  private void rightMoveTo(double setpoint){
    rightPID.setReference(setpoint, ControlType.kPosition);
  }


  public enum FunnelState{
    MOVING,
    NEUTRAL,
    ALIGN,
    CLIMB,
    POSTCLIMB
  }
}
