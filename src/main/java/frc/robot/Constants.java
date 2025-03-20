// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.units.measure.Mass;
import edu.wpi.first.units.measure.MomentOfInertia;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class DriveConstants {
    public static final int NOMINAL_VOLTAGE = 12;
    public static final double STALL_TORQUE = 2.6 * 6.75;
    public static final double STALL_CURRENT = 105;
    public static final double FREE_CURRENT = 1.8;
    public static final double FREE_RPM = 5676/6.75;
    public static final Distance WHEEL_DIAMETER = Units.Inch.of(4);
    public static final LinearVelocity MAXIMUM_VELOCITY = Units.FeetPerSecond.of(9);
    public static final double WHEEL_COEFFICIENT_OF_FRICTION = 1.19;
    public static final Current DRIVE_CURRENT_LIMIT = Units.Amps.of(40);
    public static final int NUM_DRIVE_MOTORS = 1;
    public static final Mass MASS = Units.Pounds.of(110);
    public static final MomentOfInertia MOMENT_OF_INERTIA = Units.KilogramSquareMeters.of(2.4176702976);
    public static final Translation2d FRONT_LEFT_OFFSET = new Translation2d(-0.287401, 0.2957576);
    public static final Translation2d FRONT_RIGHT_OFFSET = new Translation2d(0.287401, 0.2957576);
    public static final Translation2d BACK_LEFT_OFFSET = new Translation2d(-0.287401, -0.2957576);
    public static final Translation2d BACK_RIGHT_OFFSET = new Translation2d(0.287401, -0.2957576);
  }

  public static class OperatorConstants {
    public static final int DriveControllerPort = 0;
    public static final int MechControllerPort = 1;
  }

  public static class MotorConstants {
    public static final int ELEVATOR = 19;
    public static final int END_EFFECTOR = 18;
    public static final int ALGAEARM = 17;
    public static final int ALGAESPINNER = 15;
    public static final int FUNNELLEFT = 14;
    public static final int FUNNELRIGHT = 13;
    public static final int CLIMBER = 16;
  }

  public static class FunnelEncoderConstants {
    public static final double LEFT_ALIGN = 2.45;
    public static final double LEFT_NEUTRAL = 0;
    public static final double LEFT_CLIMB = -15;
    public static final double LEFT_POSTCLIMB = -3.76;
    public static final double RIGHT_ALIGN = -3.3;
    public static final double RIGHT_NEUTRAL = 0;
    public static final double RIGHT_CLIMB = 18.64;
    public static final double RIGHT_POSTCLIMB = 4.52;
  }
}
