// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class AlgaeIntakeSubsystem extends SubsystemBase {
private final SparkMax algaeArm;
private final SparkMax algaeSpinner;

  public AlgaeIntakeSubsystem() {
    algaeArm = new SparkMax(MotorConstants.ALGAEARM, MotorType.kBrushless);
    algaeSpinner = new SparkMax(MotorConstants.ALGAESPINNER, MotorType.kBrushless);
  }

  public void armOut(){
    algaeArm.set(.5);
  }
  //Algae arm moves outward

  public void armIn(){
    algaeArm.set(-.5);
  }
  //Algae arm moves inwards

  public void intakeL1(){
    algaeSpinner.set(.5);
  }
  //Spins algae intake forward

  public void intakeL2(){
    algaeSpinner.set(-.5);
  }
  //Spins Algae intake backward

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
