package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class AlignOnReefOffsetRightCommand extends Command {
    private VisionSubsystem vision;
    private SwerveDriveSubsystem swerve;
    private final double RIGHT_OFFSET = 5;
    
    public AlignOnReefOffsetRightCommand(VisionSubsystem vision, SwerveDriveSubsystem swerve) {
        this.vision = vision;
        this.swerve = swerve;
        
        addRequirements(vision, swerve);
    }

    @Override
    public void execute() {
        double driveX;
        if (vision.getAprilTagOffsetX() < RIGHT_OFFSET) {
            driveX = 0.15;
        } else if (vision.getAprilTagOffsetX() > RIGHT_OFFSET) {
            driveX = -0.15;
        } else {
            driveX = 0;
        }
        
        swerve.drive(driveX, 0, 0, 0, 0, 1, false);
    }

    @Override
    public void end(boolean interrupted) {
        swerve.stop();
    }

    @Override
    public boolean isFinished() {
        return (vision.getAprilTagOffsetX() < RIGHT_OFFSET + 0.25) && (vision.getAprilTagOffsetX() > RIGHT_OFFSET - 0.25) && vision.getAprilTagOffsetX() != 0;
    }
}