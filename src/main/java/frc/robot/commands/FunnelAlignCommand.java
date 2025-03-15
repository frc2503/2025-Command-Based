package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FunnelSubsystem;

public class FunnelAlignCommand extends Command {

    private FunnelSubsystem funnelSubsystem;

    public FunnelAlignCommand(FunnelSubsystem funnelSubsystem) {
        this.funnelSubsystem = funnelSubsystem;
        addRequirements(funnelSubsystem);
    }

    @Override
    public void initialize() {
        funnelSubsystem.alignPosition();
    }

    @Override
    public void end(boolean interrupted) {
        funnelSubsystem.neutralPosition();
    }
}
