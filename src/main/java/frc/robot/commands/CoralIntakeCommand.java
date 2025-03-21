package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;

public class CoralIntakeCommand extends Command {
    private CoralSubsystem coral;
    
    public CoralIntakeCommand(CoralSubsystem coral){
        this.coral = coral;

        addRequirements(coral);
    }

    @Override
    public void execute() {
        coral.loadIntake();
    }

    @Override
    public void end(boolean interrupted) {
        coral.stopIntake();
    }

    @Override
    public boolean isFinished() {
        return !coral.getFrontSensorState();
    }
}
