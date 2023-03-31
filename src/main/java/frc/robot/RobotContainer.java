package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.sidecamera.SideCamera; 
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.TeleopDrive;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.arm.commands.RunToSetpoints;
import frc.robot.util.Controller;
import frc.robot.util.DriverController;
import frc.robot.util.ManipulatorController;
import frc.robot.util.DriverController.Mode;
import frc.robot.util.enums.ArmMode;

public class RobotContainer {
  private final DriverController driverController = new DriverController(0);
  private final ManipulatorController manipulatorController = new ManipulatorController(1);

  private Gyro gyro = new Gyro();
  private Drivetrain drivetrain = new Drivetrain(gyro);
  private Arm arm = new Arm();
  private Intake intake = new Intake();
  private SideCamera sideCamera = new SideCamera(0, 1);

  private final AutoPicker autoPicker;

  public RobotContainer() {
    this.autoPicker = new AutoPicker(drivetrain, arm, gyro, intake); 

    this.drivetrain.setDefaultCommand(new TeleopDrive(drivetrain, driverController));

    this.arm.setDefaultCommand(new RunToSetpoints(arm));

    this.intake.setDefaultCommand(new RunCommand(intake::off, intake));

    configureButtonBindings();
    doSendables();
  }

  private void configureButtonBindings() {
    // driver slow mode
    Controller.onHold(driverController.RightBumper, new InstantCommand(() -> driverController.setSlowMode(Mode.SLOW)));
    Controller.onRelease(driverController.RightBumper, new InstantCommand(() -> driverController.setSlowMode(Mode.NORMAL)));

    // driver intake
    Controller.onHold(driverController.RightTrigger, new RunCommand(intake::intakeFast, intake));
    Controller.onHold(driverController.LeftTrigger, new RunCommand(intake::outtakeFast, intake));
    // manipulator intake
    Controller.onHold(manipulatorController.intakeElementTriggerFast, new RunCommand(intake::intakeFast));
    Controller.onHold(manipulatorController.outtakeElementTriggerFast, new RunCommand(intake::outtakeFast));
    Controller.onHold(manipulatorController.intakeElementTriggerSlow, new RunCommand(intake::intakeSlow));
    Controller.onHold(manipulatorController.outtakeElementTriggerSlow, new RunCommand(intake::outtakeSlow));

    // manipulator toggle cube
    Controller.onPress(manipulatorController.RightBumper, new InstantCommand(() -> {
      this.arm.armMode = ArmMode.CUBE;
    }));
    // manipulator toggle cone
    Controller.onPress(manipulatorController.LeftBumper, new InstantCommand(() -> {
      this.arm.armMode = ArmMode.CONE;
    }));

    /*
      Manipulator Arm State
    */
    // ground
    Controller.onPress(manipulatorController.A, new MoveToPos(arm, Constants.Arm.Position.GROUND));
    // contract
    Controller.onPress(manipulatorController.B, new MoveToPos(arm, Constants.Arm.Position.CONTRACTED));
    // mid - 
    Controller.onPress(manipulatorController.X, new MoveToPos(arm, () -> arm.armMode == ArmMode.CUBE ? Constants.Arm.Position.MID_CUBE : Constants.Arm.Position.MID_CONE));
    // high
    Controller.onPress(manipulatorController.Y, new MoveToPos(arm, () -> arm.armMode == ArmMode.CUBE ? Constants.Arm.Position.HIGH_CUBE : Constants.Arm.Position.HIGH_CONE));
    
    // station
    Controller.onPress(driverController.A, new MoveToPos(arm, Constants.Arm.Position.STATION));
    // shelf
    Controller.onPress(driverController.B, new MoveToPos(arm, Constants.Arm.Position.SHELF));
    // contract
    Controller.onPress(driverController.X, new MoveToPos(arm, Constants.Arm.Position.CONTRACTED));
  }

  public Command getAutonomousCommand() {
    return this.autoPicker.getAutoChooser().getSelected(); 
  }

  public void doSendables() {
    SmartDashboard.putData(this.autoPicker.getAutoChooser());
  }
}