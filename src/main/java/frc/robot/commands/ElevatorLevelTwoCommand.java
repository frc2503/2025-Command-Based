package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.ElevatorSubsystem.ElevatorState;

public class ElevatorLevelTwoCommand extends Command {
    private ElevatorSubsystem elevator;
    private CoralSubsystem coral;
    
    public ElevatorLevelTwoCommand(ElevatorSubsystem elevator, CoralSubsystem coral){
        this.elevator = elevator;
        this.coral = coral;

        addRequirements(elevator);
    }

    @Override
    public void execute() {
        if (coral.getBackSensorState() != true) {
            elevator.goToLevelTwo();
        }
    }

    @Override
    public boolean isFinished() {
        return elevator.getCurrentState() == ElevatorState.CORAL_LEVEL_TWO;
    }
}
