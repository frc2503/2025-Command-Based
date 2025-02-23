// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final SwerveDriveSubsystem swerveDrive = new SwerveDriveSubsystem();
  private final ElevatorSubsystem elevator = new ElevatorSubsystem();
  private final CommandXboxController driveController = new CommandXboxController(OperatorConstants.DriveControllerPort);
  private final CommandXboxController mechController = new CommandXboxController(OperatorConstants.MechControllerPort);
    public RobotContainer() {
    // Register subsystems
    swerveDrive.register();
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Example of how to toggle field oriented on and off in swerve drive
    new Trigger(driveController.y()).onTrue(Commands.run(() -> swerveDrive.toggleFieldOriented()));

    new Trigger(mechController.a()).onTrue(Commands.run(() -> elevator.goToStageOne()));
    new Trigger(mechController.b()).onTrue(Commands.run(() -> elevator.goToStageTwo()));
    new Trigger(mechController.y()).onTrue(Commands.run(() -> elevator.goToStageThree()));
  }

  public void onTeleopInit() {
    swerveDrive.setDefaultCommand(
      new SwerveDriveCommand(
        swerveDrive,  
        () -> -driveController.getLeftY(),
        () -> -driveController.getLeftX(), 
        () -> -driveController.getRightX()
      )
    );
  }
}
