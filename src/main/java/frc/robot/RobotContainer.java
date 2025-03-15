// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.FunnelAlignCommand;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.subsystems.AlgaeIntakeSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.FunnelSubsystem;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.FunnelSubsystem.FunnelState;
import edu.wpi.first.wpilibj.Joystick;
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
  private final CoralSubsystem coralSubsystem = new CoralSubsystem();
  private final AlgaeIntakeSubsystem algaeIntake = new AlgaeIntakeSubsystem();
  private final FunnelSubsystem funnelSubsystem = new FunnelSubsystem();
  private final FunnelAlignCommand alignCommand = new FunnelAlignCommand(funnelSubsystem);
  private final CommandXboxController driveController = new CommandXboxController(OperatorConstants.DriveControllerPort);
  private final CommandXboxController mechController = new CommandXboxController(OperatorConstants.MechControllerPort);
  private final Joystick mechJoystick = new Joystick(1);
  private double joystickDirection = mechJoystick.getRawAxis(1);
    public RobotContainer() {
    // Register subsystems
    swerveDrive.register();
    coralSubsystem.register();
    funnelSubsystem.register();

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
    //Toggles field oriented driving when you press y on the driver's controller

    new Trigger(mechController.rightTrigger()).whileTrue(Commands.run(() -> coralSubsystem.spinIntake()));
    //Spins the coral intake when the right trigger is held on the mech controller

    new Trigger(mechController.a()).onTrue(Commands.run(() -> elevator.goToLevelOne()));
    new Trigger(mechController.b()).onTrue(Commands.run(() -> elevator.goToLevelTwo()));
    new Trigger(mechController.y()).onTrue(Commands.run(() -> elevator.goToLevelThree()));
    //Switches elevator states when a, b, and y are pressed on the mech controller

    new Trigger(mechController.pov(0)).whileTrue(alignCommand);
    new Trigger(mechController.pov(90)).onTrue(Commands.runOnce(() -> funnelSubsystem.setIntendedState(FunnelState.NEUTRAL)));
    new Trigger(mechController.pov(180)).onTrue(Commands.runOnce(() -> funnelSubsystem.setIntendedState(FunnelState.CLIMB)));
    new Trigger(mechController.pov(270)).onTrue(Commands.runOnce(() -> funnelSubsystem.setIntendedState(FunnelState.POSTCLIMB)));
     
    new Trigger(mechController.leftStick()).onTrue(Commands.run(() -> algaeIntake.goToZero()));

    if(joystickDirection > .25){
      algaeIntake.armIn();
    } 
    //If you push the joystick back, the AlgaeArm goes down

    if(joystickDirection < -.25){
      algaeIntake.armOut();
      //If you push the joystick forward, the AlgaeArm goes up
    } 
    
  }

  public void onAutoInit() {
    Commands.runOnce(() -> elevator.goToLevelOne());
  }

  public void onTeleopInit() {
    // swerveDrive.setDefaultCommand(
    //   new SwerveDriveCommand(
    //     swerveDrive,  
    //     () -> -driveController.getLeftY(),
    //     () -> -driveController.getLeftX(), 
    //     () -> -driveController.getRightX()
    //   )
    // );
  }
}
