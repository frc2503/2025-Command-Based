// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.AlgaeArmCommand;
import frc.robot.commands.AlignOnReefCommand;
import frc.robot.commands.AlignOnReefOffsetLeftCommand;
import frc.robot.commands.AlignOnReefOffsetRightCommand;
import frc.robot.commands.ClimberInCommand;
import frc.robot.commands.ClimberOutCommand;
import frc.robot.commands.CoralShootCommand;
import frc.robot.commands.ElevatorLevelOneCommand;
import frc.robot.commands.ElevatorLevelTwoCommand;
import frc.robot.commands.ElevatorZeroCommand;
import frc.robot.commands.ElevatorLevelThreeCommand;
import frc.robot.commands.ElevatorLevelThreeAlgaeCommand;
import frc.robot.commands.ElevatorLevelFourCommand;
import frc.robot.commands.FunnelAlignCommand;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.commands.WaitForCoralCommand;
import frc.robot.subsystems.AlgaeIntakeSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.FunnelSubsystem;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.FunnelSubsystem.FunnelState;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
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
  private final CommandXboxController driveController = new CommandXboxController(OperatorConstants.DriveControllerPort);
  private final CommandXboxController mechController = new CommandXboxController(OperatorConstants.MechControllerPort);
  private final SwerveDriveSubsystem swerveDrive = new SwerveDriveSubsystem();
  private final CoralSubsystem coralSubsystem = new CoralSubsystem();
  private final CoralShootCommand shootCommand = new CoralShootCommand(coralSubsystem);
  private final WaitForCoralCommand waitForCoral = new WaitForCoralCommand(coralSubsystem);
  private final ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
  private final ElevatorZeroCommand elevatorZeroCommand = new ElevatorZeroCommand(elevatorSubsystem, coralSubsystem);
  private final ElevatorLevelOneCommand l1Command = new ElevatorLevelOneCommand(elevatorSubsystem, coralSubsystem);
  private final ElevatorLevelTwoCommand l2Command = new ElevatorLevelTwoCommand(elevatorSubsystem, coralSubsystem);
  private final ElevatorLevelThreeCommand l3Command = new ElevatorLevelThreeCommand(elevatorSubsystem, coralSubsystem);
  private final ElevatorLevelThreeAlgaeCommand l3AlgaeCommand = new ElevatorLevelThreeAlgaeCommand(elevatorSubsystem, coralSubsystem);
  private final ElevatorLevelFourCommand l4Command = new ElevatorLevelFourCommand(elevatorSubsystem, coralSubsystem);
  private final AlgaeIntakeSubsystem algaeIntakeSubsystem = new AlgaeIntakeSubsystem();
  private final AlgaeArmCommand armCommand = new AlgaeArmCommand(algaeIntakeSubsystem, () -> mechController.getRightY());
  private final FunnelSubsystem funnelSubsystem = new FunnelSubsystem();
  private final FunnelAlignCommand centerCoralCommand = new FunnelAlignCommand(funnelSubsystem);
  private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
  private final ClimberInCommand inCommand = new ClimberInCommand(climberSubsystem, funnelSubsystem);
  private final ClimberOutCommand outCommand = new ClimberOutCommand(climberSubsystem, funnelSubsystem);
  private final VisionSubsystem visionSubsystem = new VisionSubsystem();
  private final AlignOnReefCommand alignOnReefCommand = new AlignOnReefCommand(visionSubsystem, swerveDrive);
  private final AlignOnReefOffsetLeftCommand alignOnReefOffsetLeftCommand = new AlignOnReefOffsetLeftCommand(visionSubsystem, swerveDrive);
  private final AlignOnReefOffsetRightCommand alignOnReefOffsetRIghtCommand = new AlignOnReefOffsetRightCommand(visionSubsystem, swerveDrive);
  private final SendableChooser<Command> autoChooser;
  
  public RobotContainer() {
    // Register subsystems
    swerveDrive.register();
    coralSubsystem.register();
    elevatorSubsystem.register();
    algaeIntakeSubsystem.register();
    funnelSubsystem.register();
    climberSubsystem.register();
    //visionSubsystem.register();

    configureBindings();
    registerNamedCommands();

    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
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
    new Trigger(mechController.rightTrigger()).onTrue(shootCommand);
    //Spins the coral intake when the right trigger is held on the mech controller

    new Trigger(mechController.a()).onTrue(l1Command);
    new Trigger(mechController.b()).onTrue(l2Command);
    new Trigger(mechController.x()).onTrue(l3Command);
    new Trigger(mechController.y()).onTrue(l3AlgaeCommand);
    new Trigger(mechController.start()).whileTrue(elevatorZeroCommand);
    new Trigger(mechController.rightBumper()).onTrue(l4Command);
    //Switches elevator states when a, b, and y are pressed on the mech controller

    new Trigger(mechController.pov(0)).onTrue(centerCoralCommand);
    new Trigger(mechController.pov(90)).onTrue(Commands.runOnce(() -> funnelSubsystem.setIntendedState(FunnelState.NEUTRAL), funnelSubsystem));
    new Trigger(mechController.pov(180)).onTrue(Commands.runOnce(() -> funnelSubsystem.setIntendedState(FunnelState.CLIMB), funnelSubsystem));
    new Trigger(mechController.pov(270)).onTrue(Commands.runOnce(() -> funnelSubsystem.setIntendedState(FunnelState.POSTCLIMB), funnelSubsystem));
     
    new Trigger(mechController.rightStick()).onTrue(Commands.run(() -> algaeIntakeSubsystem.goToZero(), algaeIntakeSubsystem));

    new Trigger(mechController.axisMagnitudeGreaterThan(5, .1)).whileTrue(armCommand);
    new Trigger(mechController.leftBumper()).onTrue(Commands.run(() -> algaeIntakeSubsystem.intakeL2(), algaeIntakeSubsystem)).onFalse(Commands.runOnce(() -> algaeIntakeSubsystem.stopAlgaeIntake(), algaeIntakeSubsystem));
    new Trigger(mechController.leftTrigger(.25)).onTrue(Commands.run(() -> algaeIntakeSubsystem.intakeL1(), algaeIntakeSubsystem)).onFalse(Commands.runOnce(() -> algaeIntakeSubsystem.stopAlgaeIntake(), algaeIntakeSubsystem));
    new Trigger(driveController.x()).whileTrue(alignOnReefCommand);
    
    new Trigger(driveController.rightBumper()).whileTrue(inCommand);
    new Trigger(driveController.leftBumper()).whileTrue(outCommand);
    new Trigger(driveController.a()).onTrue(Commands.runOnce(() -> swerveDrive.resetFieldOrientation(), swerveDrive));
  }

  private void registerNamedCommands() {
    NamedCommands.registerCommand("Align Coral", centerCoralCommand);
    NamedCommands.registerCommand("Shoot Coral", shootCommand);
    NamedCommands.registerCommand("Wait For Coral", waitForCoral);
    NamedCommands.registerCommand("Elevator L1", l1Command);
    NamedCommands.registerCommand("Elevator L2", l2Command);
    NamedCommands.registerCommand("Elevator L3", l3Command);
    NamedCommands.registerCommand("Elevator L4", l4Command);
    NamedCommands.registerCommand("Align On Reef", alignOnReefCommand);
    NamedCommands.registerCommand("Align On Reef Left", alignOnReefOffsetLeftCommand);
    NamedCommands.registerCommand("Align On Reef Right", alignOnReefOffsetRIghtCommand);
  
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  public void onAutoInit() {
    l1Command.schedule();
    getAutonomousCommand().schedule();
  }

  public void onTeleopInit() {
    l1Command.schedule();
    swerveDrive.setDefaultCommand(
      new SwerveDriveCommand(
        swerveDrive,  
        () -> -driveController.getLeftX(),
        () -> -driveController.getLeftY(), 
        () -> -driveController.getRightX(),
        () -> -mechController.getLeftX(),
        () -> -mechController.getLeftY(),
        () -> (1 - (driveController.getRightTriggerAxis() / 2)),
        () -> (driveController.getLeftTriggerAxis() < .25)
      )
    );
  }
}
