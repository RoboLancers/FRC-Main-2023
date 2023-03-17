package frc.robot.subsystems.drivetrain;

import java.util.function.Consumer;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.DriveFollower;
import frc.robot.util.Encoder;
import frc.robot.util.enums.Displacement;
import frc.robot.util.limelight.LimelightAPI;
import edu.wpi.first.wpilibj.SPI;

public class Drivetrain extends SubsystemBase {
    private final CANSparkMax leftMotor1 = new CANSparkMax(Constants.Drivetrain.LeftMotors.kLeftMotor1_Port, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax leftMotor2 = new CANSparkMax(Constants.Drivetrain.LeftMotors.kLeftMotor2_Port, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax leftMotor3 = new CANSparkMax(Constants.Drivetrain.LeftMotors.kLeftMotor3_Port, CANSparkMaxLowLevel.MotorType.kBrushless);

    public final MotorControllerGroup leftMotors = new MotorControllerGroup(
            leftMotor1, 
            // leftMotor2, 
            leftMotor3
    );

    private final CANSparkMax rightMotor1 = new CANSparkMax(Constants.Drivetrain.RightMotors.kRightMotor1_Port, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rightMotor2 = new CANSparkMax(Constants.Drivetrain.RightMotors.kRightMotor2_Port, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rightMotor3 = new CANSparkMax(Constants.Drivetrain.RightMotors.kRightMotor3_Port, CANSparkMaxLowLevel.MotorType.kBrushless);

    public final MotorControllerGroup rightMotors = new MotorControllerGroup(
            rightMotor1, 
            // rightMotor2, 
            rightMotor3
    );

    private final DifferentialDrive difDrive = new DifferentialDrive(leftMotors, rightMotors);

    private final DifferentialDriveOdometry odometry;

    // reverse the encoders to match the reversed motors of the right side.
    private final Encoder rightEncoder = new Encoder(rightMotor1.getEncoder());
    private final Encoder leftEncoder = new Encoder(leftMotor1.getEncoder());

    private final AHRS gyro = new AHRS(SPI.Port.kMXP);

    private final Field2d m_field = new Field2d();

    private final DriveFollower driveFollower; 

    public boolean isAutoSteer = false; 

    public Drivetrain() {
        // inversions
        rightMotor1.setInverted(true);
        rightMotor2.setInverted(true);
        rightMotor3.setInverted(true);

        leftMotor1.setInverted(false);
        leftMotor2.setInverted(false);
        leftMotor3.setInverted(false);

        // brake/coast
        leftMotor1.setIdleMode(IdleMode.kBrake);
        leftMotor2.setIdleMode(IdleMode.kCoast);
        leftMotor3.setIdleMode(IdleMode.kBrake);

        rightMotor1.setIdleMode(IdleMode.kBrake);
        rightMotor2.setIdleMode(IdleMode.kCoast);
        rightMotor3.setIdleMode(IdleMode.kBrake);

        // current & voltage limits
        // configureMotors((m) -> {
        //     // m.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps); 
        //     m.enableVoltageCompensation(12); 
        // });

        leftMotor1.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps); 
        leftMotor2.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps); 
        leftMotor3.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps); 
        rightMotor1.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps); 
        rightMotor2.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps); 
        rightMotor3.setSmartCurrentLimit(Constants.Drivetrain.kMaxStallAmps); 

        leftMotor1.enableVoltageCompensation(12); 
        leftMotor2.enableVoltageCompensation(12); 
        leftMotor3.enableVoltageCompensation(12); 
        rightMotor1.enableVoltageCompensation(12); 
        rightMotor2.enableVoltageCompensation(12);  
        rightMotor3.enableVoltageCompensation(12); 

        rightEncoder.getEncoder().setPositionConversionFactor(Constants.Trajectory.kMetersPerRot);
        leftEncoder.getEncoder().setPositionConversionFactor(Constants.Trajectory.kMetersPerRot);

        leftEncoder.getEncoder().setVelocityConversionFactor(Constants.Trajectory.kMetersPerSecondPerRPM);
        rightEncoder.getEncoder().setVelocityConversionFactor(Constants.Trajectory.kMetersPerSecondPerRPM);

        resetEncoders();

        odometry = new DifferentialDriveOdometry(gyro.getRotation2d(), leftEncoder.getPosition(), rightEncoder.getPosition());

        this.driveFollower = new DriveFollower(this); 

