package frc.robot;

import frc.robot.commands.trajectory.TrajectoryCommand;

import java.io.IOException;

import javax.naming.ldap.Control;

import org.bananasamirite.robotmotionprofile.Waypoint;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.util.Controller;
import frc.robot.util.DriverController; 
import frc.robot.util.ManipulatorController;
import frc.robot.util.DriverController.Mode;

public class RobotContainer {
  /* Controllers */
  private final DriverController driverController = new DriverController(0);
  private final ManipulatorController manipulatorController = new ManipulatorController(1);

  /* Subsystems */
  private Drivetrain drivetrain = new Drivetrain();
  private Arm arm = new Arm();
  private Intake intake = new Intake();
  private Gyro gyro = new Gyro();
  private PoseTracker poseTracker = new PoseTracker(drivetrain);
    
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  private Command command;
  // TODO: Raf is rly dumb for this shit
  // private final SmartDashboardDB db = new SmartDashboardDB();

  public RobotContainer() {
    // this.drivetrain.setDefaultCommand(new TeleopDrive(drivetrain, driverController));
    this.drivetrain.setDefaultCommand(new RunCommand(() -> {
      drivetrain.curvatureDrive(this.driverController.getThrottle(), this.driverController.getTurn(), this.driverController.getSlowMode());
    }, drivetrain));

    CameraServer.startAutomaticCapture(); 

    command = Constants.Trajectory.trajectoryCreator.createCommand(drivetrain,
            new Waypoint[] {
                    new Waypoint(0, 0, 0, 2.734564202601426, 1),
                    new Waypoint(1.594, 0.798, Math.toRadians(1.145), 2.4, 1), 
                    // new Waypoint(
                    //   2, 1, Math.toRadians(45), 1.85, 1
                    // )
            }, new TrajectoryConfig(1, 0.2));


            configureButtonBindings();

        }
    // command = new TurnTuner(drivetrain);
    // try {
    //   command = RobotTrajectoryCommand.fromFile(drivetrain, Filesystem.getDeployDirectory().toPath().resolve("auto6.json").toFile());
    // } catch (IOException e) {
    //   // TODO Auto-generated catch block
    //   e.printStackTrace();
    // }

    // this.poseTracker.setDefaultCommand(new PrintCommand("Matt likes balls idk, Raf too"));

  private void configureButtonBindings() {
    // intake
    Controller.onHold(driverController.RightTrigger, new RunCommand(intake::intake, intake));
    Controller.onHold(driverController.LeftTrigger, new RunCommand(intake::outtake, intake));

    // toggle cube
    Controller.onPress(manipulatorController.RightBumper, new InstantCommand(() -> { this.arm.armMode = true; }));
    // toggle cone
    Controller.onPress(manipulatorController.LeftBumper, new InstantCommand(() -> { this.arm.armMode = false; }));

    // ground
    Controller.onPress(manipulatorController.A, new MoveToPos(arm, Constants.Arm.Positions.Ground.kAnchor, Constants.Arm.Positions.Ground.kFloating));
    // contract
    Controller.onPress(manipulatorController.B, new MoveToPos(arm, Constants.Arm.Positions.Contracted.kAnchor, Constants.Arm.Positions.Contracted.kFloating));
    // mid
    Controller.onPress(manipulatorController.X, new ConditionalCommand(
      // cube (from shelf)
      new MoveToPos(arm, Constants.Arm.Positions.Shelf.kAnchor, Constants.Arm.Positions.Shelf.kFloating),
      //cone (mid)
      new MoveToPos(arm, Constants.Arm.Positions.MiddleCone.kAnchor, Constants.Arm.Positions.MiddleCone.kFloating),
      () -> this.arm.armMode
    ));
    // high
    Controller.onPress(manipulatorController.Y, new ConditionalCommand(
      // cube (high)
      new MoveToPos(arm, Constants.Arm.Positions.Cube.kAnchor, Constants.Arm.Positions.Cube.kFloating),
      // cone (high)
      new MoveToPos(arm, Constants.Arm.Positions.HighCone.kAnchor, Constants.Arm.Positions.HighCone.kFloating),
      () -> this.arm.armMode
    ));

    // dynamic for tuning
    // SmartDashboard.putNumber("anchor-setpoint", 13.0);
    // SmartDashboard.putNumber("floating-setpoint", 22.0);
    // Controller.onPress(manipulatorController.X, new MoveToPos(
    //   arm,
    //   () -> ControllerUtils.clamp(SmartDashboard.getNumber("anchor-setpoint", 0.0), 13.0, 95.0),
    //   () -> ControllerUtils.clamp(SmartDashboard.getNumber("floating-setpoint", 0.0), 22.0, 180.0)
    // ));

    //slow mode
    Controller.onHold(driverController.RightBumper, new InstantCommand(() -> driverController.setSlowMode(Mode.SLOW)));
    Controller.onRelease(driverController.RightBumper, new InstantCommand(() -> driverController.setSlowMode(Mode.NORMAL)));
  }

  public Command getAutonomousCommand() {
    // return new InstantCommand(() -> {});
    return command; 
  }

  public void doSendables() {
  }
}
