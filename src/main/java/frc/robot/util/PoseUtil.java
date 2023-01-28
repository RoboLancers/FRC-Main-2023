package frc.robot.util;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.util.sendable.Sendable;

public class PoseUtil {
    public static Pose3d averagePoses(ArrayList<Pose3d> poses) {
        Translation3d avgTranslation = poses.stream()
            .map(pose -> pose.getTranslation())
            .reduce(new Translation3d(), (prev, curr) -> prev.plus(curr)) // Translation3d#plus() adds the internal x, y, and z
            .div(poses.size());
        
        Rotation3d avgRotation = poses.stream()
            .map(pose -> pose.getRotation())
            .reduce(new Rotation3d(), (prev, curr) -> prev.plus(curr)) // Rotation3d#plus() multiplies the internal quaternions
            .div(poses.size());
        
        return new Pose3d(avgTranslation, avgRotation);
    }

    public static Sendable getDefaultPoseSendable(Pose3d pose) {
        return builder -> {
            builder.addDoubleProperty("Translation X", pose::getX, t -> {});
            builder.addDoubleProperty("Translation Y", pose::getY, t -> {});
            builder.addDoubleProperty("Translation Z", pose::getZ, t -> {});

            builder.addDoubleProperty("Rotation X", pose.getRotation()::getX, t -> {});
            builder.addDoubleProperty("Rotation Y", pose.getRotation()::getY, t -> {});
            builder.addDoubleProperty("Rotation Z", pose.getRotation()::getY, t -> {});
        };
    }
}