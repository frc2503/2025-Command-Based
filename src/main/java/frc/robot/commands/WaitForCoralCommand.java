package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;

public class WaitForCoralCommand extends Command {
    private CoralSubsystem coral;

    
    public WaitForCoralCommand(CoralSubsystem coral){
        this.coral = coral;

        addRequirements(coral);
    }

    @Override
    public boolean isFinished() {
        return coral.getBackSensorState();
    }
}
