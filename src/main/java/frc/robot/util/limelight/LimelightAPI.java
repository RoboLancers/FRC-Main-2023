package frc.robot.util.limelight;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Constants;
import frc.robot.util.PoseUtil;
import frc.robot.util.enums.CamMode;
import frc.robot.util.enums.LedMode;
import frc.robot.util.enums.Snapshot;
import frc.robot.util.enums.StreamMode;

public class LimelightAPI {

    private static final NetworkTable limelightNT = NetworkTableInstance.getDefault().getTable("limelight");

    public static boolean logging;

    public LimelightAPI(boolean logging) {}

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
    public static Pose2d adjustCamPose() {

        Pose2d camPose = LimelightAPI.camPose();

        if (camPose == null) {
            return new Pose2d();
        }

        // TODO: offset or do so from pipeline
        double dZ = camPose.getY() + 0.69 * 0.420;
        double dX = camPose.getX();

        double actualRot = (Math.signum(-dX)) * camPose.getRotation().getRadians();

        SmartDashboard.putNumber("frfr rot", actualRot * 180 / Math.PI);
        SmartDashboard.putNumber("frfr x", dX);
        SmartDashboard.putNumber("frfr z", dZ);

        double adjustedRot = Math.atan2(-dX, -dZ);

        double theta = adjustedRot - actualRot;

        double distance = Math.hypot(dX, dZ);

        double adjustedX = (distance * Math.cos(theta)) - Constants.GridAlign.kAdjustZ * Math.cos(actualRot);
        double adjustedZ = (-(distance * Math.sin(theta)) - Constants.GridAlign.kAdjustZ * Math.sin(actualRot));

        return new Pose2d(adjustedX, adjustedZ, new Rotation2d(actualRot));
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

    public static Object rawJSONTargets() {
        return LimelightAPI.limelightNT.getEntry("json").getValue().getValue();
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

        for (int i = 3; i < poseRaw.length; i++) {
            poseRaw[i] *= (Math.PI / 180);
        }
        Rotation3d rotationPose = new Rotation3d(poseRaw[5], poseRaw[3], poseRaw[4]);

        return new Pose3d(poseRaw[0], poseRaw[1], poseRaw[2], rotationPose);
    }

    public static Pose2d botPose() {
        return PoseUtil.flattenPose(LimelightAPI.getPose("botpose_targetspace"));
    }

    public static Pose2d camPose() {
        return PoseUtil.flattenPose(LimelightAPI.getPose("camerapose_targetspace"));
    }

}