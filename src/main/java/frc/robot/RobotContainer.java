package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.Controller;

public class RobotContainer {
  private final XboxController manipulatorController = new XboxController(0);
  private final Grabber grabber = new Grabber();
  //private final UseGrabber uGrabber = new UseGrabber(grabber);
  private final Pneumatics pneumatics = new Pneumatics();

  public RobotContainer() {
    configureButtonBindings();

    this.drivetrain.setDefaultCommand(new RunCommand(() -> {
      this.drivetrain.arcadeDrive(
              -driverController.getLeftStickY(),
              -driverController.getRightStickX()
      );
    }, drivetrain));

    // Controller.onPress(driverController.B, new InstantCommand(() -> {
    //   this.drivetrain.resetOdometry(new Pose2d(5, 5, new Rotation2d()));
    // }));
    
    manipulatorController.whenPressed(XboxController.Button.A, new InstantCommand(() -> {
      grabber.toggleDeploy();
    }));

    /* Add autos here */
    // autoChooser.addOption("name", auto);
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  public void doSendables(){
    SmartDashboard.putString("Solenoid value", grabber.getPosition().name());
  } 

}