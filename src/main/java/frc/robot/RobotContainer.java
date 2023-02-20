package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.grabber.Grabber;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.Controller;

public class RobotContainer {
  private Controller driverController = new Controller(0); 
  private Controller manipulatorController = new Controller(1);

  private Drivetrain drivetrain = new Drivetrain();

  private Grabber grabber = new Grabber();

  public RobotContainer() {
    configureButtonBindings();

    this.drivetrain.setDefaultCommand(new RunCommand(() -> {
      this.drivetrain.arcadeDrive(
        -driverController.getLeftStickY(),
        -driverController.getRightStickX()
      );
    }, drivetrain));
  }

  private void configureButtonBindings(){
    // Grabber
    Controller.onPress(driverController.A, new InstantCommand(grabber::toggleDeploy));
  }

  public Command getAutonomousCommand() {
    return new InstantCommand();
  }

  public void doSendables() {
  }
}