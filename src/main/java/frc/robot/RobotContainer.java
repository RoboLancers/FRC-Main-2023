package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.Subsystems.Grabber.Grabber;
import frc.robot.Subsystems.Grabber.commands.UseGrabber;
import frc.robot.util.XboxController;
import frc.robot.util.XboxController.Button;

public class RobotContainer {
  private final XboxController manipulatorController = new XboxController(1);
  private final Grabber grabber = new Grabber();
  private final UseGrabber uGrabber = new UseGrabber(grabber);
  
  public RobotContainer() {
    configureButtonBindings();
  }
  
  private void configureButtonBindings() {
    manipulatorController.whenPressed(Button.X, uGrabber);
  }

  public Command getAutonomousCommand() {
    return new PrintCommand("No Auto Written Yet");
  }
}