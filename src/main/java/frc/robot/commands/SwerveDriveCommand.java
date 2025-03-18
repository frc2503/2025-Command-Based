package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;

public class SwerveDriveCommand extends Command {

    private SwerveDriveSubsystem swerveDriveSubsystem;
    private DoubleSupplier driveXSupplier;
    private DoubleSupplier driveYSupplier;
    private DoubleSupplier rotationSupplier;
    private DoubleSupplier speedScalarSupplier;
    private BooleanSupplier fieldOrientedSupplier;

    public SwerveDriveCommand(
        SwerveDriveSubsystem driveSubsystem,
        DoubleSupplier x,
        DoubleSupplier y,
        DoubleSupplier rotation,
        DoubleSupplier speedScalar,
        BooleanSupplier fieldOriented
    ) {
        swerveDriveSubsystem = driveSubsystem;
        driveXSupplier = x;
        driveYSupplier = y;
        rotationSupplier = rotation;
        speedScalarSupplier = speedScalar;
        fieldOrientedSupplier = fieldOriented;

        addRequirements(swerveDriveSubsystem);
    }

    @Override
    public void execute() {
        // Assign stick inputs to variables, to prevent discrepancies
        double driveX = driveXSupplier.getAsDouble();
        double driveY = driveYSupplier.getAsDouble();
        double rotation = rotationSupplier.getAsDouble();

        // Create deadzones on the joysticks, to prevent stick drift
        if (Math.abs(driveX) < 0.075) {
            driveX = 0.0;
        }
        if (Math.abs(driveY) < 0.075) {
            driveY = 0.0;
        }
        if (Math.abs(rotation) < 0.075) {
            rotation = 0.0;
        }

        swerveDriveSubsystem.drive(driveX, driveY, rotation, speedScalarSupplier.getAsDouble(), fieldOrientedSupplier.getAsBoolean());
    }

    @Override
    public void end(boolean interrupted) {
        // Stop the drivetrain
        swerveDriveSubsystem.drive(0, 0, 0, 0, true);;
    }
    
}
