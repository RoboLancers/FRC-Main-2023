package frc.robot;

import java.time.Instant;

import edu.wpi.first.math.geometry.Pose2d;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.grabber.Grabber;
import frc.robot.subsystems.gyro.Balance;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.commands.GridAlign;
import frc.robot.commands.Rumble;
import frc.robot.commands.TeleopGRR;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.commands.GridAlign;
import frc.robot.commands.Rumble;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveAnchorJoint;
import frc.robot.subsystems.arm.commands.MoveFloatingJoint;
import frc.robot.subsystems.drivetrain.commands.TeleopDrive;
import frc.robot.util.Controller;
import frc.robot.util.DriverController;
import frc.robot.util.InstantiatorCommand;
import frc.robot.util.DriverController.Mode;
public class RobotContainer {
  /* Controllers */
  private final DriverController driverController = new DriverController(0);
  private final Controller manipulatorController = new Controller(1);

  /* Subsystems */
  private Drivetrain drivetrain = new Drivetrain();
  private Arm arm = new Arm();
  private Grabber grabber = new Grabber();
  private Gyro gyro = new Gyro();
  private PoseTracker poseTracker = new PoseTracker(drivetrain);
    
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();
  // TODO: Raf is rly dumb for this shit
  // private final SmartDashboardDB db = new SmartDashboardDB();

  public RobotContainer() {
    // this.drivetrain.setDefaultCommand(new TeleopDrive(drivetrain, driverController));
    this.drivetrain.setDefaultCommand(new RunCommand(() -> {
      drivetrain.curvatureDrive(this.driverController.getThrottle(), this.driverController.getTurn(), driverController.getSlowMode());
    }, drivetrain));

    // this.poseTracker.setDefaultCommand(new PrintCommand("Matt likes balls idk, Raf too"));

    configureButtonBindings();
  }

  private void configureButtonBindings() {

    // Grabber
    Controller.onPress(driverController.A, new InstantCommand(grabber::toggleDeploy));

    // // Balance
    // Controller.onPress(driverController.B, new Balance(drivetrain, gyro, 0));

    //slow mode
    Controller.onHold(driverController.RightTrigger, new InstantCommand(() -> driverController.setSlowMode(Mode.SLOW)));
    Controller.onRelease(driverController.RightTrigger, new InstantCommand(() -> driverController.setSlowMode(Mode.NORMAL)));
  
    // // Grid Align
    // Controller.onPress(driverController.Y, new ConditionalCommand(
    //   // on true, instantiate and schedule align command
    //   new InstantiatorCommand(() -> new GridAlign(drivetrain, poseTracker)),
    //   // on false rumble for 1 second
    //   new Rumble(driverController, Constants.GridAlign.kRumbleTime),
    //   // conditional upon a valid april tag
    //   LimelightAPI::validTargets
    // ));


    // SmartDashboard.putNumber("target anchor  angle", 30);
    // SmartDashboard.putNumber("target floating   angle", 0);

    // Arm
    // Controller.onHold(driverController.X, new MoveAnchorJoint(() -> {
    //   double desired = SmartDashboard.getNumber("target anchor  angle", 30);
    //   if(desired < 13) return 13;

    //   if(desired > 90) return 90;

    //   return desired;
    // }, arm));

    // Controller.onHold(driverController.Y, new MoveFloatingJoint(() -> {
    //   double desired = SmartDashboard.getNumber("target floating   angle", 0);
    //   if(desired < 0) return 0;

    //   if(desired > 90) return 90;

    //   return desired;
    // }, arm));


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
    return new InstantCommand(() -> {});
  }

  public void doSendables() {
  }
}
