package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.Grabber.Grabber;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveAnchorJoint;
import frc.robot.subsystems.arm.commands.MoveFloatingJoint;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.Controller;

public class RobotContainer {
  private final Controller driverController = new Controller(0);
  
  private final Grabber grabber = new Grabber();

  // Subsystems
    private final Arm arm = new Arm();
    private final Drivetrain drivetrain = new Drivetrain();

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();
    
  public RobotContainer() {

    this.configureButtonBindings();
  }

  private void configureButtonBindings() {
    
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

    this.drivetrain.setDefaultCommand(new RunCommand(() -> {
      this.drivetrain.arcadeDrive(
              -driverController.getLeftStickY(),
              -driverController.getRightStickX()
      );
    }, drivetrain));

    Controller.onPress(driverController.A, new InstantCommand(grabber::toggleDeploy));

    SmartDashboard.putNumber("targetAngle", 13.0);
    
    Controller.onHold(driverController.X, new MoveAnchorJoint(14.0, arm));

    Controller.onHold(driverController.Y, new RunCommand(() -> {
      arm.floatingJointMotor.set(-0.15);
    }));
  }

  public Command getAutonomousCommand() {
    return new InstantCommand(() -> {});
  }
  
  public void doSendables() {
  }
}
