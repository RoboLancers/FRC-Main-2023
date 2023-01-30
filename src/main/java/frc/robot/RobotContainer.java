package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.Controller;

public class RobotContainer {

  /*   Controllers   */
  private final Controller driverController = new Controller(0);
  private final Controller manipulatorController = new Controller(1);

  /*   Subsystems   */
  private final Drivetrain drivetrain = new Drivetrain();

  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  public RobotContainer() {
    configureButtonBindings();

    this.drivetrain.setDefaultCommand(new RunCommand(() -> {
      this.drivetrain.arcadeDrive(
        driverController.getLeftStickY(),
        driverController.getRightStickX()
      );
    }, drivetrain));

    /* Add autos here */
    // autoChooser.addOption("name", auto);
  }
  
  private void configureButtonBindings() {
    /* Controller usage example for on press and on hold */

    Controller.onPress(driverController.A, new InstantCommand(() -> {}));

    Controller.onHold(driverController.B, new RunCommand(() -> {}));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}