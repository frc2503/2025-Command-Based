// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.AlgaeArmCommand;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.subsystems.AlgaeIntakeSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
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
  private final CommandXboxController driveController = new CommandXboxController(OperatorConstants.DriveControllerPort);
  private final CommandXboxController mechController = new CommandXboxController(OperatorConstants.MechControllerPort);
  private final Joystick mechJoystick = new Joystick(1);
  private double joystickDirection = mechJoystick.getRawAxis(1);
    public RobotContainer() {
    // Register subsystems
    swerveDrive.register();
    coralSubsystem.register();
    elevator.register();
    algaeIntake.register();
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
    new Trigger(driveController.y()).onTrue(Commands.run(() -> swerveDrive.toggleFieldOriented(), swerveDrive));
    //Toggles field oriented driving when you press y on the driver's controller

    new Trigger(mechController.rightBumper()).whileTrue(Commands.run(() -> coralSubsystem.spinIntake(), coralSubsystem));
    //.onFalse(Commands.runOnce(() -> coralSubsystem.stopIntake(), coralSubsystem));
    //Spins the coral intake when the right bumper is held on the mech controller

    //new Trigger(mechController.rightBumper()).onTrue(Commands.run(() -> coralSubsystem.stopIntake()));

    new Trigger(mechController.a()).onTrue(Commands.runOnce(() -> elevator.goToLevelOne(), elevator));
    new Trigger(mechController.b()).onTrue(Commands.runOnce(() -> elevator.goToLevelTwo(), elevator));
    new Trigger(mechController.x()).onTrue(Commands.runOnce(() -> elevator.goToLevelThree(), elevator));
    new Trigger(mechController.y()).onTrue(Commands.runOnce(() -> elevator.goToLevelFour(), elevator));
    //Switches elevator states when a, b, x, and y are pressed on the mech controller

    //new Trigger(mechController.button(7)).onTrue(Commands.run(() -> elevator.testElevatorMotorUp(), elevator)).onFalse(Commands.run(() -> elevator.stopMotor(), elevator));
    //new Trigger(mechController.button(8)).onTrue(Commands.runOnce(() -> elevator.testElevatorMotorDown(), elevator)).onFalse(Commands.run(() -> elevator.stopMotor(), elevator));
    
    new Trigger(mechController.start()).onTrue(Commands.runOnce(() -> algaeIntake.armIn(), algaeIntake));
    new Trigger(mechController.button(8)).onTrue(Commands.runOnce(() -> algaeIntake.armOut(), algaeIntake));
    new Trigger(mechController.leftBumper()).whileTrue(Commands.runOnce(() -> algaeIntake.intakeL1(), algaeIntake)).onFalse(Commands.runOnce(() -> algaeIntake.stopAlgaeIntake(), algaeIntake));
    new Trigger(mechController.leftTrigger()).whileTrue(Commands.runOnce(() -> algaeIntake.intakeL2(), algaeIntake)).onFalse(Commands.runOnce(() -> algaeIntake.stopAlgaeIntake(), algaeIntake));
    

    new Trigger(mechController.leftStick()).onTrue(Commands.run(() -> algaeIntake.goToZero()));

    /*if(joystickDirection > .25){
      algaeIntake.armIn();
    } 
    //If you push the joystick back, the AlgaeArm goes down

    if(joystickDirection < -.25){
      algaeIntake.armOut();
      //If you push the joystick forward, the AlgaeArm goes up
    } */
    
  }

  public void onAutoInit() {
    Commands.runOnce(() -> elevator.goToLevelOne());
  }

  public void onTeleopInit() {
    Commands.runOnce(() -> elevator.goToLevelOne(), elevator);
    swerveDrive.setDefaultCommand(
      new SwerveDriveCommand(
        swerveDrive,  
        () -> -driveController.getLeftY(),
        () -> -driveController.getLeftX(), 
        () -> -driveController.getRightX()
      )
    );

    algaeIntake.setDefaultCommand(new AlgaeArmCommand(algaeIntake, -mechController.getLeftY()));

  }
}
