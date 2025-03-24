//package frc.robot.commands;

//import edu.wpi.first.wpilibj2.command.Command;
//import frc.robot.subsystems.SwerveDriveSubsystem;
//import frc.robot.subsystems.VisionSubsystem;

/*
public class AlignOnReefCommand extends Command {
    private VisionSubsystem vision;
    private SwerveDriveSubsystem swerve;
    
    public AlignOnReefCommand(VisionSubsystem vision, SwerveDriveSubsystem swerve) {
        this.vision = vision;
        this.swerve = swerve;
        
        addRequirements(vision, swerve);
    }

    @Override
    public void execute() {
        swerve.drive(vision.getDrivePID().calculate(vision.getTargetOffsetH(), 0),
                    0, 0, 0, 0, 1, false);
    }

    @Override
    public void end(boolean interrupted) {
        swerve.stop();
    }

    @Override
    public boolean isFinished() {
        return vision.getTargetOffsetH() < 1;
    }
}
*/