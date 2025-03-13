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
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
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
    intendedState = FunnelState.OPEN;
    currentState = FunnelState.OPEN;

    configLeft
        .idleMode(IdleMode.kBrake);
    // configures the encoders to brake when not moving
    configLeft.closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .pid(0, 0, 0)
        .outputRange(-1, 1);
    // configures PID controllers
    configLeft.closedLoop.maxMotion
        .maxVelocity(2.5)
        .maxAcceleration(1);
    // sets max velocity and acceleration for the Left motor
    leftMotor.configure(configLeft, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    // Sets defaults for the elevator motor (don't touch these)

    configRight
        .idleMode(IdleMode.kBrake);
    // configures the encoders to brake when not moving
    configRight.closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .pid(0, 0, 0)
        .outputRange(-1, 1);
    // configures PID controllers
    configRight.closedLoop.maxMotion
        .maxVelocity(2.5)
        .maxAcceleration(1);
    // sets max velocity and acceleration for the Right motor
    rightMotor.configure(configRight, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    // Sets defaults for the elevator motor (don't touch these)

    // Set encoders to 0 to start
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  public void open() {
    intendedState = FunnelState.OPEN;

    // TODO Update these reference values to correct positions relative to starting position (0)
    leftPID.setReference(0, ControlType.kPosition);
    rightPID.setReference(0, ControlType.kPosition);
  }

  public void climbing() {
    intendedState = FunnelState.CLIMBING;

    // TODO Update these reference values to correct positions relative to starting position (0)
    leftPID.setReference(0, ControlType.kPosition);
    rightPID.setReference(0, ControlType.kPosition);
  }

  public void semiClosed() {
    intendedState = FunnelState.SEMI_CLOSED;

    // TODO Update these reference values to correct positions relative to starting position (0)
    leftPID.setReference(0, ControlType.kPosition);
    rightPID.setReference(0, ControlType.kPosition);
  }

  public void closed() {
    intendedState = FunnelState.CLOSED;

    // TODO Update these reference values to correct positions relative to starting position (0)
    leftPID.setReference(0, ControlType.kPosition);
    rightPID.setReference(0, ControlType.kPosition);
  }

  @Override
  public void periodic() {
    if (currentState != intendedState) {
      if (intendedState == FunnelState.OPEN) {
        if (withinLeftBounds(0) && withinRightBounds(0)) {
          currentState = FunnelState.OPEN;
        }
      }
      if (intendedState == FunnelState.CLIMBING) {
        if (withinLeftBounds(1) && withinRightBounds(-1)) {
          currentState = FunnelState.CLIMBING;
        }
      }
      if (intendedState == FunnelState.SEMI_CLOSED) {
        if (withinLeftBounds(2) && withinRightBounds(-2)) {
          currentState = FunnelState.SEMI_CLOSED;
        }
      }
      if (intendedState == FunnelState.CLOSED) {
        if (withinLeftBounds(3) && withinRightBounds(-3)) {
          currentState = FunnelState.CLOSED;
          // Since we don't want this to stay closed, but instead open right back up
          // to SEMI_CLOSED, let's set that as the intended state right away
          intendedState = FunnelState.SEMI_CLOSED;
        }
      }
    }
  }

  private boolean withinLeftBounds(double setpoint) {
    return leftEncoder.getPosition() >= (setpoint - .25) && leftEncoder.getPosition() <= (setpoint + .25);
  }

  private boolean withinRightBounds(double setpoint) {
    return rightEncoder.getPosition() >= (setpoint - .25) && rightEncoder.getPosition() <= (setpoint + .25);
  }

  public enum FunnelState {
    OPEN,
    CLIMBING,
    SEMI_CLOSED,
    CLOSED
  }
}
