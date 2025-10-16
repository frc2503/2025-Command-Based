// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.subsystems;

import java.util.Set;

import edu.wpi.first.math.controller.ProfiledPIDController;
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
  // AprilTag Limelight
  private static NetworkTable aprilTagLimelight;
  private static NetworkTableEntry aprilTagHasTarget;
  private static NetworkTableEntry aprilTagTargetOffsetX;
  private static NetworkTableEntry aprilTagTargetOffsetY;
  private static NetworkTableEntry aprilTagTargetArea;
  private static NetworkTableEntry aprilTagTargetSkew;
  private static NetworkTableEntry aprilTagPipeline;

  private static Constraints pidConstraints;
  private static ProfiledPIDController drivePID;
  private double detectedAprilTagId;
  private double targetOffset;

  private Set reefAprilTagIds = Set.of(6, 7, 8, 9, 10, 11, 17, 18, 19, 20, 21, 22);


  public VisionSubsystem() {
    inst = NetworkTableInstance.getDefault();

    initializeIntakeLimelight();
    initializeAprilTagLimelight();
    
    pidConstraints = new Constraints(.5, .1);
    drivePID = new ProfiledPIDController(5, 0, 0, pidConstraints);
  }

  public double getAprilTagOffsetX() {
    if (reefAprilTagIds.contains(detectedAprilTagId)) {
      return aprilTagTargetOffsetX.getDouble(0);
    } else {
      return 0;
    }
  }

  public double getIntakeTargetOffsetX() {
    return intakeTargetOffsetX.getDouble(0);
  }

  public double targetOffset() {
    return  getDrivePID().calculate(getIntakeTargetOffsetX(), 0);
  }

  public ProfiledPIDController getDrivePID() {
    drivePID.reset(0);
    return drivePID;
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
  }
  
  private void initializeAprilTagLimelight() {
    aprilTagLimelight = inst.getTable("limelight-tags");
    aprilTagHasTarget = intakeLimelight.getEntry("tv");
    aprilTagTargetOffsetX = intakeLimelight.getEntry("tx");
    aprilTagTargetOffsetY = intakeLimelight.getEntry("ty");
    aprilTagTargetArea = intakeLimelight.getEntry("ta");
    aprilTagTargetSkew = intakeLimelight.getEntry("ts");
    aprilTagPipeline = intakeLimelight.getEntry("pipeline");
    detectedAprilTagId = intakeLimelight.getEntry("tid").getDouble(-1);
  }
}