package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.GridAlign;
import frc.robot.commands.Rumble;
import frc.robot.commands.GRR.TeleopGRR;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.util.Controller;
import frc.robot.util.InstantiatorCommand;
import frc.robot.util.enums.Displacement;
import frc.robot.util.limelight.LimelightAPI;
import frc.robot.subsystems.grabber.Grabber;
import frc.robot.subsystems.arm.Arm;

public class RobotContainer {
  /* Controllers */
  private final Controller driverController = new Controller(0);
  private final Controller manipulatorController = new Controller(1);

  /* Subsystems */
  private final Drivetrain drivetrain = new Drivetrain(driverController);
  private final PoseTracker poseTracker = new PoseTracker(drivetrain);
  private final Arm arm = new Arm();
  private final Grabber grabber = new Grabber();

  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  private Command command;

  public RobotContainer() {
    Controller.onPress(driverController.A, new InstantCommand(this.drivetrain::zeroHeading));

    Controller.onPress(driverController.B, new InstantCommand(() -> {
      drivetrain.resetOdometry(new Pose2d());
    }));

    Controller.onPress(driverController.X, new ConditionalCommand(
        // on true, instantiate and schedule align command
        new TeleopGRR(drivetrain, poseTracker, arm, grabber, () -> {
          if (driverController.dPadAngle() == 90) {
            return Displacement.RIGHT;
          } else if (driverController.dPadAngle() == 270) {
            return Displacement.LEFT;
          } else if (driverController.dPadAngle() == 0) {
            return Displacement.CENTER;
          } else {
            return Displacement.CENTER;
          }

        }),
        // on false rumble for 1 second
        new Rumble(driverController, Constants.GridAlign.kRumbleTime),
        // conditional upon a valid april tag
        LimelightAPI::validTargets));

    Controller.onPress(driverController.Y, new ConditionalCommand(
        // on true, instantiate and schedule align command
        new InstantiatorCommand(() -> new GridAlign(drivetrain, poseTracker, () -> Displacement.CENTER)),
        // on false rumble for 1 second
        new Rumble(driverController, Constants.GridAlign.kRumbleTime),
        // conditional upon a valid april tag
        LimelightAPI::validTargets));
  }

  public Command getAutonomousCommand() {
    return command;
  }
}