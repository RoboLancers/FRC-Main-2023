package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.commands.trajectory.TrajectoryCommand;

import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;

public class GridAlign extends TrajectoryCommand {

public GridAlign(Drivetrain drivetrain, PoseTracker tracker) {
    this(drivetrain, tracker, 1.5, 0.6); 
}

    public GridAlign(Drivetrain drivetrain, PoseTracker poseTracker, double maxVel, double maxAccel) {
        super(
            drivetrain,
                Constants.Trajectory.trajectoryCreator.create(maxVel, maxAccel, poseTracker.generateSpline())
        );
    }
}
