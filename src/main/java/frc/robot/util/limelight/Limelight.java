package frc.robot.util.limelight;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.PrintCommand;

public class Limelight {

    private NetworkTable table;
    public double tV;
    public Pose3d botPose;
    public Pose3d camTran;

    public int aprilTagID;

    public Limelight() {

        this.table = NetworkTableInstance.getDefault().getTable("limelight");

        this.tV = this.table.getEntry("tv").getDouble(0);
        var rawBotPose = this.table.getEntry("botpose").getValue().toString();
        var rawCamTran = this.table.getEntry("camtran").getValue().toString();
        this.aprilTagID = (int) this.table.getEntry("tid").getInteger(0);

        ObjectMapper objMapper = new ObjectMapper();

        try {
            this.botPose = objMapper.readValue(rawBotPose, Pose3d.class);
            this.camTran = objMapper.readValue(rawCamTran, Pose3d.class);
        } catch (JsonMappingException e) {
            new PrintCommand("LIMELIGHT ERROR" + e);
        } catch (JsonProcessingException e) {
            new PrintCommand("LIMELIGHT ERROR" + e);
        }

    }
}
