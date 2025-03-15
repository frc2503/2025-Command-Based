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
        // Assign stick inputs to variables, to prevent discrepancies
        double driveX = driveXSupplier.getAsDouble();
        double driveY = driveYSupplier.getAsDouble();
        double rotation = rotationSupplier.getAsDouble();

        // Create deadzones on the joysticks, to prevent stick drift
        if (Math.abs(driveX) < 0.05) {
            driveX = 0.0;
        }
        if (Math.abs(driveY) < 0.05) {
            driveY = 0.0;
        }
        if (Math.abs(rotation) < 0.05) {
            rotation = 0.0;
        }

        swerveDriveSubsystem.drive(
            driveXSupplier.getAsDouble(), 
            driveYSupplier.getAsDouble(), 
            rotationSupplier.getAsDouble()
        );
    }

    @Override
    public void end(boolean interrupted) {
        // Stop the drivetrain
        swerveDriveSubsystem.drive(0, 0, 0);;
    }
    
}
