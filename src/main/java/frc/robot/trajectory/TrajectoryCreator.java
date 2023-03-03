package frc.robot.trajectory;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.spline.Spline;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.commands.trajectory.TrajectoryCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import org.bananasamirite.robotmotionprofile.ParametricSpline;
import org.bananasamirite.robotmotionprofile.Waypoint;

public class TrajectoryCreator {
    private DifferentialDriveKinematics kinematics;
    private DifferentialDriveVoltageConstraint voltageConstraint;
    public TrajectoryCreator(DifferentialDriveKinematics kinematics, DifferentialDriveVoltageConstraint voltageConstraint) {
        this.kinematics = kinematics;
        this.voltageConstraint = voltageConstraint;
    }

    public Trajectory create(double maxVel, double maxAccel, Waypoint[] waypoints, boolean reversed) {
        return create(maxVel, maxAccel, ParametricSpline.fromWaypoints(waypoints, reversed));
    }

    public Trajectory create(double maxVel, double maxAccel, ParametricSpline spline) {

        TrajectoryConfig config = new TrajectoryConfig(maxVel, maxAccel).setKinematics(kinematics).addConstraint(voltageConstraint);;

        double totalTime = spline.getTotalTime();

        TrajectoryGenerator.ControlVectorList controlVectors = new TrajectoryGenerator.ControlVectorList();
        for (double i = 0; i < spline.getTotalTime(); i+= totalTime / 10) {
            controlVectors.add(new Spline.ControlVector(new double[] { // x
                spline.getXAtTime(i), spline.getDxAtTime(i), spline.getDdxAtTime(i)
            }, new double[] { // y
                spline.getYAtTime(i), spline.getDyAtTime(i), spline.getDdyAtTime(i)
            }));
        }
        return TrajectoryGenerator.generateTrajectory(controlVectors, config);
    }

    public TrajectoryCommand createCommand(Drivetrain drivetrain, ParametricSpline spline, double maxVel, double maxAccel) {
        return new TrajectoryCommand(drivetrain, create(maxVel, maxAccel, spline));
    }

    public TrajectoryCommand createCommand(Drivetrain drivetrain, Waypoint[] waypoints, double maxVel, double maxAccel, boolean reversed) {
        return new TrajectoryCommand(drivetrain, create(maxVel, maxAccel, waypoints, reversed));
    }
}
