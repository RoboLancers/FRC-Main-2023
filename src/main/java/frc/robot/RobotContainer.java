package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.arm.commands.MoveAnchorJoint;
import frc.robot.subsystems.arm.commands.MoveFloatingJoint;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.Contract;

import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.XboxController;

public class RobotContainer {
  this.configureButtonBindings();
  private RobotContainer robotContainer;
  
  // Contollers
  private final XboxController driverController = new XboxController(0);
  private final XboxController manipulatorController = new XboxController(1);

  // Subsystems
    double[] armAngles;  
    private final Arm arm = new Arm();
    private final Drivetrain drivetrain = new Drivetrain(driverController);

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();
    
  public RobotContainer() {

    this.configureButtonBindings();

    armAngles = arm.calculateAngles(0.1, 0.2); // TODO: Add angles

    this.drivetrain.setDefaultCommand(new RunCommand(
            () -> this.drivetrain.arcadeDrive(driverController.getAxisValue(XboxController.Axis.LEFT_Y), driverController.getAxisValue(XboxController.Axis.RIGHT_X)),
            drivetrain));
      
      /* Add autos here */
    // autoChooser.addOption("name", auto);
    
  }
  
  private void configureButtonBindings() {
    
    manipulatorController.whenPressed(XboxController.Button.kX, new ParallelCommandGroup();

  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  // TODO: we should check if we have a cone or cube before calling command
}