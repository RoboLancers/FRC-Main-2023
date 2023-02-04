package frc.robot.commands;

import org.bananasamirite.robotmotionprofile.TankMotionProfile;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.ProfileMethod;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.TankMotionProfileConstraints;

import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.util.limelight.PoseEstimator;

public class GridAlign extends MotionProfileCommand {
    public GridAlign(Drivetrain drivetrain, PoseTracker poseTracker){
        super(
            drivetrain,
            new TankMotionProfile(
                // TODO: test inverse perspective projection
                // poseTracker.generateSpline(PoseEstimator.calculatePosition(drivetrain.getHeading() * Math.PI / 180))
                
                poseTracker.generateSpline(poseTracker.getAverageAprilPose()),
                ProfileMethod.TIME,
                new TankMotionProfileConstraints(1, 0.2)
            )
        );
    }
}
