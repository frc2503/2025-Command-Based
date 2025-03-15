package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorConstants;

public class ClimberSubsystem extends SubsystemBase {

    private SparkMax climber;
    private Servo servo;
    private Timer timer;

    public ClimberSubsystem() {

        climber = new SparkMax(MotorConstants.CLIMBER, MotorType.kBrushless);
        servo = new Servo(0);
        timer = new Timer();

    }

    public void servoInit() {
        servo.setAngle(45);
    }

    public void timerStart() {
        timer.reset();
        timer.start();
    }

    public void climberOut() {
        if(timer.get() < 1) {
            servo.setAngle(0); //TODO find angle needed
        } else if(timer.get() > 1 ) {
            climber.set(-.1); //TODO find positive or negative direction
        }
    }
    //extends the climber outside of the robot to grab the cage

    public void climberIn() {
        climber.set(.5); //TODO find positive or negative direction
    }
    //Pulls the climber back into the robot completing the climb

    public void climberStop() {
        timer.stop();
        climber.set(0);
        servo.setAngle(45);
        
    }
    //Stops the motor and resets the servo to re-engage the ratchet
}
