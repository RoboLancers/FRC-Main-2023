package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.XboxController;

public class RobotContainer {

  /*   Controllers   */
  private final XboxController driverController = new XboxController(0);
  private final XboxController manipulatorController = new XboxController(1);

  /*   Subsystems   */
  private final Drivetrain drivetrain = new Drivetrain(driverController);

  

  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  public RobotContainer() {
    configureButtonBindings();

    this.drivetrain.setDefaultCommand(new RunCommand(
            () -> this.drivetrain.arcadeDrive(driverController.getAxisValue(XboxController.Axis.LEFT_Y), driverController.getAxisValue(XboxController.Axis.RIGHT_X)),
            drivetrain));

    /* Add autos here */
    // autoChooser.addOption("name", auto);
  }
  
  private void configureButtonBindings() {}

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}