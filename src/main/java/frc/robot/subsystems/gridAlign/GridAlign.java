package frc.robot.subsystems.gridAlign;

import java.util.ArrayList;
import java.util.List;

import org.bananasamirite.robotmotionprofile.ParametricSpline;
import org.bananasamirite.robotmotionprofile.TankMotionProfile;
import org.bananasamirite.robotmotionprofile.Waypoint;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.ProfileMethod;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.TankMotionProfileConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class GridAlign extends SubsystemBase {
    private Pose2d startingCameraPosition;
    public Pose2d finalBotPosition;

    private Drivetrain drivetrain;
    private NetworkTable limelight;

    public GridAlign(Drivetrain drivetrain){
        this.startingCameraPosition = new Pose2d();
        this.finalBotPosition = new Pose2d();

        this.drivetrain = drivetrain;
        this.limelight = NetworkTableInstance.getDefault().getTable("limelight");
    }

    private static void logPose(String name, Pose2d pose){
        SmartDashboard.putNumber(name + " x", pose.getX());
        SmartDashboard.putNumber(name + " y", pose.getY());
        SmartDashboard.putNumber(name + " a", pose.getRotation().getDegrees());
    }

    public boolean hasTarget(){
        return this.limelight.getEntry("tv").getDouble(0) == 1;
    }

    private Pose2d currentPose(){
        double[] defaultPose = new double[6];

        double[] rawPose = this.limelight.getEntry("campose").getDoubleArray(defaultPose);

        if (rawPose.length != 6) return null;
        
        return new Pose2d(rawPose[0], rawPose[2], new Rotation2d(this.drivetrain.getHeading()));
    }

    public void recordStartingCameraPosition() {
        if (!this.hasTarget()) return; // TODO: error handle no target on access

        Pose2d current = this.currentPose();

        if (current == null) return; // TODO: error handle invalid campose

        this.startingCameraPosition = current;

        GridAlign.logPose("Starting", this.startingCameraPosition);
    }

    public void calculateFinalBotPosition(){
        double dx = -Constants.GridAlignment.cameraRadius * this.startingCameraPosition.getRotation().getSin();
        double dz = Constants.GridAlignment.cameraRadius * this.startingCameraPosition.getRotation().getCos();

        // assuming angle angle with respect to the look dir
        // TODO: look into angle signing and maybe flip dx

        this.finalBotPosition = new Pose2d(
            this.startingCameraPosition.getX() + dx,
            this.startingCameraPosition.getY() + dz,
            new Rotation2d(0)
        );

        GridAlign.logPose("Starting", this.finalBotPosition);
    }

    private ParametricSpline generateSpline(double finalDx){
        Waypoint p1 = new Waypoint(this.finalBotPosition.getY(), this.finalBotPosition.getX(), 0, 1, 1);
        
        // for testing
        Waypoint p2 = new Waypoint(this.finalBotPosition.getY() + 0.05, this.finalBotPosition.getX(), 0, 1, 1);
        
        List<Waypoint> points = new ArrayList<Waypoint>();
        points.add(p1);
        points.add(p2);

        return ParametricSpline.fromWaypoints(points);
    }

    public TankMotionProfile generateMotionProfile(){
        return new TankMotionProfile(
            this.generateSpline(0),
            ProfileMethod.TIME,
            new TankMotionProfileConstraints(1, 0)
        );
    }
}