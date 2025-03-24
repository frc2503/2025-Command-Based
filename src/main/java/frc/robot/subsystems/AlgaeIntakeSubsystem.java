// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class AlgaeIntakeSubsystem extends SubsystemBase {
private final SparkMax algaeArm;
private final SparkMax algaeSpinner;
private final SparkClosedLoopController armPID;
private final SparkMaxConfig config;
private double setpoint;

  public AlgaeIntakeSubsystem() {
    algaeArm = new SparkMax(MotorConstants.ALGAEARM, MotorType.kBrushless);
    algaeSpinner = new SparkMax(MotorConstants.ALGAESPINNER, MotorType.kBrushless);
    armPID = algaeArm.getClosedLoopController();
    config = new SparkMaxConfig();
    setpoint = 0;

    config
      .idleMode(IdleMode.kBrake);
  // configures the encoders to brake when not moving
    config.closedLoop
      .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
      .pid(3, 0.001, 0)
      .outputRange(-.2, .2);
  // configures PID controllers
    config.closedLoop.maxMotion
      .maxVelocity(2.5)
      .maxAcceleration(1);
  //sets max velocity and acceleration for the elevator motor
    algaeArm.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
   // Sets defaults for the algaeArm motor (don't touch these)
  }

  public void updateSetpoint(double speed) {
    setpoint += (speed/60);
    if (setpoint < -.6) {
      setpoint = -.6;
    } else if (setpoint > 0) {
      setpoint = 0;
    }
  }

  public void goToZero(){
    setpoint = 0;
  }
  //When arm isn't moving and the command is called

  public void intakeL1(){
    algaeSpinner.set(1);
  }
  //Spins algae intake forward

  public void intakeL2(){
    algaeSpinner.set(-1);
  }
  //Spins Algae intake backward

  public void stopAlgaeIntake(){
    algaeSpinner.set(0);
  }

  @Override
  public void periodic() {
      //System.out.println(setpoint);
      armPID.setReference(setpoint, ControlType.kPosition);
  }
}
