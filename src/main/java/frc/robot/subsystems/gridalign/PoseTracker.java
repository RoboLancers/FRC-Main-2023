package frc.robot.subsystems.gridalign;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.PipelineIndex;
import frc.robot.util.PoseUtil;
import frc.robot.util.SizedQueue;
import frc.robot.util.limelight.LimelightAPI;

public class PoseTracker extends SubsystemBase {

        // Getting last 3 camera pose values
        private SizedQueue<Pose2d> camPoseQueue = new SizedQueue<Pose2d>(3);

        // Getting last 3 bot pose values
        private SizedQueue<Pose2d> botPoseQueue = new SizedQueue<Pose2d>(3);

        private Drivetrain drivetrain;

        private Pose2d avgPythonCamPose;

        private Pose2d avgAprilTagCamPose;

        public PoseTracker(Drivetrain drivetrain) {

                this.drivetrain = drivetrain;
        }

        @Override
        public void periodic() {

                // setting the last 3
                this.camPoseQueue.add(LimelightAPI.adjustedCamPose(this.drivetrain));

                this.botPoseQueue.add(LimelightAPI.botPose());
               
  

                SmartDashboard.putNumber("avg campose x", avgAprilTagCamPose.getX());
                SmartDashboard.putNumber("avg campose z", avgAprilTagCamPose.getY());

                
                // Pose2d avgPythonBotPose = this.getAveragePose(this.botPoseQueue);
                // Pose2d avgAprilTagBotPose = this.getAveragePose(this.botPoseQueue);  

                // SmartDashboard.putData("average of last three apriltag pipeline cam poses",
                // PoseUtil.getDefaultPoseSendable(avgAprilTagCamPose));

                // SmartDashboard.putData("average of last three python pipeline bot poses",
                // PoseUtil.getDefaultPoseSendable(avgPythonBotPose));
                // SmartDashboard.putData("average of last three python pipeline cam poses",
                // PoseUtil.getDefaultPoseSendable(avgPythonCamPose));

                // // smartdashboard the average of both
                // // SmartDashboard.putData("average of apriltag & python pipeline bot poses",
                // // PoseUtil.getDefaultPoseSendable(avgBotPose));
                // SmartDashboard.putData("average of apriltag & python pipeline cam poses",
                // PoseUtil.getDefaultPoseSendable(avgCamPose));
        }

        public Pose2d getSensorFusionAverage() {
                return PoseUtil
                                .averagePipelinePoses(new ArrayList<>(List.of(avgAprilTagCamPose, avgPythonCamPose)));
        }

        public void clearAndSetPipeline(PipelineIndex index) {

                this.camPoseQueue.clear();
                this.botPoseQueue.clear();

                LimelightAPI.setPipeline(index.getValue());

        }

        public Pose2d getAverageAprilPose() {
                return this.getAveragePose(this.camPoseQueue);
        }

        public Pose2d getAveragePythonPose() {
                return this.getAveragePose(this.camPoseQueue);
        }

        public Pose2d getAveragePose(SizedQueue<Pose2d> poses) {

                return PoseUtil.averagePoses(poses);
        }
}