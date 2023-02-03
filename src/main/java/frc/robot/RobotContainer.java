package frc.robot;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.gridalign.PoseTracker;
import frc.robot.subsystems.gridalign.commands.Align;
// import frc.robot.subsystems.gridalign.commands.GridAlign;
import frc.robot.util.XboxController;
import frc.robot.util.XboxController.Button;
import frc.robot.util.limelight.LimelightAPI;

public class RobotContainer {

  /* Controllers */
  private final XboxController driverController = new XboxController(0);
  private final XboxController manipulatorController = new XboxController(1);

  /* Subsystems */
  private final Drivetrain drivetrain = new Drivetrain(driverController);
  private final PoseTracker poseTracker = new PoseTracker(drivetrain);

  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  public RobotContainer() {

    configureButtonBindings();

    BooleanSupplier isAprilTagVisible = LimelightAPI::validTargets;

    // this.driverController.whileHeld(Button.Y, new ConditionalCommand(new
    // RunCommand((() -> {
    // SmartDashboard.putBoolean("visible", true);
    // SmartDashboard.putNumber("AprilTag ID", LimelightAPI.getAprilTagID());

    // })), new RunCommand(() -> {
    // SmartDashboard.putBoolean("visible", false);
    // }), isAprilTagVisible));

    this.driverController.whenPressed(Button.X, new InstantCommand(this.drivetrain::zeroHeading));

    this.driverController.whenPressed(Button.Y,
        new ConditionalCommand(
          new InstantCommand(() -> {
            CommandScheduler.getInstance().schedule(new Align(drivetrain, poseTracker));
          }),
            // new InstantCommand(() -> {
            //   var waypoints = poseTracker.gridAlign();

            //   SmartDashboard.putNumber("waypoint initial x", waypoints[0].getX());
            //   SmartDashboard.putNumber("waypointinitial  z", waypoints[0].getY());

            //   SmartDashboard.putNumber("waypoint final x", waypoints[1].getX());
            //   SmartDashboard.putNumber("waypoint final y", waypoints[1].getY());

            // })

            new SequentialCommandGroup(new InstantCommand(() -> {
              driverController.setRumble(true);
            }), new WaitCommand(Constants.GridAlign.kRumbleTime), new InstantCommand(() -> {
              driverController.setRumble(false);
            })), isAprilTagVisible));

    this.drivetrain.setDefaultCommand(new RunCommand(
        () -> this.drivetrain.arcadeDrive(driverController.getAxisValue(XboxController.Axis.LEFT_Y),
            driverController.getAxisValue(XboxController.Axis.RIGHT_X)),
        drivetrain));

    /* Add autos here */
    // autoChooser.addOption("name", auto);
  }

  private void configureButtonBindings() {
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}