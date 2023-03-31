package frc.robot.commands.trajectory;

import java.sql.Time;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class SimulateTrajectoryCommand extends CommandBase
{
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final Field2d field;
    private final Trajectory trajectory;
    private Timer timer; 
    private double prevTime; 
    private double maxTime; 

    public SimulateTrajectoryCommand(Trajectory trajectory, Field2d field) {
        this.timer = new Timer();
        this.trajectory = trajectory; 
        this.field = field; 
        this.maxTime = trajectory.getTotalTimeSeconds(); 
    }

    @Override
    public void initialize() {
        prevTime = -1;
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        double curTime = timer.get();

        // double dt = curTime - prevTime;

        Trajectory.State state = this.trajectory.sample(curTime);

        System.out.println(state);

        field.getRobotObject().setPose(new Pose2d(state.poseMeters.getX() + 10, state.poseMeters.getY() + 10, state.poseMeters.getRotation())
        // .plus(new Transform2d(new Translation2d(10, 10), new Rotation2d(0)))
        );
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished()
    {
        return timer.hasElapsed(maxTime);
    }
}
