package frc.robot.commands.trajectory;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class TrajectoryCommand extends CommandBase
{
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final Drivetrain subsystem;
    private final RamseteController ramseteController;

    private final Timer timer;

    private final Trajectory trajectory;
    private final double maxTime;

    private double startTime;
    private double prevTime;

    private final PIDController ctrlLeft;
    private final PIDController ctrlRight;
    private DifferentialDriveWheelSpeeds prevSpeeds;

    private final SimpleMotorFeedforward feedforward;

    private final NetworkTableEntry velocity;
    private final NetworkTableEntry robotVelocity;

    public TrajectoryCommand(Drivetrain drivetrain, Trajectory trajectory)
    {
        this.subsystem = drivetrain;
        this.timer = new Timer();
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(drivetrain);

        this.trajectory = trajectory;
        this.maxTime = trajectory.getTotalTimeSeconds();

        this.ramseteController = new RamseteController();

        this.feedforward = new SimpleMotorFeedforward(Constants.Trajectory.ksVolts, Constants.Trajectory.ksVoltSecondsPerMeter, Constants.Trajectory.kaVoltSecondsSquaredPerMeter);

        this.ctrlLeft = new PIDController(Constants.Trajectory.kPDriveVel, 0, 0);
        this.ctrlRight = new PIDController(Constants.Trajectory.kPDriveVel, 0, 0);

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        this.velocity = inst.getEntry("velocity");
        this.robotVelocity = inst.getEntry("robotVelocity");

        subsystem.getField().getObject("robot").setTrajectory(trajectory);
    }

    @Override
    public void initialize() {
        prevTime = -1;
        timer.reset();
        timer.start();

        Trajectory.State initialState = this.trajectory.sample(0);
        
        prevSpeeds = Constants.Trajectory.kDriveKinematics.toWheelSpeeds(
                new ChassisSpeeds(
                        initialState.velocityMetersPerSecond,
                        0,
                        initialState.curvatureRadPerMeter * initialState.velocityMetersPerSecond
                )
        );

        subsystem.resetOdometry(initialState.poseMeters);
    }

    @Override
    public void execute() {
        double curTime = timer.get();

        double dt = curTime - prevTime;

        Trajectory.State state = this.trajectory.sample(curTime);

        this.velocity.setDouble(state.velocityMetersPerSecond);
        this.robotVelocity.setDouble(Constants.Trajectory.kDriveKinematics.toChassisSpeeds(subsystem.getWheelSpeeds()).vxMetersPerSecond);
        Pose2d robotPose = subsystem.getPose(); 

        SmartDashboard.putNumber("expectedX", state.poseMeters.getX()); 
        SmartDashboard.putNumber("robotX", robotPose.getX()); 
        SmartDashboard.putNumber("expectedY", state.poseMeters.getY()); 
        SmartDashboard.putNumber("robotY", robotPose.getY()); 
        SmartDashboard.putNumber("expectedTheta", state.poseMeters.getRotation().getDegrees()); 
        SmartDashboard.putNumber("robotTheta", robotPose.getRotation().getDegrees()); 

        if (prevTime < 0) {
            subsystem.tankDriveVolts(0, 0);
            prevTime = curTime;
            return;
        }

        ChassisSpeeds speeds = ramseteController.calculate(subsystem.getPose(), state);

        DifferentialDriveWheelSpeeds wheelSpeeds = Constants.Trajectory.kDriveKinematics.toWheelSpeeds(speeds);


        double leftSpeedSetpoint = wheelSpeeds.leftMetersPerSecond;
        double rightSpeedSetpoint = wheelSpeeds.rightMetersPerSecond;

        SmartDashboard.putNumber("expectedLeftSpeed", leftSpeedSetpoint); 
        SmartDashboard.putNumber("robotLeftSpeed", subsystem.getWheelSpeeds().leftMetersPerSecond); 
        SmartDashboard.putNumber("expectedRightSpeed", rightSpeedSetpoint); 
        SmartDashboard.putNumber("robotRightSpeed", subsystem.getWheelSpeeds().leftMetersPerSecond); 

        double leftFeedforward = feedforward.calculate(leftSpeedSetpoint, (leftSpeedSetpoint - prevSpeeds.leftMetersPerSecond) / dt);
        double rightFeedforward = feedforward.calculate(rightSpeedSetpoint, (rightSpeedSetpoint - prevSpeeds.rightMetersPerSecond) / dt);

        double leftOutput = leftFeedforward + ctrlLeft.calculate(subsystem.getWheelSpeeds().leftMetersPerSecond, leftSpeedSetpoint);
        double rightOutput = rightFeedforward + ctrlRight.calculate(subsystem.getWheelSpeeds().rightMetersPerSecond, rightSpeedSetpoint);

        SmartDashboard.putNumber("expectedLeftVoltage", leftOutput); 
        SmartDashboard.putNumber("robotLeftVoltage", subsystem.getLeftVoltage()); 
        SmartDashboard.putNumber("expectedRightVoltage", rightOutput); 
        SmartDashboard.putNumber("robotRightVoltage", subsystem.getRightVoltage()); 

        this.subsystem.tankDriveVolts(leftOutput, rightOutput);

        prevSpeeds = wheelSpeeds;
        prevTime = curTime;
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished()
    {
        return timer.hasElapsed(maxTime);
    }
}
