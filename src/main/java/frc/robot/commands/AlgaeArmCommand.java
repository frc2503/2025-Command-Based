package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeIntakeSubsystem;

public class AlgaeArmCommand extends Command {
    private AlgaeIntakeSubsystem algae;
    private DoubleSupplier speed;
    
    public AlgaeArmCommand(AlgaeIntakeSubsystem algae, DoubleSupplier speed){
        this.algae = algae;
        this.speed = speed;

        addRequirements(algae);
    }

    @Override
    public void execute() {
        algae.updateSetpoint(speed.getAsDouble());
    }


}
