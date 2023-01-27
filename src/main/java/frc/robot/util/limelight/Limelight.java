package frc.robot.util.limelight;


import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.Constants;

public class Limelight {

    private NetworkTable table;
    // public double tV;
    public Pose3d botPose = new Pose3d();
    public Pose3d camPose = new Pose3d();

    public int aprilTagID;

    public boolean logging;

    public Limelight(boolean logging) {

        this.table = NetworkTableInstance.getDefault().getTable("limelight");

        if (this.table == null) {
            

            SmartDashboard.putString("Limelight table", "null");

        }

        this.logging = logging;

        SmartDashboard.putString("Limelight table", "not null");

        this.aprilTagID = (int) this.table.getEntry("tid").getInteger(0);
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

    public boolean validTargets() {
        return this.table.getEntry("tv").getDouble(0) == 1;
    }

    public double horizontalOffset() {
        return this.table.getEntry("tx").getDouble(0);
    }

    public double verticalOffset() {
        return this.table.getEntry("ty").getDouble(0);
    }

    public double skew() {
        return this.table.getEntry("ts").getDouble(0);
    }

    public double targetArea() {
        return this.table.getEntry("ta").getDouble(0);
    }

    public double latency() {
        return this.table.getEntry("tl").getDouble(0);
    }

    public double shortSideLength() {
        return this.table.getEntry("tshort").getDouble(0);
    }

    public double longSideLength() {
        return this.table.getEntry("tlong").getDouble(0);
    }

    public double verticalSideLength() {
        return this.table.getEntry("tvert").getDouble(0);
    }

    public double horizontalSideLength() {
        return this.table.getEntry("thor").getDouble(0);
    }

    public double getAprilTagID() {
        return this.table.getEntry("tid").getDouble(0);
    }

    public double getPipeline() {
        return this.table.getEntry("getpipe").getDouble(0);
    }

    public Object rawJSONTargets() {
        var raw = this.table.getEntry("json").getValue().getValue();
        return raw;
    }

    // ! TODO find someway to type the raw json data
    public void getJSONTargets() {

        var mapper = new ObjectMapper();

    }

    public void setPipeline(int pipeline) {
        if (pipeline > 9 || pipeline < 0) {
            SmartDashboard.putString("Limelight pipeline", "invalid pipeline");
        } else {
            this.table.getEntry("pipeline").setNumber(pipeline);
            SmartDashboard.putString("Limelight pipeline", "pipeline set to" + pipeline);
        }

    }

    public void setLEDMode(LedMode mode) {
        this.table.getEntry("ledMode").setNumber(mode.getValue());
    }

    public void setCamMode(CamMode mode) {
        this.table.getEntry("camMode").setNumber(mode.getValue());
    }

    public void setStreamMode(StreamMode mode) {
        this.table.getEntry("stream").setNumber(mode.getValue());
    }

    public void setSnapshotMode(Snapshot mode) {
        this.table.getEntry("snapshot").setNumber(mode.getValue());
    }

    public void sendToRobot(double[] llrobot) {
        this.table.getEntry("llrobot").setDoubleArray(llrobot);
    }

    public double[] getFromRobot() {
        double[] llpython = new double[6];
        return this.table.getEntry("llpython").getDoubleArray(llpython);
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public Pose3d getPose(String target) {
        double[] smd = new double[6];

        double[] poseRaw = this.table.getEntry(target).getDoubleArray(smd);

        Rotation3d rotationPose = new Rotation3d(poseRaw[3], poseRaw[4], poseRaw[5]);

        Pose3d pose = new Pose3d(poseRaw[0], poseRaw[1], poseRaw[2], rotationPose);

        return pose;
    }

    /** Returns an adjusted Pose3D based on camera pose */
    public static Pose3d adjustCamPose(Pose3d rawCamPose) {
        var adjustedCamPoseX = (rawCamPose.getX() - Constants.GridAlign.kAdjustX) / rawCamPose.getZ();
        var adjustedCamPoseZ = (rawCamPose.getZ() - Constants.GridAlign.kAdjustZ);

        return new Pose3d(adjustedCamPoseX, rawCamPose.getY(),
                adjustedCamPoseZ, rawCamPose.getRotation());

    }

    public Pose3d botPose() {
        return this.getPose("botpose");
    }

    public Pose3d camPose() {
        return this.getPose("campose");
    }

    public Pose3d adjustedCamPose() {
        return adjustCamPose(this.camPose());
    }

}
