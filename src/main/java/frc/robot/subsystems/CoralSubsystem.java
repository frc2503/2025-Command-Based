// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class CoralSubsystem extends SubsystemBase {
  private final SparkMax boxMotor;
  private final DigitalInput backSensor;
  private final DigitalInput frontSensor;
  public IntakeState intakeState;
  
  // Creates a new EndEffectorSubsystem. 
  public CoralSubsystem() {
    boxMotor = new SparkMax(MotorConstants.END_EFFECTOR, MotorType.kBrushless);
    backSensor = new DigitalInput(1);
    frontSensor = new DigitalInput(2);
  }

public void spinIntake(){
  boxMotor.set(.5);
}
//When the function is called, spin the box motor at a speed of .5

  @Override
  public void periodic() {
    if (backSensor.get() == true && intakeState != IntakeState.LOADED) {
      spinIntake();
      intakeState = IntakeState.LOADING;
    }
    // Checks the back laserEye, and if it detects a coral, sets the intake state to
    // loading

    if (backSensor.get() == false && frontSensor.get() == true) {
      intakeState = IntakeState.LOADED;
    }
    // Check the front and back laserEyes, if the front sees a coral and the back
    // sees nothing, sets the intake state to loaded

    if (backSensor.get() == false && frontSensor.get() == false) {
      intakeState = IntakeState.EMPTY;
    }
    // If neither laserEye sees anything, set the intake state to empty
  }

  public enum IntakeState{
    EMPTY,
    LOADING,
    LOADED
  }
}