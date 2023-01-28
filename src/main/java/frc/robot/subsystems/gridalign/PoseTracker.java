package frc.robot.subsystems.gridalign;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.Constants.Drivetrain;
import frc.robot.util.PoseUtil;
import frc.robot.util.SizedStack;
import frc.robot.util.limelight.LimelightAPI;

public class PoseTracker extends SubsystemBase {

    // Getting last 3 camera pose values
    private SizedQueue<Pose3d> camPoseStack = new SizedQueue<Pose3d>(3);

    // Getting last 3 bot pose values
    private SizedQueue<Pose3d> botPoseStack = new SizedQueue<Pose3d>(3);

    public PoseTracker(Drivetrain drivetrain) {
        
    }

    @Override
    public void periodic() {
        // get apriltag data
        Pose3d avgAprilTagBotPose = this.getAveragePose(Constants.GridAlign.kAprilTagPipelineIndex, new ArrayList<>(this.botPoseStack));
        Pose3d avgAprilTagCamPose = this.getAveragePose(Constants.GridAlign.kAprilTagPipelineIndex, new ArrayList<>(this.camPoseStack));

        // get python data, might need to be via llpython instead
        Pose3d avgPythonBotPose = this.getAveragePose(Constants.GridAlign.kPythonPipelineIndex, new ArrayList<Pose3d>(this.botPoseStack));
        Pose3d avgPythonCamPose = this.getAveragePose(Constants.GridAlign.kPythonPipelineIndex, new ArrayList<Pose3d>(this.camPoseStack));

        // find average of apriltag & python data
        Pose3d avgBotPose = PoseUtil.averagePoses(new ArrayList<>(List.of(avgAprilTagBotPose, avgPythonBotPose)));
        Pose3d avgCamPose = PoseUtil.averagePoses(new ArrayList<>(List.of(avgAprilTagCamPose, avgPythonCamPose)));

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
        
        this.camPoseStack.clear();
        this.botPoseStack.clear();

        // could make this dynamic based on stack capacity, not necessary at the moment
        this.camPoseStack.add(LimelightAPI.adjustedCamPose());
        this.camPoseStack.add(LimelightAPI.adjustedCamPose());
        this.camPoseStack.add(LimelightAPI.adjustedCamPose());

        this.botPoseStack.add(LimelightAPI.botPose());
        this.botPoseStack.add(LimelightAPI.botPose());
        this.botPoseStack.add(LimelightAPI.botPose());
    }

    private Pose3d getAveragePose(int pipelineIndex, ArrayList<Pose3d> poses) {
        this.clearAndUpdatePoseStacks(pipelineIndex);

        if (LimelightAPI.getPipeline() != pipelineIndex) {
            return new Pose3d();
        }
        
        return PoseUtil.averagePoses(poses);
    }    
}