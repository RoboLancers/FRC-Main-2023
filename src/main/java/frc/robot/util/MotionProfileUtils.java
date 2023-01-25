package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import org.bananasamirite.robotmotionprofile.TankMotionProfile;
import org.bananasamirite.robotmotionprofile.geometry.Position;

import java.util.stream.Collectors;

public class MotionProfileUtils {
    public static Trajectory profileToTrajectory(TankMotionProfile profile) {
        return new Trajectory(profile.getNodes().stream().map(e -> profileStateToTrajectoryState(e.asState())).collect(Collectors.toList()));
    }

    public static Trajectory.State profileStateToTrajectoryState(TankMotionProfile.MotionProfileState state) {
        return new Trajectory.State(state.getTotalTime(), state.getVelocity(), state.getAcceleration(), positionToPose2d(state.getPose()), state.getCurvature());
    }

    public static Pose2d positionToPose2d(Position position) {
        return new Pose2d(new Translation2d(position.getX(), position.getY()), new Rotation2d(position.getRotation()));
    }
}
