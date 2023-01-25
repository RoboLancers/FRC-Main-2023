package frc.robot.util.limelight;

import java.lang.constant.Constable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Limelight extends SubsystemBase {

    private NetworkTable table;
    // public double tV;
    public Pose3d botPose = new Pose3d();
    public Pose3d camTran = new Pose3d();

    public int aprilTagID;

    public Limelight() {

        this.table = NetworkTableInstance.getDefault().getTable("limelight");

        if (this.table == null) {
            new PrintCommand("Limelight table is null");

            SmartDashboard.putString("Limelight table", "null");

        }

        SmartDashboard.putString("Limelight table", "not null");

        this.aprilTagID = (int) this.table.getEntry("tid").getInteger(0);
    }

    @Override
    public void periodic() {
        Pose3d botPose = this.currentPose();
        Pose3d camPose = this.camPose();

        SmartDashboard.putNumber("campose X", camPose.getX());
        SmartDashboard.putNumber("campose y", camPose.getY());
        SmartDashboard.putNumber("campose z", camPose.getZ());

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

    public boolean tV() {
        return this.table.getEntry("tv").getDouble(0) == 1;
    }

    public Pose3d getPose(String target) {
        double[] smd = new double[6];

        double[] poseRaw = this.table.getEntry(target).getDoubleArray(smd);

        Rotation3d rotationPose = new Rotation3d(poseRaw[3], poseRaw[4], poseRaw[5]);

        Pose3d pose = new Pose3d(poseRaw[0], poseRaw[1], poseRaw[2], rotationPose);

        return pose;
    }

    /** Returns an adjusted Pose3D based on camera pose */
    public static Pose3d adjustForCenter(Pose3d camPose) {

        return new Pose3d(camPose.getX() - Constants.GridAlign.kAdjustX, camPose.getY(),
                camPose.getZ() - Constants.GridAlign.kAdjustZ, camPose.getRotation());

    }

    public Pose3d currentPose() {
        return this.getPose("botpose");
    }

    public Pose3d camPose() {
        return this.getPose("campose");
    }

}
