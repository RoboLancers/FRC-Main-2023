package frc.robot.util.limelight;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class LimelightAPI {

    private static NetworkTable limelightNT = NetworkTableInstance.getDefault().getTable("limelight");

    public static boolean logging;

    public LimelightAPI(boolean logging) {
        // this.logging = logging;
        // LimelightAPI.limelightNT =
        // NetworkTableInstance.getDefault().getTable("limelight");

        // if (LimelightAPI.limelightNT == null) {
        // SmartDashboard.putString("Limelight table", "null");
        // }

        // SmartDashboard.putString("Limelight table", "not null");
    }

    public static void logPoses(Pose3d camPose, Pose3d botPose) {

        SmartDashboard.putNumber("campose x (z adj)", camPose.getX() / camPose.getZ());
        SmartDashboard.putNumber("campose y (z adj)", camPose.getY() / camPose.getZ());
        SmartDashboard.putNumber("campose z", camPose.getZ());

        /*
         * SmartDashboard.putNumber("adj campose x",
         * Limelight.adjustCamPose(camPose).getX());
         * SmartDashboard.putNumber("adj campose y",
         * Limelight.adjustCamPose(camPose).getY());
         * SmartDashboard.putNumber("adj campose z",
         * Limelight.adjustCamPose(camPose).getZ());
         */

        SmartDashboard.putNumber("campose rX", camPose.getRotation().getX());
        SmartDashboard.putNumber("campose rY", camPose.getRotation().getY());
        SmartDashboard.putNumber("campose rZ", camPose.getRotation().getZ());

        SmartDashboard.putNumber("botpose X", botPose.getX());
        SmartDashboard.putNumber("botpose y", botPose.getY());
        SmartDashboard.putNumber("botpose z", botPose.getZ());

        SmartDashboard.putNumber("botpose rX", botPose.getRotation().getX());
        SmartDashboard.putNumber("botpose rY", botPose.getRotation().getY());
        SmartDashboard.putNumber("botpose rZ", botPose.getRotation().getZ());
    }

    /** Returns an adjusted Pose3D based on camera pose */
    public static Pose2d adjustCamPose(Pose2d camPose, Drivetrain drivetrain) {
        if (camPose == null) {
            return new Pose2d();
        }
        
        double adjustedTransX = camPose.getX() - Constants.GridAlign.kAdjustX;
        double adjustedTransZ = camPose.getY() - Constants.GridAlign.kAdjustZ;
        double rotY = drivetrain.getHeading() * Math.PI / 180;

        double adjustedRotX = camPose.getX() - Constants.GridAlign.kAdjustZ * Math.sin(rotY)
                + Constants.GridAlign.kAdjustX * Math.cos(rotY);
        double adjustedRotZ = camPose.getY() - Constants.GridAlign.kAdjustZ * Math.cos(rotY)
                + Constants.GridAlign.kAdjustX * Math.sin(rotY);

        return new Pose2d(adjustedTransX, adjustedTransZ, new Rotation2d(adjustedRotX, adjustedRotZ));
        // return new Pose2d(adjustedCamPoseX, pose3d.getY(), adjustedCamPoseZ,
        // pose3d.getRotation());
    }

    public static boolean validTargets() {
        return LimelightAPI.limelightNT.getEntry("tv").getDouble(0) == 1;
    }

    public static double horizontalOffset() {
        return LimelightAPI.limelightNT.getEntry("tx").getDouble(0);
    }

    public static double verticalOffset() {
        return LimelightAPI.limelightNT.getEntry("ty").getDouble(0);
    }

    public static double skew() {
        return LimelightAPI.limelightNT.getEntry("ts").getDouble(0);
    }

    public static double targetArea() {
        return LimelightAPI.limelightNT.getEntry("ta").getDouble(0);
    }

    public static double latency() {
        return LimelightAPI.limelightNT.getEntry("tl").getDouble(0);
    }

    public static double shortSideLength() {
        return LimelightAPI.limelightNT.getEntry("tshort").getDouble(0);
    }

    public static double longSideLength() {
        return LimelightAPI.limelightNT.getEntry("tlong").getDouble(0);
    }

    public static double verticalSideLength() {
        return LimelightAPI.limelightNT.getEntry("tvert").getDouble(0);
    }

    public static double horizontalSideLength() {
        return LimelightAPI.limelightNT.getEntry("thor").getDouble(0);
    }

    public static double getAprilTagID() {
        return LimelightAPI.limelightNT.getEntry("tid").getDouble(0);
    }

    public static double getPipeline() {
        return LimelightAPI.limelightNT.getEntry("getpipe").getDouble(0);
    }

    public static double[] getCorners() {
        double[] defaultCorners = new double[8];

        return LimelightAPI.limelightNT.getEntry("tcornxy").getDoubleArray(defaultCorners);
    }

    public static Object rawJSONTargets() {
        var raw = LimelightAPI.limelightNT.getEntry("json").getValue().getValue();
        return raw;
    }

    // ! TODO find some way to type the raw json data
    // public static void getJSONTargets() {

    // var mapper = new ObjectMapper();

    // }

    public static void setPipeline(int pipeline) {
        if (pipeline > 9 || pipeline < 0) {
            SmartDashboard.putString("Limelight pipeline", "invalid pipeline");
        } else {
            LimelightAPI.limelightNT.getEntry("pipeline").setNumber(pipeline);
            SmartDashboard.putString("Limelight pipeline", "pipeline set to" + pipeline);
        }
    }

    // ! TODO: import/create these enums

    public static void setLEDMode(LedMode mode) {
        LimelightAPI.limelightNT.getEntry("ledMode").setNumber(mode.getValue());
    }

    public static void setCamMode(CamMode mode) {
        LimelightAPI.limelightNT.getEntry("camMode").setNumber(mode.getValue());
    }

    public static void setStreamMode(StreamMode mode) {
        LimelightAPI.limelightNT.getEntry("stream").setNumber(mode.getValue());
    }

    public static void setSnapshotMode(Snapshot mode) {
        LimelightAPI.limelightNT.getEntry("snapshot").setNumber(mode.getValue());
    }

    public static void sendToRobot(double[] llrobot) {
        LimelightAPI.limelightNT.getEntry("llrobot").setDoubleArray(llrobot);
    }

    public static double[] getFromRobot() {
        double[] llpython = new double[6];
        return LimelightAPI.limelightNT.getEntry("llpython").getDoubleArray(llpython);
    }

    public static void setLogging(boolean logging) {
        LimelightAPI.logging = logging;
    }

    public static Pose3d getPose(String target) {
        double[] smd = new double[6];

        double[] poseRaw = LimelightAPI.limelightNT.getEntry(target).getDoubleArray(smd);

        if (poseRaw.length != 6) {
            return new Pose3d();
        }

        Rotation3d rotationPose = new Rotation3d(poseRaw[3], poseRaw[4], poseRaw[5]);

        return new Pose3d(poseRaw[0], poseRaw[1], poseRaw[2], rotationPose);
    }

    public static Pose2d flattenPose(Pose3d raw) {
        return new Pose2d(raw.getX(), raw.getZ(), new Rotation2d(raw.getRotation().getAngle()));
    }

    public static Pose2d botPose() {
        return flattenPose(LimelightAPI.getPose("botpose"));
    }

    public static Pose2d camPose() {
        return flattenPose(LimelightAPI.getPose("campose"));
    }

    public static Pose2d adjustedCamPose(Drivetrain drivetrain) {
        return LimelightAPI.adjustCamPose(LimelightAPI.camPose(), drivetrain);
    }
}