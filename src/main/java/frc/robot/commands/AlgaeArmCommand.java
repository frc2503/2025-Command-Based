package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeIntakeSubsystem;

public class AlgaeArmCommand extends Command {
    private AlgaeIntakeSubsystem algae;
    private Double armSpeed; 
    
    public AlgaeArmCommand(AlgaeIntakeSubsystem algae, Double armSpeed){
        this.algae = algae;
        this.armSpeed = armSpeed;

        addRequirements(algae);
    }

    @Override
    public void execute() {

        if(armSpeed > .25){
            algae.intakeL1();
        } else if(armSpeed < -.25){
            algae.intakeL2();
        } else {
            algae.armStop();
        }

    }


}
