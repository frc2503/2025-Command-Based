// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
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
private final SparkClosedLoopController PIDcontroller;
private final RelativeEncoder encoder;
private final SparkMaxConfig config;

  public AlgaeIntakeSubsystem() {
    algaeArm = new SparkMax(MotorConstants.ALGAEARM, MotorType.kBrushless);
    algaeSpinner = new SparkMax(MotorConstants.ALGAESPINNER, MotorType.kBrushless);
    PIDcontroller = algaeArm.getClosedLoopController();
    encoder = algaeArm.getEncoder();
    config = new SparkMaxConfig();

    config
      .idleMode(IdleMode.kBrake);
  // configures the encoders to brake when not moving
    config.closedLoop
      .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
      .pid(.3, 0, .01)
      .outputRange(-.2, .2);
  // configures PID controllers
    config.closedLoop.maxMotion
      .maxVelocity(2.5)
      .maxAcceleration(1);
  //sets max velocity and acceleration for the elevator motor
    algaeArm.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
   // Sets defaults for the algaeArm motor (don't touch these)
  }

  public void armOut(){
    algaeArm.set(-.1);
  }
  //Algae arm moves outward if the motor is at less than 5 rotations and isn't moving to zero

  public void armIn(){
    algaeArm.set(.1);
  }
  //Algae arm moves outward if the motor is at less than 5 rotations and isn't moving to zero

  public void armStop(){
    algaeArm.set(0);
  }

  public void goToZero(){
    if(moving == false){
      movingToZero = true;
      PIDcontroller.setReference(0, ControlType.kPosition);
    }
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

  private boolean withinBounds(double setpoint){
    return encoder.getPosition() >= (setpoint - .25) && encoder.getPosition() <= (setpoint + .25);
  }

  private boolean moving = false;

  private boolean movingToZero = false;

  @Override
  public void periodic() {
    if(movingToZero == true){
      if(withinBounds(0)){
        movingToZero = false;
      }
      //Tracks if the arm is moving to zero, and if it's within .25 inches, stop the arm
    }
  }
}
