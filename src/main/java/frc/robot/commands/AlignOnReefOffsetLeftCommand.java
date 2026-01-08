package frc.robot.commands;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class AlignOnReefOffsetLeftCommand extends Command {
    private final double LEFT_OFFSET = -5;
    private VisionSubsystem vision;
    private SwerveDriveSubsystem swerve;
    private static Constraints pidConstraints;
    private static ProfiledPIDController drivePID;
    
    
    public AlignOnReefOffsetLeftCommand(VisionSubsystem vision, SwerveDriveSubsystem swerve) {
        this.vision = vision;
        this.swerve = swerve;
        pidConstraints = new Constraints(.5, .1);
        drivePID = new ProfiledPIDController(5, 0, 0, pidConstraints);
        
        addRequirements(vision, swerve);
    }

    @Override
    public void execute() {
        swerve.drive(drivePID.calculate(vision.getIntakeTargetOffsetX(), LEFT_OFFSET), 0, 0, 0, 0, 1, false);
    }

    @Override
    public void end(boolean interrupted) {
        swerve.stop();
    }

    @Override
    public boolean isFinished() {
        return (vision.getIntakeTargetOffsetX() < LEFT_OFFSET + 0.25) && (vision.getIntakeTargetOffsetX() > LEFT_OFFSET + -0.25) && vision.getIntakeTargetOffsetX() != 0;
    }
}