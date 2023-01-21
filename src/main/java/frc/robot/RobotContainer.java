package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.commands.MoveAnchorJoint;
import frc.robot.subsystems.arm.commands.MoveFloatingJoint;
import frc.robot.subsystems.Arm;

public class RobotContainer {
    private final Arm arm = new Arm();
  public RobotContainer() {

    configureButtonBindings();

    double[] armAngles = arm.calculateAngles(); // TODO: Add angles

    new SequentialCommandGroup(new MoveAnchorJoint((Math.PI / 2), arm), new MoveFloatingJoint(armAngles[1], arm),
    new MoveAnchorJoint(armAngles[0], arm));
  }
  
  private void configureButtonBindings() {}

  public Command getAutonomousCommand() {
    return new PrintCommand("No Auto Written Yet");
  }

  // TODO: we should check if we have a cone or cube before calling command
}