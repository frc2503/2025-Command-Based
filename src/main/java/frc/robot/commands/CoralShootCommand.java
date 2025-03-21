package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;

public class CoralShootCommand extends Command {
    private CoralSubsystem coral;
    
    public CoralShootCommand(CoralSubsystem coral){
        this.coral = coral;

        addRequirements(coral);
    }

    @Override
    public void execute() {
        coral.spinIntake();
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
