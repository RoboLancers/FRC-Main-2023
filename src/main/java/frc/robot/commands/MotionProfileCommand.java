// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.MotionProfileUtils;
import org.bananasamirite.robotmotionprofile.TankMotionProfile;

/** An example command that uses an example subsystem. */
public class MotionProfileCommand extends CommandBase
{
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final Drivetrain subsystem;
    private final RamseteController ramseteController;

    private final Timer timer;

    private final TankMotionProfile motionProfile;
    private final double maxTime;

    private double startTime;
    private double prevTime;

    private final PIDController ctrlLeft;
    private final PIDController ctrlRight;
    private DifferentialDriveWheelSpeeds prevSpeeds;

    private final SimpleMotorFeedforward feedforward;

    private final NetworkTableEntry velocity;
    private final NetworkTableEntry robotVelocity;

    /**
     * Creates a new ExampleCommand.
     *
     * @param drivetrain The subsystem used by this command.
     */
    public MotionProfileCommand(Drivetrain drivetrain, TankMotionProfile motionProfile)
    {
        this.subsystem = drivetrain;
        this.timer = new Timer();
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(drivetrain);

        this.motionProfile = motionProfile;
        this.maxTime = motionProfile.getTotalTime();

        this.ramseteController = new RamseteController();

        this.feedforward = new SimpleMotorFeedforward(Constants.Trajectory.ksVolts, Constants.Trajectory.ksVoltSecondsPerMeter, Constants.Trajectory.kaVoltSecondsSquaredPerMeter);

        this.ctrlLeft = new PIDController(Constants.Trajectory.kPDriveVel, 0, 0);
        this.ctrlRight = new PIDController(Constants.Trajectory.kPDriveVel, 0, 0);

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        this.velocity = inst.getEntry("velocity");
        this.robotVelocity = inst.getEntry("robotVelocity");
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        prevTime = -1;
        timer.reset();
        timer.start();

        Trajectory.State initialState = MotionProfileUtils.profileStateToTrajectoryState(this.motionProfile.getStateAtTime(0));
        
        prevSpeeds = Constants.Trajectory.kDriveKinematics.toWheelSpeeds(
                new ChassisSpeeds(
                        initialState.velocityMetersPerSecond,
                        0,
                        initialState.curvatureRadPerMeter * initialState.velocityMetersPerSecond
                )
        );

        subsystem.resetOdometry(initialState.poseMeters);

        System.out.println("starting motion profile");
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double curTime = timer.get();

        double dt = curTime - prevTime;

        Trajectory.State state = MotionProfileUtils.profileStateToTrajectoryState(this.motionProfile.getStateAtTime(curTime));

        this.velocity.setDouble(state.velocityMetersPerSecond);
        this.robotVelocity.setDouble(Constants.Trajectory.kDriveKinematics.toChassisSpeeds(subsystem.getWheelSpeeds()).vxMetersPerSecond);

        if (prevTime < 0) {
            subsystem.tankDriveVolts(0, 0);
            prevTime = curTime;
            return;
        }

        ChassisSpeeds speeds = ramseteController.calculate(subsystem.getPose(), state);

        DifferentialDriveWheelSpeeds wheelSpeeds = Constants.Trajectory.kDriveKinematics.toWheelSpeeds(speeds);


        double leftSpeedSetpoint = wheelSpeeds.leftMetersPerSecond;
        double rightSpeedSetpoint = wheelSpeeds.rightMetersPerSecond;

        double leftFeedforward = feedforward.calculate(leftSpeedSetpoint, (leftSpeedSetpoint - prevSpeeds.leftMetersPerSecond) / dt);
        double rightFeedforward = feedforward.calculate(rightSpeedSetpoint, (rightSpeedSetpoint - prevSpeeds.rightMetersPerSecond) / dt);

        double leftOutput = leftFeedforward + ctrlLeft.calculate(subsystem.getWheelSpeeds().leftMetersPerSecond, leftSpeedSetpoint);
        double rightOutput = rightFeedforward + ctrlRight.calculate(subsystem.getWheelSpeeds().rightMetersPerSecond, rightSpeedSetpoint);

        this.subsystem.tankDriveVolts(leftOutput, rightOutput);

        prevSpeeds = wheelSpeeds;
        prevTime = curTime;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return timer.hasElapsed(maxTime);
    }
}
