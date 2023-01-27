package frc.robot.commands;

import org.bananasamirite.robotmotionprofile.ParametricSpline;
import org.bananasamirite.robotmotionprofile.TankMotionProfile;
import org.bananasamirite.robotmotionprofile.TankMotionProfile.ProfileMethod;
import org.bananasamirite.robotmotionprofile.Waypoint;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.gridalign.GridAlignSubsytem;

import frc.robot.Constants;

public class GridAlign extends CommandBase {
    private Pose3d camPose;

    private Waypoint[] waypoints;

    private MotionProfileCommand motionProfile;

    private GridAlignSubsytem gridAlignSubsytem;


    public GridAlign(Drivetrain drivetrain) {
    
        this.gridAlignSubsytem = new GridAlignSubsytem();
        this.camPose = this.gridAlignSubsytem.getAverageAprilCamPose();

        this.waypoints = new Waypoint[2];

       



        // Initial waypoint (switch x and y)

        this.waypoints[0] = new Waypoint(0, 0, this.camPose.getRotation().getY(), Constants.GridAlign.kInitialWeight, 1);

        // grid waypoint (flipped x and y)
        this.waypoints[1] = new Waypoint(this.camPose.getZ(), this.camPose.getX(), 0, Constants.GridAlign.kGridWeight, 1);
        
        ParametricSpline spline = ParametricSpline.fromWaypoints(this.waypoints);

        this.motionProfile = new MotionProfileCommand(
            drivetrain, 
            new TankMotionProfile(
                spline, 
                ProfileMethod.TIME, 
                new TankMotionProfile.TankMotionProfileConstraints(1, 0)
            )
        );
    }

    /* (non-Javadoc)
     * @see edu.wpi.first.wpilibj2.command.Command#execute()
     */
    @Override
    public void execute() {
        this.motionProfile.execute();
    }

  

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {}
}