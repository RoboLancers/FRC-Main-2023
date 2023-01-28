package frc.robot.subsystems.gridalign;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.PoseUtil;
import frc.robot.util.SizedQueue;
import frc.robot.util.limelight.LimelightAPI;

public class PoseTracker extends SubsystemBase {

    // Getting last 3 camera pose values
    private SizedQueue<Pose2d> camPoseQueue = new SizedQueue<Pose2d>(3);

    // Getting last 3 bot pose values
    private SizedQueue<Pose2d> botPoseQueue = new SizedQueue<Pose2d>(3);

    private Drivetrain drivetrain;

    private Pose2d avgCamPose;

    public PoseTracker(Drivetrain drivetrain) {

        this.drivetrain = drivetrain;
    }

    @Override
    public void periodic() {
        // get apriltag data
        Pose2d avgAprilTagBotPose = this.getAveragePose(Constants.GridAlign.kAprilTagPipelineIndex, new ArrayList<>(this.botPoseQueue));
        Pose2d avgAprilTagCamPose = this.getAveragePose(Constants.GridAlign.kAprilTagPipelineIndex, new ArrayList<>(this.camPoseQueue));

        // get python data, might need to be via llpython instead
        Pose2d avgPythonBotPose = this.getAveragePose(Constants.GridAlign.kPythonPipelineIndex, new ArrayList<Pose2d>(this.botPoseQueue));
        Pose2d avgPythonCamPose = this.getAveragePose(Constants.GridAlign.kPythonPipelineIndex, new ArrayList<Pose2d>(this.camPoseQueue));

        // find average of apriltag & python data
        Pose2d avgBotPose = PoseUtil.averagePoses(new ArrayList<>(List.of(avgAprilTagBotPose, avgPythonBotPose)));
        this.avgCamPose =  PoseUtil.averagePoses(new ArrayList<>(List.of(avgAprilTagCamPose, avgPythonCamPose)));



        SmartDashboard.putData("average of last three apriltag pipeline bot poses", PoseUtil.getDefaultPoseSendable(avgAprilTagBotPose));
        SmartDashboard.putData("average of last three apriltag pipeline cam poses", PoseUtil.getDefaultPoseSendable(avgAprilTagCamPose));

        SmartDashboard.putData("average of last three python pipeline bot poses", PoseUtil.getDefaultPoseSendable(avgPythonBotPose));
        SmartDashboard.putData("average of last three python pipeline cam poses", PoseUtil.getDefaultPoseSendable(avgPythonCamPose));

        // smartdashboard the average of both
        SmartDashboard.putData("average of apriltag & python pipeline bot poses", PoseUtil.getDefaultPoseSendable(avgBotPose));
        SmartDashboard.putData("average of apriltag & python pipeline cam poses", PoseUtil.getDefaultPoseSendable(avgCamPose));
    }

    private void clearAndUpdatePoseStacks(int pipelineIndex) {
        LimelightAPI.setPipeline(pipelineIndex);
        
        this.camPoseQueue.clear();
        this.botPoseQueue.clear();

        // could make this dynamic based on stack capacity, not necessary at the moment
        this.camPoseQueue.add(LimelightAPI.adjustedCamPose(this.drivetrain));
        this.camPoseQueue.add(LimelightAPI.adjustedCamPose(this.drivetrain));
        this.camPoseQueue.add(LimelightAPI.adjustedCamPose(this.drivetrain));

        this.botPoseQueue.add(LimelightAPI.botPose());
        this.botPoseQueue.add(LimelightAPI.botPose());
        this.botPoseQueue.add(LimelightAPI.botPose());
    }

    public Pose2d getAverageAprilPose() {
       return this.avgCamPose;
    }



    public Pose2d getAveragePose(int pipelineIndex, ArrayList<Pose2d> poses) {
        this.clearAndUpdatePoseStacks(pipelineIndex);

        if (LimelightAPI.getPipeline() != pipelineIndex) {
            return new Pose2d();
        }
        
        return PoseUtil.averagePoses(poses);
    }    
}