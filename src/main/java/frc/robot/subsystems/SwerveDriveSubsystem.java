package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class SwerveDriveSubsystem extends SubsystemBase {
    private double maximumVelocity = Units.feetToMeters(3);
    private SwerveDrive swerveDrive; // Define this in the constructor

    public SwerveDriveSubsystem() {
        SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
        File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
        try {
            swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(maximumVelocity);
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Unable to read YAGSL JSON. Please add JSON files to the deploy/swerve directory.");
        }

        swerveDrive.setHeadingCorrection(false);
        swerveDrive.setCosineCompensator(false);
        swerveDrive.setAngularVelocityCompensation(true,true, 0.1);
        swerveDrive.setModuleEncoderAutoSynchronize(false, 1);
    }
    
    public void drive(Translation2d translation, double rotation, boolean fieldRelative) {
        swerveDrive.drive(translation, rotation, fieldRelative, false);
    }

    public void drive(double x, double y, double rotation, boolean fieldOriented) {
        Translation2d translation = new Translation2d(
            x * maximumVelocity,
            y * maximumVelocity
        );
        double angularRotation = rotation * swerveDrive.getMaximumChassisAngularVelocity();
        
        swerveDrive.drive(translation, angularRotation, fieldOriented, false);
    }

    public double getMaximumVelocity() {
        return maximumVelocity;
    }

    // Gets the current pose (position and rotation) of the robot, as reported by odometry.
    public Pose2d getPose() {
        return swerveDrive.getPose();
    }

    public Rotation2d getRotation() {
        return getPose().getRotation();
    }
}
