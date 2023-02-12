package frc.robot.util;

import java.util.Collection;
import java.util.Collections;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.Sendable;

import frc.robot.Constants;

public class PoseUtil {
    public static <T extends Collection<Pose2d>> Pose2d averagePoses(T poses, boolean sanitize) {
        if (sanitize) {
            sanitizePoses(poses);
        }

        Translation2d avgTranslation = poses.stream()
                .map(Pose2d::getTranslation)
                .reduce(new Translation2d(), Translation2d::plus)
                .div(poses.size());

        Rotation2d avgRotation = poses.stream()
                .map(Pose2d::getRotation)
                .reduce(new Rotation2d(), Rotation2d::plus)
                .div(poses.size());

        return new Pose2d(avgTranslation, avgRotation);
    }

    // TODO: decide if direct modification is fine here (probably not)
    public static <T extends Collection<Pose2d>> void sanitizePoses(T poses) {
        /* T copy = poses; */

        poses.removeIf(p -> !(
                p.getX() > Constants.GridAlign.kCamSanityXMin &&
                        p.getX() < Constants.GridAlign.kCamSanityXMax &&
                        p.getY() > Constants.GridAlign.kCamSanityZMin &&
                        p.getY() < Constants.GridAlign.kCamSanityZMax
        ));

        // return copy;
    }

    public static Pose2d flattenPose(Pose3d pose) {
        return new Pose2d(pose.getX(), pose.getZ(), new Rotation2d(pose.getRotation().getY()));
    }

    public static Sendable getDefaultPose2dSendable(Pose2d pose) {
        return builder -> {
            builder.addDoubleProperty("Translation X", pose::getX, t -> {
            });
            builder.addDoubleProperty("Translation Z", pose::getY, t -> {
            });

            builder.addDoubleProperty("Rotation (degrees)", pose.getRotation()::getDegrees, t -> {
            });
            builder.addDoubleProperty("Rotation (radians)", pose.getRotation()::getRadians, t -> {
            });
        };
    }

    public static Sendable getDefaultPose3dSendable(Pose3d pose) {
        return builder -> {
            builder.addDoubleProperty("Translation X", pose::getX, t -> {
            });
            builder.addDoubleProperty("Translation Y", pose::getY, t -> {
            });
            builder.addDoubleProperty("Translation Z", pose::getZ, t -> {
            });

            builder.addDoubleProperty("Roll (degrees)", () -> Math.toDegrees(pose.getRotation().getX()), t -> {
            });
            builder.addDoubleProperty("Pitch (degrees)", () -> Math.toDegrees(pose.getRotation().getY()), t -> {
            });
            builder.addDoubleProperty("Yaw (degrees)", () -> Math.toDegrees(pose.getRotation().getX()), t -> {
            });

            builder.addDoubleProperty("Roll (radians)", pose.getRotation()::getX, t -> {
            });
            builder.addDoubleProperty("Pitch (radians)", pose.getRotation()::getY, t -> {
            });
            builder.addDoubleProperty("Yaw (radians)", pose.getRotation()::getZ, t -> {
            });
        };
    }
}