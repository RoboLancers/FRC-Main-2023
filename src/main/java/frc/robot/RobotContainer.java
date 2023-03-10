package frc.robot;

import frc.robot.commands.trajectory.TrajectoryCommand;

import java.io.IOException;

import javax.naming.ldap.Control;

import org.bananasamirite.robotmotionprofile.Waypoint;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.grabber.Grabber;
import frc.robot.subsystems.gyro.Balance;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.trajectory.RobotTrajectoryCommand;
import frc.robot.commands.GridAlign;
import frc.robot.commands.Rumble;
import frc.robot.commands.TurnTuner;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveAnchorJoint;
import frc.robot.subsystems.arm.commands.MoveFloatingJoint;
import frc.robot.util.Controller;
import frc.robot.util.DriverController;
import frc.robot.util.InstantiatorCommand;
import frc.robot.util.ManipulatorController;
import frc.robot.util.DriverController.Mode;
import frc.robot.util.limelight.LimelightAPI;
public class RobotContainer {
  /* Controllers */
  private final DriverController driverController = new DriverController(0);
  private final ManipulatorController manipulatorController = new ManipulatorController(1);

  /* Subsystems */
  private Drivetrain drivetrain = new Drivetrain();
  private Arm arm = new Arm();
  // private Grabber grabber = new Grabber();
  private Gyro gyro = new Gyro();
  private PoseTracker poseTracker = new PoseTracker(drivetrain);
  private Intake intake = new Intake(); 
    
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  private Command command;
  // TODO: Raf is rly dumb for this shit
  // private final SmartDashboardDB db = new SmartDashboardDB();

  public RobotContainer() {
    // this.drivetrain.setDefaultCommand(new TeleopDrive(drivetrain, driverController));
    this.drivetrain.setDefaultCommand(new RunCommand(() -> {
      drivetrain.curvatureDrive(this.driverController.getThrottle(), this.driverController.getTurn(), this.driverController.getSlowMode());
    }, drivetrain));

  //  command = new MotionProfileCommand(drivetrain, new TankMotionProfile(ParametricSpline.fromWaypoints(new Waypoint[] {
  //    new Waypoint(1, 1, 0, 1, 1),
  //    new Waypoint(2, 1, Math.toRadians(90), 1, 1)
  //  }), ProfileMethod.TIME, new TankMotionProfileConstraints(1, 0.2)));
    command = Constants.Trajectory.trajectoryCreator.createCommand(drivetrain,
            new Waypoint[] {
                    new Waypoint(0, 0, 0, 2.734564202601426, 1),
                    new Waypoint(1.594, 0.798, Math.toRadians(1.145), 2.4, 1), 
                    // new Waypoint(
                    //   2, 1, Math.toRadians(45), 1.85, 1
                    // )
            }, new TrajectoryConfig(1, 0.2));
    // command = new TurnTuner(drivetrain);
    // try {
    //   command = RobotTrajectoryCommand.fromFile(drivetrain, Filesystem.getDeployDirectory().toPath().resolve("auto6.json").toFile());
    // } catch (IOException e) {
    //   // TODO Auto-generated catch block
    //   e.printStackTrace();
    // }

    // this.poseTracker.setDefaultCommand(new PrintCommand("Matt likes balls idk, Raf too"));

    CameraServer.startAutomaticCapture(); 

    configureButtonBindings();
  }

