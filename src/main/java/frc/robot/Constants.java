// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
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
