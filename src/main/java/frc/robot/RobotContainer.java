package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.grabber.Grabber;
import frc.robot.subsystems.grabber.Pneumatics;
import frc.robot.util.Controller;

public class RobotContainer {
private final Controller driverController = new Controller(0); 
  private final Controller manipulatorController = new Controller(1);
  private final Drivetrain drivetrain = new Drivetrain(); 
  private final Grabber grabber = new Grabber();
  private final Pneumatics pneumatics = new Pneumatics();

  private final SendableChooser<Command> autoChooser = new SendableChooser<>(); 

  public RobotContainer() {
    configureButtonBindings();

    this.drivetrain.setDefaultCommand(new RunCommand(() -> {
      this.drivetrain.arcadeDrive(
              -driverController.getLeftStickY(),
              -driverController.getRightStickX()
      );
    }, drivetrain));

    /* Add autos here */
    // autoChooser.addOption("name", auto);
  }

  public void configureButtonBindings() {

  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  public void doSendables() {
  } 

}