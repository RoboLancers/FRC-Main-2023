package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.arm.commands.MoveAnchorJoint;
import frc.robot.subsystems.arm.commands.MoveFloatingJoint;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.arm.commands.Contract;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.Controller;

public class RobotContainer {
  private RobotContainer robotContainer;
  
  // Contollers
  private final Controller driverController = new Controller(0);
  private final Controller manipulatorController = new Controller(1);

  // Subsystems
    private final Arm arm = new Arm();
    private final Drivetrain drivetrain = new Drivetrain();

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();
    
  public RobotContainer() {

    this.configureButtonBindings();

    this.drivetrain.setDefaultCommand(new RunCommand(
            () -> this.drivetrain.arcadeDrive(driverController.getAxisValue(Controller.Axis.LEFT_Y), driverController.getAxisValue(Controller.Axis.RIGHT_X)),
            drivetrain));
      
      /* Add autos here */
    // autoChooser.addOption("name", auto);
  }



  
  
  private void configureButtonBindings() {
    
    // Move the arm to the ground
    manipulatorController.whenPressed(Controller.Button.X, new MoveToPos(arm, Constants.Arms.Positions.kLowAnchor, Constants.Arms.Positions.kLowFloating));
    // Move the arm to the intake shelf
    manipulatorController.whenPressed(Controller.Button.A, new MoveToPos(arm, Constants.Arms.Positions.kIntakeShelfAnchor, Constants.Arms.Positions.kIntakeShelfFloating));
    // Move to the mid node
    manipulatorController.whenPressed(Controller.Button.B, new MoveToPos(arm, Constants.Arms.Positions.kMidNodeAnchor, Constants.Arms.Positions.kMidNodeFloating));
    // Mode to mid shelf
    manipulatorController.whenPressed(Controller.Button.Y, new MoveToPos(arm, Constants.Arms.Positions.kMidShelfAnchor, Constants.Arms.Positions.kMidShelfFloating));
    // Move to high node
    manipulatorController.whenPressed(Controller.Button.RIGHT_BUMPER, new MoveToPos(arm, Constants.Arms.Positions.kHighNodeAnchor, Constants.Arms.Positions.kHighNodeFloating));
    // Move to high shelf
    manipulatorController.whenPressed(Controller.Button.LEFT_BUMPER, new MoveToPos(arm, Constants.Arms.Positions.kHighShelfAnchor, Constants.Arms.Positions.kHighShelfFloating));
    // Contract
    manipulatorController.whenPressed(Controller.Button.RIGHT_JOYSTICK_BUTTON, new Contract(arm));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}