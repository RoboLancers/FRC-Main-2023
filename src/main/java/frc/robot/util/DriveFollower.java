package frc.robot.util;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class DriveFollower {
    private final PIDController ctrlLeft;
    private final PIDController ctrlRight;

    private final SimpleMotorFeedforward feedforward;

    private Drivetrain subsystem; 

    private final Timer timer; 

    private double prevTime; 

    private DifferentialDriveWheelSpeeds prevSpeeds; 

    public DriveFollower(Drivetrain subsystem) {
        this.subsystem = subsystem; 

        this.feedforward = new SimpleMotorFeedforward(Constants.Trajectory.ksVolts, Constants.Trajectory.ksVoltSecondsPerMeter, Constants.Trajectory.kaVoltSecondsSquaredPerMeter);

        this.ctrlLeft = new PIDController(Constants.Trajectory.kPDriveVel, 0, 0);
        this.ctrlRight = new PIDController(Constants.Trajectory.kPDriveVel, 0, 0);

        this.timer = new Timer(); 

    }

    // should be called everytime 
    public void reset() {
        timer.reset(); 
        timer.start();

        prevTime = timer.get(); 

        prevSpeeds = subsystem.getWheelSpeeds(); 
    }

    public Voltage calculate(ChassisSpeeds chassisSpeeds) {
        return calculate(Constants.Trajectory.kDriveKinematics.toWheelSpeeds(chassisSpeeds)); 
    }


    public Voltage calculate(DifferentialDriveWheelSpeeds wheelSpeeds) {

        double curTime = timer.get();

        double dt = curTime - prevTime;
        
        double leftSpeedSetpoint = wheelSpeeds.leftMetersPerSecond;
        double rightSpeedSetpoint = wheelSpeeds.rightMetersPerSecond;

        double leftFeedforward = feedforward.calculate(leftSpeedSetpoint, (leftSpeedSetpoint - prevSpeeds.leftMetersPerSecond) / dt);
        double rightFeedforward = feedforward.calculate(rightSpeedSetpoint, (rightSpeedSetpoint - prevSpeeds.rightMetersPerSecond) / dt);

        double leftOutput = leftFeedforward + ctrlLeft.calculate(subsystem.getWheelSpeeds().leftMetersPerSecond, leftSpeedSetpoint);
        double rightOutput = rightFeedforward + ctrlRight.calculate(subsystem.getWheelSpeeds().rightMetersPerSecond, rightSpeedSetpoint);

        prevSpeeds = subsystem.getWheelSpeeds(); 

        return new Voltage(leftOutput, rightOutput); 
    }
    
    
    public static class Voltage {
        private double left; 
        private double right; 

        public Voltage(double left, double right) {
            this.left = left; 
            this.right = right; 
        }

        public double getLeft() {
            return left; 
        }

        public double getRight() {
            return right; 
        }
    }
}
