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

        private Pose2d avgAprilTagCamPose;

        public PoseTracker(Drivetrain drivetrain) {

                this.drivetrain = drivetrain;
        }

        @Override
        public void periodic() {

                // setting the last 3
                this.camPoseQueue.add(LimelightAPI.adjustedCamPose(this.drivetrain));

                this.botPoseQueue.add(LimelightAPI.botPose());

                // get apriltag data
                Pose2d avgAprilTagBotPose = this.getAveragePose(Constants.GridAlign.kAprilTagPipelineIndex,
                                this.botPoseQueue);
                Pose2d avgAprilTagCamPose = this.getAveragePose(Constants.GridAlign.kAprilTagPipelineIndex,
                                this.camPoseQueue);

                this.avgAprilTagCamPose = avgAprilTagCamPose;

                // get python data, might need to be via llpython instead
                Pose2d avgPythonBotPose = this.getAveragePose(Constants.GridAlign.kPythonPipelineIndex,
                                this.botPoseQueue);
                Pose2d avgPythonCamPose = this.getAveragePose(Constants.GridAlign.kPythonPipelineIndex,
                                this.camPoseQueue);

                // find average of apriltag & python data
                // Pose2d avgBotPose = PoseUtil.averagePoses(new
                // SizedQueue<>(List.of(avgAprilTagBotPose, avgPythonBotPose)));
                this.avgCamPose = PoseUtil
                                .averagePipelinePoses(new ArrayList<>(List.of(avgAprilTagCamPose, avgPythonCamPose)));

                //   

                // we need this thing

                SmartDashboard.putNumber("avg campose x", avgAprilTagCamPose.getX());
                SmartDashboard.putNumber("avg campose z", avgAprilTagCamPose.getY());

                // SmartDashboard.putData("average of last three apriltag pipeline cam poses",
                //                 PoseUtil.getDefaultPoseSendable(avgAprilTagCamPose));

                // SmartDashboard.putData("average of last three python pipeline bot poses",
                //                 PoseUtil.getDefaultPoseSendable(avgPythonBotPose));
                // SmartDashboard.putData("average of last three python pipeline cam poses",
                //                 PoseUtil.getDefaultPoseSendable(avgPythonCamPose));

                // // smartdashboard the average of both
                // // SmartDashboard.putData("average of apriltag & python pipeline bot poses",
                // // PoseUtil.getDefaultPoseSendable(avgBotPose));
                // SmartDashboard.putData("average of apriltag & python pipeline cam poses",
                //                 PoseUtil.getDefaultPoseSendable(avgCamPose));
        }

        private void clearAndSetPipeline(int pipelineIndex) {

                this.camPoseQueue.clear();
                this.botPoseQueue.clear();

                LimelightAPI.setPipeline(pipelineIndex);

        }

        public Pose2d getAverageAprilPose() {
                return this.avgAprilTagCamPose;
        }

        public Pose2d getAveragePose(int pipelineIndex, SizedQueue<Pose2d> poses) {
                this.clearAndSetPipeline(pipelineIndex);

                if (LimelightAPI.getPipeline() != pipelineIndex) {
                        return new Pose2d();
                }

                return PoseUtil.averagePoses(poses);
        }
}