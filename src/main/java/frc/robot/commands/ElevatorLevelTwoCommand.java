package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorLevelTwoCommand extends Command {
    private ElevatorSubsystem elevator;
    private CoralSubsystem coral;
    
    public ElevatorLevelTwoCommand(ElevatorSubsystem elevator, CoralSubsystem coral){
        this.elevator = elevator;
        this.coral = coral;

        addRequirements(elevator);
    }

    @Override
    public void initialize() {
        if (coral.getBackSensorState() != true) {
            elevator.goToLevelTwo();
        }
    }
}
