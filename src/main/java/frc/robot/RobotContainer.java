package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.grabber.Grabber;
import frc.robot.subsystems.gyro.Balance;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.commands.GridAlign;
import frc.robot.commands.Rumble;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveAnchorJoint;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.TeleopDrive;
import frc.robot.util.Controller;
import frc.robot.util.InstantiatorCommand;
import frc.robot.util.limelight.LimelightAPI;

public class RobotContainer {
  private Controller driverController = new Controller(0); 
  private Controller manipulatorController = new Controller(1);

  private Drivetrain drivetrain = new Drivetrain();
  private Arm arm = new Arm();
  private Grabber grabber = new Grabber();
  private Gyro gyro = new Gyro();
  private PoseTracker poseTracker = new PoseTracker(drivetrain);
    
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  public RobotContainer() {
    this.drivetrain.setDefaultCommand(new TeleopDrive(drivetrain, driverController));
  }

  private void configureButtonBindings(){
    // Grabber
    Controller.onPress(driverController.A, new InstantCommand(grabber::toggleDeploy));

    // Balance
    Controller.onPress(driverController.B, new Balance(drivetrain, gyro, 0));

    // Grid Align
    Controller.onPress(driverController.Y, new ConditionalCommand(
      // on true, instantiate and schedule align command
      new InstantiatorCommand(() -> new GridAlign(drivetrain, poseTracker)),
      // on false rumble for 1 second
      new Rumble(driverController, Constants.GridAlign.kRumbleTime),
      // conditional upon a valid april tag
      LimelightAPI::validTargets
    ));

    // Arm
    // TODO: choose our desired angle more carefully when we actually test so we don't break anything
    Controller.onHold(driverController.X, new MoveAnchorJoint(0, arm));
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
