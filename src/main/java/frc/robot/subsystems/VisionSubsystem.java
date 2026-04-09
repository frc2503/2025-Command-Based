// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.subsystems;

import java.util.Set;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionSubsystem extends SubsystemBase {
  private static NetworkTableInstance inst;
  // Intake Limelight
  private static NetworkTable intakeLimelight;
  private static NetworkTableEntry intakeHasTarget;
  private static NetworkTableEntry intakeTargetOffsetX;
  private static NetworkTableEntry intakeTargetOffsetY;
  private static NetworkTableEntry intakeTargetArea;
  private static NetworkTableEntry intakeTargetSkew;
  private static NetworkTableEntry intakePipeline;

  private static PIDController xPID;
  private static PIDController yPID;
  private static PIDController rotPID;
  private double detectedAprilTagId;
  private double targetOffset;

  private Set reefAprilTagIds = Set.of(6, 7, 8, 9, 10, 11, 17, 18, 19, 20, 21, 22);


  public VisionSubsystem() {
    inst = NetworkTableInstance.getDefault();

    initializeIntakeLimelight();
    
    xPID = new PIDController(.01, 0, 0);
    xPID.setTolerance(.5, .2);
    yPID = new PIDController(.01, 0, 0);
    yPID.setTolerance(.25, .1);
    rotPID = new PIDController(.01, 0, 0);
    rotPID.setTolerance(.25, .1);
  }

  public double getIntakeTargetOffsetX(double defaultValue) {
    return intakeTargetOffsetX.getDouble(defaultValue);
  }

  public double getIntakeTargetOffsetY(double defaultValue) {
    return intakeTargetOffsetY.getDouble(defaultValue);
  }

  public double getIntakeTargetSkew() {
    return intakeTargetSkew.getDouble(0);
  }

  public double targetOffset() {
    return xPID.calculate(getIntakeTargetOffsetX(0), 0);
  }

  public PIDController getXPID() {
    return xPID;
  }

  public PIDController getYPID() {
    return yPID;
  }

  public PIDController getRotPID() {
    return rotPID;
  }

  @Override
  public void periodic() {
      //System.out.println(getTargetOffsetH());
      SmartDashboard.putNumber("Offset", targetOffset());
  }

  private void initializeIntakeLimelight() {
    intakeLimelight = inst.getTable("limelight-intake");
    intakeHasTarget = intakeLimelight.getEntry("tv");
    intakeTargetOffsetX = intakeLimelight.getEntry("tx");
    intakeTargetOffsetY = intakeLimelight.getEntry("ty");
    intakeTargetArea = intakeLimelight.getEntry("ta");
    intakeTargetSkew = intakeLimelight.getEntry("ts");
    intakePipeline = intakeLimelight.getEntry("pipeline");
    detectedAprilTagId = intakeLimelight.getEntry("tid").getDouble(-1);
  }
}