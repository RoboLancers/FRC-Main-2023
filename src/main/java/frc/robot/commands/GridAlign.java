package frc.robot.commands;

import java.util.function.Supplier;

import org.bananasamirite.robotmotionprofile.TankMotionProfile;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.ProfileMethod;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.TankMotionProfileConstraints;

import edu.wpi.first.math.trajectory.constraint.MaxVelocityConstraint;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.util.enums.Displacement;

public class GridAlign extends MotionProfileCommand {

    public GridAlign(Drivetrain drivetrain, PoseTracker tracker, Supplier<Displacement> displacement) {
        this(drivetrain, tracker, 1.5, 0.6, displacement);
    }

    public GridAlign(Drivetrain drivetrain, PoseTracker poseTracker, double maxVel, double maxAccel, Supplier<Displacement> _displacement) {
        super(
                drivetrain,
                new TankMotionProfile(
                        poseTracker.generateSpline(_displacement.get()),
                        ProfileMethod.TIME,
                        new TankMotionProfileConstraints(maxVel, maxAccel)));
    }
}