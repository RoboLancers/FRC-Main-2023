package frc.robot.team1678;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.math.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.team254.DriveMotionPlanner;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class PathWeaverTrajectory {

    //TODO make actual paths, this format is just for reference on how it should be used
    private double max_vel = Constants.Trajectory.kMaxSpeedMetersPerSecond;
    private double max_accel = Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared;
    private Drivetrain drivetrain;

    String file_path_forward = "paths/Forward.wpilib.json";
    String flie_path_SplineForward = "paths/Spline Forward.wpilib.json";

    private Trajectory path_forward;
    private Trajectory path_SplineForward;

    public PathWeaverTrajectory(){
        this.drivetrain = new Drivetrain();

        this.path_forward = AutoTrajectoryReader.generateTrajectoryFromFile(file_path_forward, 
        createConfig(max_vel, max_accel));

        this.path_SplineForward = AutoTrajectoryReader.generateTrajectoryFromFile(flie_path_SplineForward,
        createConfig(max_vel, max_accel));

    }

    public TrajectoryConfig createConfig(double max_vel, double max_accel){
        return createConfig(max_vel, max_accel, 0, 0);
    }

    public TrajectoryConfig createConfig(double max_vel, double max_accel, double start_vel, double end_vel){
        TrajectoryConfig config = new TrajectoryConfig(max_vel, max_accel);
        config.setKinematics(Constants.Trajectory.kDriveKinematics);
        config.setStartVelocity(start_vel);
        config.setEndVelocity(end_vel);
        config.addConstraint(Constants.Trajectory.centripetalAccelerationConstraint);
        config.addConstraint(
            new DifferentialDriveVoltageConstraint(
            Constants.Trajectory.driveFeedforward,
            Constants.Trajectory.kDriveKinematics,
            Constants.Trajectory.kMaxVoltage)
        );
        return config;
    }

    public Command setTrajectory(Trajectory trajectory, Drivetrain drivetrain){
        return new RamseteCommand(
            trajectory,
            drivetrain :: getPose,
            Constants.Trajectory.ramseteController,
            Constants.Trajectory.driveFeedforward,
            Constants.Trajectory.kDriveKinematics,
            drivetrain :: getWheelSpeeds,
            Constants.Trajectory.leftPID,
            Constants.Trajectory.rightPID,
            drivetrain::tankDriveVolts,
            drivetrain
        );
    }

    public Command get_path_forward(){
        return setTrajectory(path_forward, drivetrain);
    }

    public Command get_path_SplineForward(){
        return setTrajectory(path_SplineForward, drivetrain);
    }

}