        //initDefaultCommand(driverController);
    }

    public void configureMotors(Consumer<CANSparkMax> func) {
        func.accept(leftMotor1);
        func.accept(leftMotor2);
        func.accept(leftMotor3);
        func.accept(rightMotor1);
        func.accept(rightMotor2);
        func.accept(rightMotor3);
    }

    // Constantly updates the odometry of the robot with the rotation and the distance traveled.
    @Override
    public void periodic() {
        odometry.update(gyro.getRotation2d(), leftEncoder.getPosition(), rightEncoder.getPosition());
        m_field.setRobotPose(odometry.getPoseMeters());
        SmartDashboard.putData("field", m_field);
        SmartDashboard.putNumber("x", odometry.getPoseMeters().getX());
        SmartDashboard.putNumber("y", odometry.getPoseMeters().getY());
        SmartDashboard.putNumber("rotation", odometry.getPoseMeters().getRotation().getDegrees());
        SmartDashboard.putNumber("encoderLeft", leftEncoder.getPosition());
        SmartDashboard.putNumber("encoderRight", rightEncoder.getPosition());
        SmartDashboard.putNumber("leftSpeed", leftMotor1.get());
        SmartDashboard.putNumber("rightSpeed", rightMotor1.get()); 
    }

    // Returns the pose of the robot.
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    // Returns the current speed of the wheels of the robot.
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(leftEncoder.getEncoder().getVelocity(), rightEncoder.getEncoder().getVelocity());
    }

    public ChassisSpeeds getChassisSpeeds() {
        return Constants.Trajectory.kDriveKinematics.toChassisSpeeds(getWheelSpeeds()); 
    }

    // Resets the odometry, both rotation and distance traveled.
    public void resetOdometry(Pose2d pose) {
        gyro.zeroYaw();
        resetEncoders();
        odometry.resetPosition(gyro.getRotation2d(), leftEncoder.getPosition(), rightEncoder.getPosition(), pose);
    }

    public void setDrivePower(double power) {
        leftMotors.set(power);
        rightMotors.set(power);
    }

    public void rawArcadeDrive(double throttle, double turn, boolean squareInputs) {
        difDrive.arcadeDrive(throttle, turn, squareInputs);
    }

    // Drives the robot with arcade controls.
    public void arcadeDrive(double throttle, double turn) {
        SmartDashboard.putBoolean("is quickturning", Math.abs(throttle) < 0.05);
        SmartDashboard.putNumber("turn", turn); 

        difDrive.arcadeDrive(throttle, turn, false);
    }

    public void autoSteerArcadeDrive(double throttle, Pose2d aprilTagPose) { // aprilTagPose = pose relative to robot
        double turnPower = aprilTagPose.getY() * Constants.GridAlign.kSteer * (throttle != 0 ? throttle : 0.25);

        arcadeDrive(throttle, turnPower);

        // double curvature = ParametricSpline.fromWaypoints(new Waypoint[] {
        //     new Waypoint(0, 0, 0, 1, 1), 
        //     new Waypoint(aprilTagPose.getX(), aprilTagPose.getY(), aprilTagPose.getRotation().getDegrees(), 1, 1)
        // }).signedCurvatureAt(0); 

        // double velocity = throttle * RobotDriveBase.kDefaultMaxOutput / Constants.Trajectory.ksVoltSecondsPerMeter; 
        
        // ChassisSpeeds newSpeeds = new ChassisSpeeds(velocity, 0, velocity * curvature); 
        // Voltage voltages = driveFollower.calculate(newSpeeds); 
        // tankDriveVolts(voltages.getLeft(), voltages.getRight());
    }

    public void drive(double throttle, double turn) {
        Pose2d pose = LimelightAPI.adjustCamPose(Displacement.kCenter); 

        if (isAutoSteer && pose != null) {
            autoSteerArcadeDrive(throttle, pose);
        } else {
            arcadeDrive(throttle, turn);
        }
    }

    // Controls the left and right side motors directly with voltage.
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        leftMotors.setVoltage(leftVolts);
        rightMotors.setVoltage(rightVolts);
        difDrive.feed();
    }

    // Resets the record values of both sides of encoders.
    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    // These methods are never used?
    // Returns the average of the distances between both sides of encoders.
    public double getAverageEncoderDistance() {
        return (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2.0;
    }

    // Returns the left encoders.
    public RelativeEncoder getLeftEncoder() {
        return leftEncoder.getEncoder();
    }

    // Returns the right encoders.
    public RelativeEncoder getRightEncoder() {
        return rightEncoder.getEncoder();
    }

    // Sets the max output of the drive. Used for scaling the drive to drive more slowly.
    public void setMaxOutput(double maxOutPut) {
        difDrive.setMaxOutput(maxOutPut);
    }

    public double getLeftVoltage() {
        return leftMotors.get();
    }

    public double getRightVoltage() {
        return rightMotors.get();
    }

    // Sets the recorded heading to 0. Makes new direction the 0 heading.
    public void zeroHeading() {
        gyro.reset();
    }

    // Returns the direction the robot is facing in degrees from -180 to 180 degrees.
    public double getHeading() {
        return gyro.getRotation2d().getDegrees();
    }

    // Returns the rate at which the robot is turning in degrees per second.
    public double getTurnRate() {
        return -gyro.getRate();
    }

    public Field2d getField() {
        return this.m_field; 
    }
    
    public DriveFollower getDriveFollower() {
        return driveFollower; 
    }
}
