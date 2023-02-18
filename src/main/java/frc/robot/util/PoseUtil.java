package frc.robot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.opencv.core.Size;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.Sendable;

import frc.robot.Constants;

public class PoseUtil {
    public static Pose2d averagePoses(SizedQueue<Pose2d> posesRaw) {
        SizedQueue<Pose2d> poses = posesRaw; // sanitizePoses(posesRaw);

        // System.out.println("san");
        // System.out.println(poses);

        Translation2d avgTranslation = poses.stream()
            .map(pose -> pose.getTranslation())
            .reduce(new Translation2d(), (prev, curr) -> prev.plus(curr))
            .div(poses.size());

            // System.out.println("trans");
            // System.out.println(avgTranslation);

        Rotation2d avgRotation = poses.stream()
            .map(pose -> pose.getRotation())
            .reduce(new Rotation2d(), (prev, curr) -> prev.plus(curr))
            .div(poses.size());

        return new Pose2d(avgTranslation.getX(), avgTranslation.getY(), avgRotation);
    }

    public static Pose2d averagePipelinePoses(ArrayList<Pose2d> poses) {
        // TODO: debug sanitization
        // List<Pose2d> poses = sanitizePosesList(posesRaw);

        Translation2d avgTranslation = poses.stream()
            .map(pose -> pose.getTranslation())
            .reduce(new Translation2d(), (prev, curr) -> prev.plus(curr))
            .div(poses.size());

        Rotation2d avgRotation = poses.stream()
            .map(pose -> pose.getRotation())
            .reduce(new Rotation2d(), (prev, curr) -> prev.plus(curr))
            .div(poses.size());

        return new Pose2d(avgTranslation.getX(), avgTranslation.getY(), avgRotation);
    }

    public static SizedQueue<Pose2d> sanitizePoses(SizedQueue<Pose2d> poses) {
        List<Pose2d> filteredPoses = poses.stream()
            .filter(p -> (
                p.getX() > Constants.GridAlign.kCamSanityXMin &&
                p.getX() < Constants.GridAlign.kCamSanityXMax &&
                p.getY() > Constants.GridAlign.kCamSanityZMin &&
                p.getY() < Constants.GridAlign.kCamSanityZMax
            )).collect(Collectors.toList());

        SizedQueue<Pose2d> queue = new SizedQueue<>(3);

        for (int i = 0; i < filteredPoses.size(); i++) {
            queue.add(filteredPoses.get(i));
        }

        return queue;
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