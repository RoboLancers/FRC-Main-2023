package frc.robot.subsystems.drivetrain.commands;

import java.util.List;

import org.bananasamirite.robotmotionprofile.Waypoint;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import frc.robot.Constants;
import frc.robot.commands.trajectory.TrajectoryCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class MoveBackward extends TrajectoryCommand {

    public MoveBackward(Drivetrain drivetrain, double distanceMeters, double maxVel, double maxAccel) {
        super(drivetrain, Constants.Trajectory.trajectoryCreator.create(List.of(new Waypoint(0, 0, 0, 1, 1), new Waypoint(-distanceMeters, 0, 0, 1, 1)), new TrajectoryConfig(maxVel, maxAccel).setReversed(true)));
    }
    
    public MoveBackward(Drivetrain drivetrain, double distanceMeters) {
        this(drivetrain, distanceMeters, Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared); 
    }
}
