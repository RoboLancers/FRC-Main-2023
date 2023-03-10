package frc.robot.trajectory;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.spline.Spline;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Constants;
import frc.robot.commands.trajectory.TrajectoryCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;

import java.util.List;

import org.bananasamirite.robotmotionprofile.ParametricSpline;
import org.bananasamirite.robotmotionprofile.Waypoint;

public class TrajectoryCreator {
    private DifferentialDriveKinematics kinematics;
    private DifferentialDriveVoltageConstraint voltageConstraint;
    public TrajectoryCreator(DifferentialDriveKinematics kinematics, DifferentialDriveVoltageConstraint voltageConstraint) {
        this.kinematics = kinematics;
        this.voltageConstraint = voltageConstraint;
    }

    public Trajectory create(List<Waypoint> waypoints, TrajectoryConfig config) {

        config.addConstraint(voltageConstraint); 

        TrajectoryGenerator.ControlVectorList controlVectors = new TrajectoryGenerator.ControlVectorList();
        for (Waypoint w : waypoints) {
            controlVectors.add(new Spline.ControlVector(new double[] { // x
                w.getX(), w.getWeight()*Math.cos(w.getAngle()), 0
            }, new double[] { // y
                w.getY(), w.getWeight()*Math.sin(w.getAngle()), 0
            }));
        }
        return TrajectoryGenerator.generateTrajectory(controlVectors, config);
    }

    public Trajectory create(Waypoint[] waypoints, TrajectoryConfig config) {
        return create(List.of(waypoints), config); 
    }

    public TrajectoryCommand createCommand(Drivetrain drivetrain, List<Waypoint> waypoints, TrajectoryConfig config) {
                // Trajectory t = create(maxVel, maxAccel, waypoints, reversed);

                // TrajectoryConfig config = new TrajectoryConfig(maxVel, maxAccel).setKinematics(kinematics).addConstraint(voltageConstraint);
                // Trajectory t = TrajectoryGenerator.generateTrajectory(new Pose2d(), List.of(new Translation2d(1, 1)), new Pose2d(new Translation2d(2, 1), new Rotation2d()), config); 
                // return new RamseteCommand(t,  drivetrain::getPose, new RamseteController(), new SimpleMotorFeedforward(Constants.Trajectory.ksVolts, Constants.Trajectory.ksVoltSecondsPerMeter, Constants.Trajectory.kaVoltSecondsSquaredPerMeter), () -> drivetrain.getWheelSpeeds(), new PIDController(Constants.Trajectory.kPDriveVel, 0, 0), new PIDController(Constants.Trajectory.kPDriveVel, 0, 0), (a, b) -> drivetrain.tankDriveVolts(a, b), drivetrain); 
        
                // return new RamseteCommand(
                //     t,
                //     drivetrain::getPose,
                //     new RamseteController(),
                //     new SimpleMotorFeedforward(
                //         Constants.Trajectory.ksVolts,
                //         Constants.Trajectory.ksVoltSecondsPerMeter,
                //         Constants.Trajectory.kaVoltSecondsSquaredPerMeter),
                //         Constants.Trajectory.kDriveKinematics,
                //     drivetrain::getWheelSpeeds,
                //     new PIDController(Constants.Trajectory.kPDriveVel, 0, 0),
                //     new PIDController(Constants.Trajectory.kPDriveVel, 0, 0),
                //     // RamseteCommand passes volts to the callback
                //     drivetrain::tankDriveVolts,
                //     drivetrain);
                return new TrajectoryCommand(drivetrain, create(waypoints, config));
    }

    public TrajectoryCommand createCommand(Drivetrain drivetrain, Waypoint[] waypoints, TrajectoryConfig config) {
        return createCommand(drivetrain, List.of(waypoints), config); 
    }
}
