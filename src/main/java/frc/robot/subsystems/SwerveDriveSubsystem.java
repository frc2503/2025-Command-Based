package frc.robot.subsystems;

import static edu.wpi.first.units.Units.MetersPerSecond;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class SwerveDriveSubsystem extends SubsystemBase {
    private SwerveDrive swerveDrive; // Define this in the constructor
    private final Field2d field;

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

        RobotConfig robotConfig;
        try{
            robotConfig = RobotConfig.fromGUISettings();
            SmartDashboard.putBoolean("Config is fallback?", false);
        } catch (Exception e) {
            DCMotor driveMotor = new DCMotor(DriveConstants.NOMINAL_VOLTAGE, DriveConstants.STALL_TORQUE, DriveConstants.STALL_CURRENT, DriveConstants.FREE_CURRENT, DriveConstants.FREE_RPM, DriveConstants.NUM_DRIVE_MOTORS);
            ModuleConfig moduleConfig = new ModuleConfig(DriveConstants.WHEEL_DIAMETER, DriveConstants.MAXIMUM_VELOCITY, DriveConstants.WHEEL_COEFFICIENT_OF_FRICTION, driveMotor, DriveConstants.DRIVE_CURRENT_LIMIT, DriveConstants.NUM_DRIVE_MOTORS);
            Translation2d[] moduleOffsets = {DriveConstants.FRONT_LEFT_OFFSET, DriveConstants.FRONT_RIGHT_OFFSET, DriveConstants.BACK_LEFT_OFFSET, DriveConstants.BACK_RIGHT_OFFSET};
            robotConfig = new RobotConfig(DriveConstants.MASS, DriveConstants.MOMENT_OF_INERTIA, moduleConfig, moduleOffsets);
            SmartDashboard.putBoolean("Config is fallback?", true);
        }

        AutoBuilder.configure(this::getPose, swerveDrive::resetOdometry, swerveDrive::getRobotVelocity, this::drive,
                new PPHolonomicDriveController(
                    new PIDConstants(0, 0,0),
                    new PIDConstants(1, 0, 0)),
                robotConfig,
                () -> DriverStation.getAlliance().isPresent() ? DriverStation.getAlliance().get().equals(DriverStation.Alliance.Red) : false,
                this);

        field = new Field2d();
        SmartDashboard.putData("Field", field);
    }

    public void drive(double driverX, double driverY, double driverRotation, double operatorX, double operatorY, double speedScalar, boolean fieldOriented) {
        Translation2d driverTranslation = new Translation2d(driverY, driverX);

        Translation2d operatorTranslation = new Translation2d(0, operatorX/2);
        if (fieldOriented) {
            operatorTranslation = applyInverseFieldOriented(operatorTranslation);
        }

        Translation2d translation = driverTranslation.plus(operatorTranslation);
        if (translation.getNorm() < 0.075) {
            translation = Translation2d.kZero;
        }

        translation = translation.times(getMaximumVelocity());
        if (translation.getNorm() > getMaximumVelocity()) {
            translation.times(getMaximumVelocity() / translation.getNorm());
        }
        translation = translation.times(speedScalar);

        SmartDashboard.putNumber("Speed Scale", speedScalar);


        SmartDashboard.putNumber("F/B Intended Velosity", translation.getX());
        SmartDashboard.putNumber("L/R Intended Velosity", translation.getY());

        if (Math.abs(driverRotation) < 0.075) {
            driverRotation = 0;
        }
        double angularRotation = driverRotation * swerveDrive.getMaximumChassisAngularVelocity() * speedScalar;
        
        swerveDrive.drive(translation, angularRotation, fieldOriented, false);
    }

    public void drive(ChassisSpeeds speeds, DriveFeedforwards feedforward) {
        SmartDashboard.putNumber("X Intended Velosity", speeds.vxMetersPerSecond);
        SmartDashboard.putNumber("Y Intended Velosity", speeds.vyMetersPerSecond);
        swerveDrive.drive(speeds);
    }

    public void resetFieldOrientation() {
        
    }

    public void stop() {
        drive(0, 0, 0, 0, 0, 0, false);
    }

    private Translation2d applyInverseFieldOriented(Translation2d translation) {
        return translation.rotateBy(getRotation());
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

    public void resetOdometry(Pose2d pose) {
        //swerveDrive.setGyro(new Rotation3d(swerveDrive.getRoll().getMeasure(), swerveDrive.getPitch().getMeasure(), pose.getRotation().getMeasure()));
        System.out.println("test");
        swerveDrive.resetOdometry(pose);
    }

    @Override
    public void periodic() {
        field.setRobotPose(getPose());
    }
}
