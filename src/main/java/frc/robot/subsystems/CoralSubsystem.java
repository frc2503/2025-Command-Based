// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;
import frc.robot.subsystems.CoralSubsystem.IntakeState;
import frc.robot.subsystems.ElevatorSubsystem.ElevatorState;

public class CoralSubsystem extends SubsystemBase {
  private final SparkMax boxMotor;
  private final DigitalInput backSensor;
  private final DigitalInput frontSensor;
  public IntakeState intakeState;
  
  // Creates a new EndEffectorSubsystem. 
  public CoralSubsystem() {
    boxMotor = new SparkMax(MotorConstants.END_EFFECTOR, MotorType.kBrushless);
    backSensor = new DigitalInput(1);
    frontSensor = new DigitalInput(0);
  }

public void loadIntake(){
  boxMotor.set(-.2);
}
//A slower speed is nice to index the coral

public void spinIntake(){
  boxMotor.set(-.5);
}
//When the function is called, spin the box motor at a speed of .5

public void stopIntake(){
  boxMotor.set(0);
}

public boolean getBackSensorState() {
  return backSensor.get();
}

public boolean getFrontSensorState() {
  return frontSensor.get();
}

public IntakeState getState() {
  return intakeState;
}

//If the motor is not set back to 0 it continues to run

  @Override
  public void periodic() {
    if (frontSensor.get() == true) {
      SmartDashboard.putBoolean("Front Sensor", true);

    } else {
      SmartDashboard.putBoolean("Front Sensor", false);
    }

    if (backSensor.get() == true && intakeState != IntakeState.LOADED) {
      loadIntake();
      intakeState = IntakeState.LOADING;
      SmartDashboard.putString("Intake State", "Loading");
    }
    // Checks the back laserEye, and if it detects a coral, sets the intake state to
    // loading

    if (backSensor.get() == false && frontSensor.get() == true) {
      stopIntake();
      intakeState = IntakeState.LOADED;
      SmartDashboard.putString("Intake State", "Loaded");
    }
    // Check the front and back laserEyes, if the front sees a coral and the back
    // sees nothing, sets the intake state to loaded

    if (backSensor.get() == false && frontSensor.get() == false) {
      stopIntake();
      intakeState = IntakeState.EMPTY;
      SmartDashboard.putString("Intake State", "Empty...");
    }
    // If neither laserEye sees anything, set the intake state to empty

    //System.out.println(intakeState);
    //System.out.println(frontSensor.get());
  }

  public enum IntakeState{
    EMPTY,
    LOADING,
    DISCHARGING,
    LOADED
  }
}