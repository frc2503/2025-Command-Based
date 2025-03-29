package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class ClimberSubsystem extends SubsystemBase {

    private final SparkMax climber;
    private final RelativeEncoder encoder;
    private final Servo servo;
    private final Timer timer;

    public ClimberSubsystem() {
        climber = new SparkMax(MotorConstants.CLIMBER, MotorType.kBrushless);
        encoder = climber.getEncoder();
        servo = new Servo(0);
        servo.set(1);
        timer = new Timer();

    }

    public void timerStart() {
        timer.reset();
        timer.start();
    }

    public void climberOut() {
        if (encoder.getPosition() > -53) {
            if(timer.get() < 0.5) {
                servo.set(.7);
            } else if(timer.get() > 0.5) {
                climber.set(-1); 
            }
        } else {
            climberStop();
        }
    }
    //extends the climber outside of the robot to grab the cage

    public void climberIn() {
        if (encoder.getPosition() < 0) {
            servo.set(1);
            climber.set(1);
        } else {
            climberStop();
        }
    }
    //Pulls the climber back into the robot completing the climb

    public void climberStop() {
        timer.stop();
        climber.set(0);
        servo.set(1);
    }
    //Stops the motor and resets the servo to re-engage the ratchet
}
