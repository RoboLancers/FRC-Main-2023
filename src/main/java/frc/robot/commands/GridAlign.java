package frc.robot.commands;

import org.bananasamirite.robotmotionprofile.TankMotionProfile;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.ProfileMethod;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.TankMotionProfileConstraints;

import frc.robot.commands.trajectory.MotionProfileCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;

public class GridAlign extends MotionProfileCommand {
    public GridAlign(Drivetrain drivetrain, PoseTracker tracker) {
        this(drivetrain, tracker, 1.5, 0.6);
    }

    public GridAlign(Drivetrain drivetrain, PoseTracker poseTracker, double maxVel, double maxAccel) {
        super(
            drivetrain,
            new TankMotionProfile(
                poseTracker.generateSpline(),
                ProfileMethod.TIME,
                new TankMotionProfileConstraints(maxVel, maxAccel)
            )
        );
    }
}