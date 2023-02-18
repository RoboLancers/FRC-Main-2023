package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.Grabber.Grabber;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.Controller;

public class RobotContainer {
  private final Controller driverController = new Controller(0);
  
  private final Grabber grabber = new Grabber();

  private final Drivetrain drivetrain = new Drivetrain();

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

    Controller.onPress(driverController.A, new InstantCommand(grabber::toggleDeploy));
  }

  private void configureButtonBindings(){

  }

  public Command getAutonomousCommand() {
    return new InstantCommand();
  }

  public void doSendables(){
    SmartDashboard.putString("Solenoid value", grabber.getPosition().name());
  } 

}