package frc.robot.subsystems.gridalign;


import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.limelight.Limelight;

public class GridAlignSubsytem extends SubsystemBase {
    public Limelight limelight;
    public Pose3d camPose;
    public Pose3d botPose;

    // Getting last 3 camera pose values
    SizedStack<Pose3d> camPoseStack = new SizedStack<Pose3d>(3);

    // Getting last 3 bot pose values
    SizedStack<Pose3d> botPoseStack = new SizedStack<Pose3d>(3);

    public GridAlignSubsytem() {
        this.limelight = new Limelight(true);
        this.camPose = this.limelight.adjustedCamPose();

        this.botPose = this.limelight.botPose();

    }

    @Override
    public void periodic() {

        // adjusted
        camPoseStack.push(this.camPose);
        botPoseStack.push(this.botPose);

        if (this.limelight.logging) {
            Limelight.logPoses(camPose, botPose);
        }
    }

    public void aprilPipeline() {
        this.limelight.setPipeline(0);
    }

    public void pythonPipeline() {
        this.limelight.setPipeline(1);
    }

    // ! TODO figure out a way to deal with rotations, for now it averages
    // translational and zeros rotation
    public Pose3d getAverageAprilCamPose() {

        this.aprilPipeline();

        if (this.limelight.getPipeline() != 0) {
            return new Pose3d();
        }

        double addedX = 0;
        double addedY = 0;
        double addedZ = 0;
        for (Pose3d pose : camPoseStack) {
            addedX += pose.getX();
            addedY += pose.getY();
            addedZ += pose.getZ();
        }

        Pose3d averageCamPose = new Pose3d(addedX, addedY, addedZ, new Rotation3d());

        averageCamPose = averageCamPose.div(camPoseStack.capacity());

        return averageCamPose;
    }

}
