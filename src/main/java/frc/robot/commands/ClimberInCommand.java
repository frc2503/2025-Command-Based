package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.FunnelSubsystem;
import frc.robot.subsystems.FunnelSubsystem.FunnelState;

public class ClimberInCommand extends Command {
    private ClimberSubsystem climber;
    private FunnelSubsystem funnel;
    
    public ClimberInCommand(ClimberSubsystem climber, FunnelSubsystem funnel){
        this.climber = climber;
        this.funnel = funnel;

        addRequirements(climber, funnel);
    }

    @Override
    public void execute() {
        if (funnel.getIntendedState() != FunnelState.CLIMB) {
            climber.climberStop();
        } else {
            climber.climberIn();
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.climberStop();
    }
}
