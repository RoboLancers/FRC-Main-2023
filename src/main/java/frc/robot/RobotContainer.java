package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;

public class RobotContainer {
  public RobotContainer() {
    configureButtonBindings();
  }
  
  private void configureButtonBindings() {}

  public Command getAutonomousCommand() {
    return new PrintCommand("No Auto Written Yet");
  }

  // TODO: we should check if we have a cone or cube before calling command
}