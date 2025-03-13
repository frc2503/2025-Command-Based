package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeIntakeSubsystem;

public class AlgaeIntakeCommand extends Command {

    private AlgaeIntakeSubsystem algaeIntakeSubsystem;
    private DoubleSupplier intakeSupplier;

    public AlgaeIntakeCommand(
        AlgaeIntakeSubsystem algaeSubsystem,
        DoubleSupplier joystickValue
    ) {
        algaeIntakeSubsystem = algaeSubsystem;
        intakeSupplier = joystickValue;

        addRequirements(algaeIntakeSubsystem);
    }

    @Override
    public void execute() {
        algaeIntakeSubsystem.moveArmPosition(intakeSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        algaeIntakeSubsystem.moveArmPosition(0.0);
    }
}
