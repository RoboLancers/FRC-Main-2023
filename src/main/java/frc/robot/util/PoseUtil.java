package frc.robot.util;

import java.util.ArrayList;
import java.util.stream.Stream;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.Sendable;
import frc.robot.Constants;

public class PoseUtil {
    public static Pose2d averagePoses(ArrayList<Pose2d> posesRaw) {

        ArrayList<Pose2d> poses = sanitizePoses(posesRaw);

        Translation2d avgTranslation = poses.stream()
                .map(pose -> pose.getTranslation())
                .reduce(new Translation2d(), (prev, curr) -> prev.plus(curr)) // Translation3d#plus() adds the internal
                                                                              // x, y, and z
                .div(poses.size());

        Rotation2d avgRotation = poses.stream()
                .map(pose -> pose.getRotation())
                .reduce(new Rotation2d(), (prev, curr) -> prev.plus(curr)) // Rotation3d#plus() multiplies the internal
                                                                           // quaternions
                .div(poses.size());

        return new Pose2d(avgTranslation.getX(), avgTranslation.getY(), avgRotation);
    }

    public static ArrayList<Pose2d> sanitizePoses(ArrayList<Pose2d> poses) {

        Stream<Pose2d> poseStream = poses.stream()
                .filter(p -> (p.getX() > Constants.GridAlign.kCamSanityX[0]
                        && p.getX() < Constants.GridAlign.kCamSanityX[1]) &&
                        (p.getY() > Constants.GridAlign.kCamSanityY[0]
                                && p.getY() < Constants.GridAlign.kCamSanityY[1]));

        return (ArrayList<Pose2d>) poseStream.toList();

    }

    public static Sendable getDefaultPoseSendable(Pose2d pose) {
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
}