package frc.robot.util.limelight;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

    private NetworkTable table;
    // public double tV;
    public Pose3d botPose = new Pose3d();
    public Pose3d camTran = new Pose3d();

    public int aprilTagID;

    public Limelight() {

        this.table = NetworkTableInstance.getDefault().getTable("limelight");

        if (this.table == null) {
            new PrintCommand("Fuck off");
        }

        // this.tV = this.table.getEntry("tv").getDouble(0);
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

    @Override
    public void periodic(){
        double[] pose = this.currentPose();
        double[] camtran = this.currentCamtran();
        for(int i = 0;i<6;i++){
            SmartDashboard.putNumber("botpose entry" + i, pose[i]);
            SmartDashboard.putNumber("camtran entry" + i, camtran[i]);
        }
    }

    public boolean tV(){
        return this.table.getEntry("tv").getDouble(0) == 1;
    }

    public double[] currentPose(){
        double[] smd = new double[6];

        double[] pose = this.table.getEntry("botpose").getDoubleArray(smd);

        if(pose.length == 0){
            return smd;
        } else {
            return pose;
        }
    }

    public double[] currentCamtran(){
        double[] smd = new double[6];

        double[] pose = this.table.getEntry("campose").getDoubleArray(smd);

        if(pose.length == 0){
            return smd;
        } else {
            return pose;
        }
    }
    
    
}
