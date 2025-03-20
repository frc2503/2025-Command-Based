package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;

import java.io.File;
import java.io.IOException;
import org.json.simple.parser.ParseException;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.DistanceUnit;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.util.DriveFeedforwards;

public class SwerveDriveSubsystem extends SubsystemBase {
    private SwerveDrive swerveDrive; // Define this in the constructor

    public SwerveDriveSubsystem() {

        SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
        File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
        try {
            swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(getMaximumVelocity());
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Unable to read YAGSL JSON. Please add JSON files to the deploy/swerve directory.");
        }

        swerveDrive.setHeadingCorrection(false);
        swerveDrive.setCosineCompensator(true);
        swerveDrive.setAngularVelocityCompensation(true,true, 0.1);
        swerveDrive.setModuleEncoderAutoSynchronize(false, 1);
        DCMotor driveMotor = new DCMotor(DriveConstants.NOMINAL_VOLTAGE, DriveConstants.STALL_TORQUE, DriveConstants.STALL_CURRENT, DriveConstants.FREE_CURRENT, DriveConstants.FREE_RPM, DriveConstants.NUM_DRIVE_MOTORS);
        ModuleConfig moduleConfig = new ModuleConfig(DriveConstants.WHEEL_DIAMETER, DriveConstants.MAXIMUM_VELOCITY, DriveConstants.WHEEL_COEFFICIENT_OF_FRICTION, driveMotor, DriveConstants.DRIVE_CURRENT_LIMIT, DriveConstants.NUM_DRIVE_MOTORS);
        Translation2d[] moduleOffsets = {DriveConstants.FRONT_LEFT_OFFSET, DriveConstants.FRONT_RIGHT_OFFSET, DriveConstants.BACK_LEFT_OFFSET, DriveConstants.BACK_RIGHT_OFFSET};
        RobotConfig robotConfig = new RobotConfig(DriveConstants.MASS, DriveConstants.MOMENT_OF_INERTIA, moduleConfig, moduleOffsets);
        AutoBuilder.configure(this::getPose, swerveDrive::resetOdometry, swerveDrive::getRobotVelocity, this::drive,
                new PPHolonomicDriveController(
                    new PIDConstants(5.0, 0.0, 0.0),
                    new PIDConstants(5.0, 0.0, 0.0)),
                robotConfig,
                () -> DriverStation.getAlliance().isPresent() ? DriverStation.getAlliance().get() == DriverStation.Alliance.Red : false,
                this);
    }

    public void drive(double x, double y, double rotation, double speedScalar, boolean fieldOriented) {
        Translation2d translation = new Translation2d(
            x * getMaximumVelocity() * speedScalar,
            y * getMaximumVelocity() * speedScalar
        );
        double angularRotation = rotation * swerveDrive.getMaximumChassisAngularVelocity() * speedScalar;
        
        swerveDrive.drive(translation, angularRotation, fieldOriented, false);
    }

    public void drive(ChassisSpeeds speeds, DriveFeedforwards feedforward) {
        swerveDrive.drive(speeds);
    }

    public double getMaximumVelocity() {
        return DriveConstants.MAXIMUM_VELOCITY.in(MetersPerSecond);
    }

    // Gets the current pose (position and rotation) of the robot, as reported by odometry.
    public Pose2d getPose() {
        return swerveDrive.getPose();
    }

    public Rotation2d getRotation() {
        return getPose().getRotation();
    }
}
