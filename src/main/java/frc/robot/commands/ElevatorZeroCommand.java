package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorZeroCommand extends Command {
    private ElevatorSubsystem elevator;
    private CoralSubsystem coral;
        
    public ElevatorZeroCommand(ElevatorSubsystem elevator, CoralSubsystem coral){
        this.elevator = elevator;
        this.coral = coral;

        addRequirements(elevator);
    }

    @Override
    public void execute() {
        if (coral.getBackSensorState() != true) {
            elevator.lowerElevator();
        }
    }

    @Override
    public boolean isFinished() {
        return elevator.isAtZero();
    }
}
