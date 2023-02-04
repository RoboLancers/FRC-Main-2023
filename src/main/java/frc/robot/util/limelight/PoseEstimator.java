package frc.robot.util.limelight;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants;

public class PoseEstimator {
    private static double calculateHeightRatio(){
        double[] corners = LimelightAPI.getCorners();

        // TODO: error handle properly
        if (corners.length != 8) return 0;

        // TODO: corners order?
        double y1 = corners[1];
        double y2 = corners[3];
        double y3 = corners[5];
        double y4 = corners[7];

        double h1 = y2 - y1;
        double h2 = y4 - y3;

        return h1 / h2;
    }

    public static Pose2d calculatePosition(double heading){
        double R = PoseEstimator.calculateHeightRatio();
        double tan = Math.tan(heading);
        double W = Constants.GridAlign.kAprilTagWidth;

        double numerator = (R + 1) * (W / 2) * tan;
        double denominator = (R - 1) * (tan * tan + 1);

        double z = numerator / denominator;
        double x = z * tan;

        return new Pose2d(z, x, new Rotation2d(heading));
    }

    public static double calculateHeadingError(){
        double[] corners = LimelightAPI.getCorners();
        
        // TODO: corners order?
        double y1 = corners[1];
        double y2 = corners[3];
        double y3 = corners[5];
        double y4 = corners[7];

        double h1 = y2 - y1;
        double h2 = y4 - y3;

        double x1 = (corners[0] + corners[2]) / 2;
        double x2 = (corners[4] + corners[6]) / 2;

        double w = x2 - x1;

        // TODO: translate x to center if necessary

        return x1 + w * h1 / (h1 + h2);
    }
}