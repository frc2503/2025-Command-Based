package frc.robot.commands;

import java.lang.constant.Constable;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.ReefConstants;
import frc.robot.subsystems.SwerveDriveSubsystem;
import frc.robot.subsystems.VisionSubsystem;

public class AlignOnReefOffsetRightCommand extends Command {
    private VisionSubsystem vision;
    private SwerveDriveSubsystem swerve;
    private PIDController xPID;
    private PIDController yPID;
    private PIDController rotPID;
    
    public AlignOnReefOffsetRightCommand(VisionSubsystem vision, SwerveDriveSubsystem swerve) {
        this.vision = vision;
        this.swerve = swerve;
        xPID = vision.getXPID();
        yPID = vision.getYPID();
        rotPID = vision.getRotPID();
        
        addRequirements(vision, swerve);
    }

    @Override
    public void execute() {
        double xSetpoint = MathUtil.clamp(xPID.calculate(vision.getIntakeTargetOffsetX(ReefConstants.RIGHT_POLE_X_OFFSET), ReefConstants.RIGHT_POLE_X_OFFSET), -1, 1);
        double ySetpoint = MathUtil.clamp(yPID.calculate(vision.getIntakeTargetOffsetY(ReefConstants.POLE_Y_OFFSET), ReefConstants.POLE_Y_OFFSET), -1, 1);
        double rotSetpoint = MathUtil.clamp(rotPID.calculate(vision.getIntakeTargetSkew(), 0), -1, 1);
        swerve.drive(xSetpoint, ySetpoint, rotSetpoint, 0, 0, 1, false);
    }

    @Override
    public void end(boolean interrupted) {
        swerve.stop();
    }

    @Override
    public boolean isFinished() {
        return xPID.atSetpoint() && yPID.atSetpoint() && rotPID.atSetpoint();
    }
}