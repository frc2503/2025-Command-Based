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
    private DoubleSupplier operatorXSupplier;
    private DoubleSupplier operatorYSupplier;
    private DoubleSupplier speedScalarSupplier;
    private BooleanSupplier fieldOrientedSupplier;

    public SwerveDriveCommand(
        SwerveDriveSubsystem driveSubsystem,
        DoubleSupplier driveX,
        DoubleSupplier driveY,
        DoubleSupplier rotation,
        DoubleSupplier operatorX,
        DoubleSupplier operatorY,
        DoubleSupplier speedScalar,
        BooleanSupplier fieldOriented
    ) {
        swerveDriveSubsystem = driveSubsystem;
        driveXSupplier = driveX;
        driveYSupplier = driveY;
        rotationSupplier = rotation;
        operatorXSupplier = operatorX;
        operatorYSupplier = operatorY;
        speedScalarSupplier = speedScalar;
        fieldOrientedSupplier = fieldOriented;

        addRequirements(swerveDriveSubsystem);
    }

    @Override
    public void execute() {
        swerveDriveSubsystem.drive(
            driveXSupplier.getAsDouble(), 
            driveYSupplier.getAsDouble(), 
            rotationSupplier.getAsDouble(), 
            operatorXSupplier.getAsDouble(),
            operatorYSupplier.getAsDouble(),
            speedScalarSupplier.getAsDouble(), 
            fieldOrientedSupplier.getAsBoolean());
    }

    @Override
    public void end(boolean interrupted) {
        // Stop the drivetrain
        swerveDriveSubsystem.stop();
    }
    
}
