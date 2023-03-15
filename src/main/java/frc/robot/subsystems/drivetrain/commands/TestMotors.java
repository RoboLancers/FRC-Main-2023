package frc.robot.subsystems.drivetrain.commands;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.util.Encoder;

public class TestMotors extends CommandBase {
    private final CANSparkMax leftMotor1 = new CANSparkMax(Constants.Drivetrain.LeftMotors.kLeftMotor1_Port, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax leftMotor2 = new CANSparkMax(Constants.Drivetrain.LeftMotors.kLeftMotor2_Port, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax leftMotor3 = new CANSparkMax(Constants.Drivetrain.LeftMotors.kLeftMotor3_Port, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final CANSparkMax rightMotor1 = new CANSparkMax(Constants.Drivetrain.RightMotors.kRightMotor1_Port, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rightMotor2 = new CANSparkMax(Constants.Drivetrain.RightMotors.kRightMotor2_Port, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rightMotor3 = new CANSparkMax(Constants.Drivetrain.RightMotors.kRightMotor3_Port, CANSparkMaxLowLevel.MotorType.kBrushless);

    private final Encoder rightEncoder = new Encoder(rightMotor1.getEncoder());
    private final Encoder leftEncoder = new Encoder(leftMotor1.getEncoder());

    private final AHRS gyro = new AHRS(SPI.Port.kMXP);

    private final DifferentialDriveOdometry odometry;

    public TestMotors() {
        rightMotor1.setInverted(true);
        rightMotor2.setInverted(true);
        rightMotor3.setInverted(true);

        leftMotor1.setInverted(false);
        leftMotor2.setInverted(false);
        leftMotor3.setInverted(false);

        leftMotor1.setIdleMode(CANSparkMax.IdleMode.kBrake);
        leftMotor2.setIdleMode(CANSparkMax.IdleMode.kCoast);
        leftMotor3.setIdleMode(CANSparkMax.IdleMode.kBrake);

        rightMotor1.setIdleMode(CANSparkMax.IdleMode.kBrake);
        rightMotor2.setIdleMode(CANSparkMax.IdleMode.kCoast);
        rightMotor3.setIdleMode(CANSparkMax.IdleMode.kBrake);

        leftMotor3.follow(leftMotor1); // may need true here? 
        rightMotor3.follow(rightMotor1); // may need true here? 
        
        leftMotor1.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps, Constants.Drivetrain.kMaxFreeAmps);
        leftMotor2.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps, Constants.Drivetrain.kMaxFreeAmps);
        leftMotor3.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps, Constants.Drivetrain.kMaxFreeAmps);
        rightMotor1.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps, Constants.Drivetrain.kMaxFreeAmps);
        rightMotor2.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps, Constants.Drivetrain.kMaxFreeAmps);
        rightMotor3.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps, Constants.Drivetrain.kMaxFreeAmps);

        leftMotor1.enableVoltageCompensation(12);
        leftMotor2.enableVoltageCompensation(12);
        leftMotor3.enableVoltageCompensation(12);
        rightMotor1.enableVoltageCompensation(12);
        rightMotor2.enableVoltageCompensation(12);
        rightMotor3.enableVoltageCompensation(12);

        // Sets the distance per pulse to the pre-defined constant we calculated for both encoders.
        rightEncoder.getEncoder().setPositionConversionFactor(Constants.Trajectory.kMetersPerRot);
        leftEncoder.getEncoder().setPositionConversionFactor(Constants.Trajectory.kMetersPerRot);

        leftEncoder.getEncoder().setVelocityConversionFactor(Constants.Trajectory.kMetersPerSecondPerRPM);
        rightEncoder.getEncoder().setVelocityConversionFactor(Constants.Trajectory.kMetersPerSecondPerRPM);

        resetEncoders();

        odometry = new DifferentialDriveOdometry(gyro.getRotation2d(), leftEncoder.getPosition(), rightEncoder.getPosition());
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        odometry.update(gyro.getRotation2d(), leftEncoder.getPosition(), rightEncoder.getPosition());
        leftMotor1.setVoltage(-2);
        rightMotor1.setVoltage(2);
//        rightMotor1.set(0.5);
//        leftMotor1.set(-0.5);
//        rightMotor1.set(-0.5);
        SmartDashboard.putNumber("left encoder", leftEncoder.getPosition());
        SmartDashboard.putNumber("right encoder", rightEncoder.getPosition());
        SmartDashboard.putNumber("x", odometry.getPoseMeters().getX());
        SmartDashboard.putNumber("y", odometry.getPoseMeters().getY());
        SmartDashboard.putNumber("rotation", odometry.getPoseMeters().getRotation().getDegrees());
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }
}
