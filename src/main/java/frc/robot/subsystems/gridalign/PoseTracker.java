package frc.robot.subsystems.gridalign;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.bananasamirite.robotmotionprofile.ParametricSpline;
import org.bananasamirite.robotmotionprofile.TankMotionProfile;
import org.bananasamirite.robotmotionprofile.Waypoint;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.ProfileMethod;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.commands.MotionProfileCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.MotionProfileUtils;
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
        public Pose2d fuckingCampose;

        private Pose2d avgPythonCamPose;

        private Pose2d avgAprilTagCamPose;

        public PoseTracker(Drivetrain drivetrain) {

                this.drivetrain = drivetrain;
        }

        @Override
        public void periodic() {

                // setting the last 3
                this.camPoseQueue.add(LimelightAPI.adjustedCamPose(this.drivetrain));
                SmartDashboard.putNumber("latest queue num x", LimelightAPI.adjustedCamPose(this.drivetrain).getX());

                this.botPoseQueue.add(LimelightAPI.botPose());

                this.avgAprilTagCamPose = getAverageAprilPose();

                SmartDashboard.putNumber("avg campose x", avgAprilTagCamPose.getX());
                SmartDashboard.putNumber("avg campose z", avgAprilTagCamPose.getY());
                SmartDashboard.putNumber("heading", this.drivetrain.getHeading());
                // System.out.println("campose queue" + camPoseQueue);

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

        public static ParametricSpline generateSpline(Supplier<Pose2d> camPoseSupplier, Drivetrain drivetrain){
                Pose2d camPose = camPoseSupplier.get();

                var relativeDistance = Math
                                .sqrt((camPose.getX() * camPose.getX()) + (camPose.getY() * camPose.getY()));

                                System.out.println("reldist: " + relativeDistance);

                                var waypoints = new Waypoint[2];

                waypoints[0] = new Waypoint(0, 0, drivetrain.getHeading() * Math.PI / 180,
                                Constants.GridAlign.kInitialWeight * relativeDistance, 1);

                // grid waypoint (flipped x and y)
                waypoints[1] = new Waypoint((-1) * camPose.getY(), camPose.getX(), 0,
                               Constants.GridAlign.kGridWeight * relativeDistance,
                               1);

                return ParametricSpline.fromWaypoints(waypoints);
        }
}