  private void configureButtonBindings() {

    // intake
    // don't question this
    Controller.onPress(manipulatorController.intakeElementTrigger, new InstantCommand(() -> {
      intake.intake();
    }, intake));
    Controller.onPress(manipulatorController.outtakeElementTrigger, new InstantCommand(() -> {
      intake.outtake();
    }, intake));
    Controller.onPress(manipulatorController.intakeOffTrigger, new InstantCommand(() -> {
      intake.off();
    }, intake));

    // // Grabber
    // Controller.onPress(driverController.A, new InstantCommand(grabber::toggleDeploy));

    // // Balance
    Controller.onPress(driverController.B, new Balance(drivetrain, gyro, 0));

    //slow mode
    Controller.onHold(driverController.RightTrigger, new InstantCommand(() -> driverController.setSlowMode(Mode.SLOW)));
    Controller.onRelease(driverController.RightTrigger, new InstantCommand(() -> driverController.setSlowMode(Mode.NORMAL)));
  
    // Grid Align
    Controller.onPress(driverController.Y, new ConditionalCommand(
      // on true, instantiate and schedule align command
      new InstantiatorCommand(() -> new GridAlign(drivetrain, poseTracker)),
      // on false rumble for 1 second
      new Rumble(driverController, Constants.GridAlign.kRumbleTime),
      // conditional upon a valid april tag
      LimelightAPI::validTargets
    ));


    // SmartDashboard.putNumber("target anchor  angle", 30);
    // SmartDashboard.putNumber("target floating   angle", 0);

    // Arm
    SmartDashboard.putNumber("anchor--setpoint", 0);
    Controller.onPress(driverController.X, new MoveAnchorJoint(() -> {
      double desired = SmartDashboard.getNumber("anchor--setpoint", 30);

      if(desired < 13) return 13;

      if(desired > 90) return 90;

      return desired;
    }, arm));

    SmartDashboard.getNumber("floating setpoint", 0);

    Controller.onPress(driverController.Y, new MoveFloatingJoint(() -> {
      double desired = SmartDashboard.getNumber("floating setpoint", 0);
      if(desired < 22) return 22;

      if(desired > 90) return 90;

      return desired;
    }, arm));


   // TODO: this lowkey not really gonna work rn, need to implement displacement properly
  //   Controller.onPress(driverController.LeftBumper, new ConditionalCommand(
  //       // on true, instantiate and schedule align command
  //       new TeleopGRR(drivetrain, poseTracker, arm, grabber),
  //       // on false rumble for 1 second
  //       new Rumble(driverController, Constants.GridAlign.kRumbleTime),
  //       // conditional upon a valid april tag
  //       LimelightAPI::validTargets));
  }

  // Complete arm controls, for now use testing
    /*
    // // Move the arm to the ground
    // manipulatorController.onPress(manipulatorController.X, new MoveToPos(arm, Constants.Arms.Positions.kLowAnchor, Constants.Arms.Positions.kLowFloating));
    // // Move the arm to the intake shelf
    // manipulatorController.onPress(manipulatorController.A, new MoveToPos(arm, Constants.Arms.Positions.kIntakeShelfAnchor, Constants.Arms.Positions.kIntakeShelfFloating));
    // // Move to the mid node
    // manipulatorController.onPress(manipulatorController.B, new MoveToPos(arm, Constants.Arms.Positions.kMidNodeAnchor, Constants.Arms.Positions.kMidNodeFloating));
    // // Mode to mid shelf
    // manipulatorController.onPress(manipulatorController.Y, new MoveToPos(arm, Constants.Arms.Positions.kMidShelfAnchor, Constants.Arms.Positions.kMidShelfFloating));
    // // Move to high node
    // manipulatorController.onPress(manipulatorController.LeftBumper, new MoveToPos(arm, Constants.Arms.Positions.kHighNodeAnchor, Constants.Arms.Positions.kHighNodeFloating));
    // // Move to high shelf
    // manipulatorController.onPress(manipulatorController.RightBumper, new MoveToPos(arm, Constants.Arms.Positions.kHighShelfAnchor, Constants.Arms.Positions.kHighShelfFloating));
    // Contract
    // manipulatorController.onPress(Controller.Button.RIGHT_JOYSTICK_BUTTON, new Contract(arm));
  */

  public Command getAutonomousCommand() {
    // return new InstantCommand(() -> {});
    return command; 
  }

  public void doSendables() {
  }
}
