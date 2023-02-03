package frc.robot.subsystems.gridalign.commands;

import org.bananasamirite.robotmotionprofile.TankMotionProfile;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.ProfileMethod;

import frc.robot.commands.MotionProfileCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.gridalign.PoseTracker;

public class Align extends MotionProfileCommand {
    public Align(Drivetrain drivetrain, PoseTracker tracker){
        super(
            drivetrain,
            new TankMotionProfile(
                PoseTracker.generateSpline(tracker::getAverageAprilPose, drivetrain), ProfileMethod.TIME, new TankMotionProfile.TankMotionProfileConstraints(1, 0.2))
        );
    }
}
