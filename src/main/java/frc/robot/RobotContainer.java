package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.GridAlign;
import frc.robot.commands.MotionProfileCommand;
import frc.robot.commands.Rumble;
import frc.robot.commands.TrajectoryCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.util.Controller;
import frc.robot.util.InstantiatorCommand;
import frc.robot.util.limelight.LimelightAPI;

public class RobotContainer {
    /* Controllers */
    private final Controller driverController = new Controller(0);
    private final Controller manipulatorController = new Controller(1);

    /* Subsystems */
    private final Drivetrain drivetrain = new Drivetrain(driverController);
    private final PoseTracker poseTracker = new PoseTracker(drivetrain);

  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  private Command command; 

  public RobotContainer() {
    Controller.onPress(driverController.A, new InstantCommand(this.drivetrain::zeroHeading));

    Controller.onPress(driverController.B, new InstantCommand(() -> {
      drivetrain.resetOdometry(new Pose2d());
    }));

    Controller.onPress(driverController.Y, new ConditionalCommand(
      // on true, instantiate and schedule align command
      new InstantiatorCommand(() -> new GridAlign(drivetrain, poseTracker)),
      // on false rumble for 1 second
      new Rumble(driverController, Constants.GridAlign.kRumbleTime),
      // conditional upon a valid april tag
      LimelightAPI::validTargets
    ));

    command = new MotionProfileCommand(drivetrain, new TankMotionProfile(ParametricSpline.fromWaypoints(new Waypoint[] {
      new Waypoint(0, 0, 0, 1, 1), 
      new Waypoint(1, 1, Math.toRadians(90), 1, 1)
    }), ProfileMethod.TIME, new TankMotionProfile.TankMotionProfileConstraints(2, 0.2))); 

    var autoVoltageConstraint =
        new DifferentialDriveVoltageConstraint(
            new SimpleMotorFeedforward(
                Constants.Trajectory.ksVolts,
                Constants.Trajectory.ksVoltSecondsPerMeter,
                Constants.Trajectory.kaVoltSecondsSquaredPerMeter),
                Constants.Trajectory.kDriveKinematics,
            10);

        // Create config for trajectory
        TrajectoryConfig config =
        new TrajectoryConfig(
                1,
                0.2)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.Trajectory.kDriveKinematics)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

            Trajectory exampleTrajectory =
            TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(0, 0, new Rotation2d(0)),
                // Pass through these two interior waypoints, making an 's' curve path
                // List.of(new Translation2d(0.5, 0.5), ),
                new ArrayList<>(), 
                // End 3 meters straight ahead of where we started, facing forward
                new Pose2d(1, 1, Rotation2d.fromDegrees(90)),
                // Pass config
                config);

                // command = new TrajectoryCommand(drivetrain, exampleTrajectory); 


    
  }

  public Command getAutonomousCommand() {
    return command; 
  }
}