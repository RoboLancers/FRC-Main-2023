package frc.robot.team254;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.List;


public class DriveMotionPlanner {
    RamseteController ramseteController;
    SimpleMotorFeedforward feedforward;
    DifferentialDriveKinematics kinematics;
    PIDController leftPID;
    PIDController rightPID;

    public DriveMotionPlanner() {
        this.ramseteController = new RamseteController(Constants.Trajectory.kRamseteB, Constants.Trajectory.kRamseteZeta);
        this.feedforward = new SimpleMotorFeedforward(Constants.Trajectory.ksVolts, Constants.Trajectory.ksVoltSecondsPerMeter, Constants.Trajectory.kaVoltSecondsSquaredPerMeter);
        this.kinematics = new DifferentialDriveKinematics(Constants.Trajectory.kTrackWidthMeters);
        this.leftPID = new PIDController(Constants.Trajectory.kPDriveVel, 0, 0);
        this.rightPID = new PIDController(Constants.Trajectory.kPDriveVel, 0, 0);
    }

    public Command generateTrajectory(
            Drivetrain drivetrain,
            boolean reversed,
            final List<Pose2d> waypoints,
            final List<TrajectoryConstraint> constraints
            ) {
        return generateTrajectory(drivetrain, reversed, waypoints, constraints, 0, 0);
    }

    public Command generateTrajectory(
        Drivetrain drivetrain,
        boolean reversed,
        final List<Pose2d> waypoints,
        final List<TrajectoryConstraint> constraints,
        double start_vel,
        double end_vel
        ) {

        TrajectoryConfig config = new TrajectoryConfig(
            Constants.Trajectory.kMaxSpeedMetersPerSecond, 
            Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared)
        .addConstraints(constraints)
        .setKinematics(kinematics)
        .setStartVelocity(start_vel)
        .setEndVelocity(end_vel)
        .setReversed(reversed);
        
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(waypoints, config);

        RamseteCommand ramseteCommand = new RamseteCommand(
            trajectory,
            drivetrain :: getPose,
            ramseteController,
            feedforward,
            kinematics,
            drivetrain :: getWheelSpeeds,
            leftPID,
            rightPID,
            drivetrain::tankDriveVolts,
            drivetrain
        );

        return ramseteCommand;
        }
    }
