// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.subsystems;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionSubsystem extends SubsystemBase {
  private static NetworkTableInstance inst;
  private static NetworkTable Limelight;
  private static NetworkTableEntry HasTarget;
  private static NetworkTableEntry TargetOffsetH;
  private static NetworkTableEntry TargetOffsetV;
  private static NetworkTableEntry TargetArea;
  private static NetworkTableEntry TargetSkew;
  private static NetworkTableEntry Pipeline;
  private static Constraints pidConstraints;
  private static ProfiledPIDController drivePID;

  public VisionSubsystem() {
    inst = NetworkTableInstance.getDefault();
    Limelight = inst.getTable("Reef-View");
    HasTarget = Limelight.getEntry("tv");
    TargetOffsetH = Limelight.getEntry("tx");
    TargetOffsetV = Limelight.getEntry("ty");
    TargetArea = Limelight.getEntry("ta");
    TargetSkew = Limelight.getEntry("ts");
    Pipeline = Limelight.getEntry("pipeline");
    pidConstraints = new Constraints(1, 1);
    drivePID = new ProfiledPIDController(5, 0, 0, pidConstraints);
  }

  public double getTargetOffsetH() {
    return TargetOffsetH.getDouble(0);
  }

  public ProfiledPIDController getDrivePID() {
    return drivePID;
  }

  @Override
  public void periodic() {
      System.out.println(TargetOffsetH.getDouble(0));
  }
}