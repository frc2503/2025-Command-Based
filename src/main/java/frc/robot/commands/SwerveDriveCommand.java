package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class SwerveDriveCommand extends Command {

    private SwerveDriveSubsystem swerveDriveSubsystem;
    private DoubleSupplier driveXSupplier;
    private DoubleSupplier driveYSupplier;
    private DoubleSupplier rotationSupplier;

    public SwerveDriveCommand(
        SwerveDriveSubsystem driveSubsystem,
        DoubleSupplier x,
        DoubleSupplier y,
        DoubleSupplier rotation
    ) {
        swerveDriveSubsystem = driveSubsystem;
        driveXSupplier = x;
        driveYSupplier = y;
        rotationSupplier = rotation;

        addRequirements(swerveDriveSubsystem);
    }

    @Override
    public void execute() {
        swerveDriveSubsystem.drive(
            driveXSupplier.getAsDouble(), 
            driveYSupplier.getAsDouble(), 
            rotationSupplier.getAsDouble(), 
            true // We can add functionality in the future to swap this between field oriented and robot oriented 
        );
    }

    @Override
    public void end(boolean interrupted) {
        // Stop the drivetrain
        swerveDriveSubsystem.drive(0, 0, 0, false);;
    }
    
}
