package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
<<<<<<< HEAD
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.commands.MoveAnchorJoint;
import frc.robot.subsystems.arm.commands.MoveFloatingJoint;
import frc.robot.subsystems.Arm;

public class RobotContainer {
    private final Arm arm = new Arm();
=======
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

>>>>>>> 5704c7d07c066370bd8cca33ba46522e98627af1
  public RobotContainer() {

    configureButtonBindings();

<<<<<<< HEAD
    double[] armAngles = arm.calculateAngles(); // TODO: Add angles

    new SequentialCommandGroup(new MoveAnchorJoint((Math.PI / 2), arm), new MoveFloatingJoint(armAngles[1], arm),
    new MoveAnchorJoint(armAngles[0], arm));
=======
    this.drivetrain.setDefaultCommand(new RunCommand(
            () -> this.drivetrain.arcadeDrive(driverController.getAxisValue(XboxController.Axis.LEFT_Y), driverController.getAxisValue(XboxController.Axis.RIGHT_X)),
            drivetrain));

    /* Add autos here */
    // autoChooser.addOption("name", auto);
>>>>>>> 5704c7d07c066370bd8cca33ba46522e98627af1
  }
  
  private void configureButtonBindings() {}

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  // TODO: we should check if we have a cone or cube before calling command
}