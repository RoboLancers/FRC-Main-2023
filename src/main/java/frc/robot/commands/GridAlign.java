package frc.robot.commands;

import edu.wpi.first.math.trajectory.TrajectoryConfig;
import frc.robot.Constants;
import frc.robot.commands.trajectory.TrajectoryCommand;

import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;

public class GridAlign extends TrajectoryCommand {

public GridAlign(Drivetrain drivetrain, PoseTracker tracker) {
    this(drivetrain, tracker, 1, 0.2); 
}

    public GridAlign(Drivetrain drivetrain, PoseTracker poseTracker, double maxVel, double maxAccel) {
        super(
            drivetrain,
                Constants.Trajectory.trajectoryCreator.create(poseTracker.generateWaypoints(), new TrajectoryConfig(maxVel, maxAccel).setReversed(false).setStartVelocity(drivetrain.getChassisSpeeds().vxMetersPerSecond))
        );
    }
}
