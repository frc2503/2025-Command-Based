package frc.robot.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class AlignOnReefOffsetRightCommand extends Command {
    private VisionSubsystem vision;
    private SwerveDriveSubsystem swerve;
    private static Constraints pidConstraints;
    private static ProfiledPIDController drivePID;
    private final double RIGHT_OFFSET = 5;
    
    public AlignOnReefOffsetRightCommand(VisionSubsystem vision, SwerveDriveSubsystem swerve) {
        this.vision = vision;
        this.swerve = swerve;
        pidConstraints = new Constraints(.5, .1);
        drivePID = new ProfiledPIDController(5, 0, 0, pidConstraints);
        
        addRequirements(vision, swerve);
    }

    @Override
    public void execute() {
        swerve.drive(drivePID.calculate(vision.getIntakeTargetOffsetX(), RIGHT_OFFSET), 0, 0, 0, 0, 1, false);
    }

    @Override
    public void end(boolean interrupted) {
        swerve.stop();
    }

    @Override
    public boolean isFinished() {
        return (vision.getIntakeTargetOffsetX() < RIGHT_OFFSET + 0.25) && (vision.getIntakeTargetOffsetX() > RIGHT_OFFSET - 0.25) && vision.getIntakeTargetOffsetX() != 0;
    }
}