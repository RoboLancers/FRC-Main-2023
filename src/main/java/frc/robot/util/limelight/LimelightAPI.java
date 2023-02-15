package frc.robot.util.limelight;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

// import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.Websocket;

public class LimelightAPI {

    private static NetworkTable limelightNT = NetworkTableInstance.getDefault().getTable("limelight");

    public Websocket wsClient;

    public static boolean logging;

    public LimelightAPI(boolean logging) throws URISyntaxException {
        this.wsClient = new Websocket(new URI(Constants.Limelight.kLimelightURLString));
        // this.logging = logging;
        // LimelightAPI.limelightNT =
        // NetworkTableInstance.getDefault().getTable("limelight");

        // if (LimelightAPI.limelightNT == null) {
        // SmartDashboard.putString("Limelight table", "null");
        // }

        // SmartDashboard.putString("Limelight table", "not null");
    }

    public Pose2d getActualPose2d() {
        var rawJson = this.wsClient.getMessage();

        if (rawJson == null) {
            System.out.println("rawJson is null");
            return new Pose2d();
        }

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(rawJson);
            System.out.println("node" + node);

            return new Pose2d();

            // double tx = node.get("transform").get("tx").asDouble();
            // double tz = node.get("transform").get("tz").asDouble();
            // double ry = node.get("transform").get("ry").asDouble();

            // return new Pose2d(tx, tz, new Rotation2d(ry * Math.PI / 180));

        } catch (JsonMappingException e) {
            System.out.println("fuck1");
            System.out.println(e);
        } catch (JsonProcessingException e) {
            System.out.println("fuck2");
            System.out.println(e);
        }
        return new Pose2d();

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
    public static Pose2d adjustCamPose() {

        var camPose = LimelightAPI.camPose();

        if (camPose == null) {
            return new Pose2d();
        }
        
        double dZ = camPose.getY();
        double dX = camPose.getX();

        double actualRot = camPose.getRotation().getRadians();

        double adjustedRot = Math.atan2(dX, dZ);

        double theta = adjustedRot - actualRot;

        double distance = Math.hypot(dX, dZ);

        double adjustedX = (distance * Math.cos(theta)) - Constants.GridAlign.kAdjustZ * Math.sin(actualRot);
        double adjustedZ = (distance * Math.sin(theta)) - Constants.GridAlign.kAdjustZ * Math.cos(actualRot);

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

    public static Pose2d flattenPose(Pose3d raw) {
        return new Pose2d(raw.getX(), raw.getZ(), new Rotation2d(raw.getRotation().getAngle()));
    }

    public static Pose2d botPose() {
        return flattenPose(LimelightAPI.getPose("botpose_targetspace"));
    }

    public static Pose2d camPose() {
        return flattenPose(LimelightAPI.getPose("camerapose_targetspace"));
    }

}
