package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.MoveBackward;
import frc.robot.subsystems.drivetrain.commands.MoveForward;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.commands.BottomLaneAuto;
import frc.robot.commands.MidLaneAuto;
import frc.robot.commands.TopLaneAuto;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.util.Controller;
import frc.robot.util.DriverController; 
import frc.robot.util.ManipulatorController;
import frc.robot.util.DriverController.Mode;

public class RobotContainer {
  /* Controllers */
  private final DriverController driverController = new DriverController(0);
  private final ManipulatorController manipulatorController = new ManipulatorController(1);

  /* Subsystems */
  private Drivetrain drivetrain = new Drivetrain();
  private Arm arm = new Arm();
  private Intake intake = new Intake();
  private Gyro gyro = new Gyro();
  private PoseTracker poseTracker = new PoseTracker(drivetrain);
    
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();
  // TODO: Raf is rly dumb for this shit
  // private final SmartDashboardDB db = new SmartDashboardDB();

  public RobotContainer() {
    // this.drivetrain.setDefaultCommand(new TeleopDrive(drivetrain, driverController));
    this.drivetrain.setDefaultCommand(new RunCommand(() -> {
      drivetrain.curvatureDrive(this.driverController.getThrottle(), this.driverController.getTurn(), this.driverController.getSlowMode());
    }, drivetrain));

    CameraServer.startAutomaticCapture(); 

    // command = Constants.Trajectory.trajectoryCreator.createCommand(drivetrain,
    //         new Waypoint[] {
    //                 new Waypoint(0, 0, 0, 2.734564202601426, 1),
    //                 new Waypoint(1.594, 0.798, Math.toRadians(1.145), 2.4, 1), 
    //                 // new Waypoint(
    //                 //   2, 1, Math.toRadians(45), 1.85, 1
    //                 // )
    //         }, new TrajectoryConfig(1, 0.2));
    // SmartDashboard.putNumber("Angular Setpoint", 0);
    // command = new TurnToAngle(drivetrain, () -> SmartDashboard.getNumber("Angular Setpoint", 0));

            configureButtonBindings();
            configureAutos(); 
            doSendables();
        }
    // command = new TurnTuner(drivetrain);
    // try {
    //   command = RobotTrajectoryCommand.fromFile(drivetrain, Filesystem.getDeployDirectory().toPath().resolve("auto6.json").toFile());
    // } catch (IOException e) {
    //   // TODO Auto-generated catch block
    //   e.printStackTrace();
    // }

    // this.poseTracker.setDefaultCommand(new PrintCommand("Matt likes balls idk, Raf too"));

  private void configureButtonBindings() {
    // intake
    Controller.onHold(driverController.RightTrigger, new RunCommand(intake::outtakeFast, intake));
    Controller.onHold(driverController.LeftTrigger, new RunCommand(intake::intakeFast, intake));
    
    Controller.onHold(manipulatorController.intakeElementTriggerFast, new RunCommand(intake::intakeFast));
    Controller.onHold(manipulatorController.outtakeElementTriggerFast, new RunCommand(intake::outtakeFast));
    Controller.onHold(manipulatorController.intakeElementTriggerSlow, new RunCommand(intake::intakeSlow));
    Controller.onHold(manipulatorController.outtakeElementTriggerSlow, new RunCommand(intake::outtakeSlow));

    // toggle cube
    Controller.onPress(manipulatorController.RightBumper, new InstantCommand(() -> { this.arm.armMode = true; }));
    // toggle cone
    Controller.onPress(manipulatorController.LeftBumper, new InstantCommand(() -> { this.arm.armMode = false; }));

    // ground
    Controller.onPress(manipulatorController.A, new MoveToPos(arm, Constants.Arm.Position.GROUND));
    // contract
    Controller.onPress(manipulatorController.B, new MoveToPos(arm, Constants.Arm.Position.CONTRACTED));
    // mid
    Controller.onPress(manipulatorController.X, new MoveToPos(arm, Constants.Arm.Position.MID_CONE));
    // high
    Controller.onPress(manipulatorController.Y, new ConditionalCommand(
      // cube (high)
      new MoveToPos(arm, Constants.Arm.Position.HIGH_CUBE),
      // cone (high)
      new MoveToPos(arm, Constants.Arm.Position.HIGH_CONE),
      () -> this.arm.armMode
    ));
    // shelf
    Controller.onPress(manipulatorController.dPadDown, new MoveToPos(arm, Constants.Arm.Position.SHELF));

    // dynamic for tuning
    // SmartDashboard.putNumber("anchor-setpoint", 13.0);
    // SmartDashboard.putNumber("floating-setpoint", 22.0);
    // Controller.onPress(manipulatorController.X, new MoveToPos(
    //   arm,
    //   () -> ControllerUtils.clamp(SmartDashboard.getNumber("anchor-setpoint", 0.0), 13.0, 95.0),
    //   () -> ControllerUtils.clamp(SmartDashboard.getNumber("floating-setpoint", 0.0), 22.0, 180.0)
    // ));

    // slow mode
    Controller.onHold(driverController.RightBumper, new InstantCommand(() -> driverController.setSlowMode(Mode.SLOW)));
    Controller.onRelease(driverController.RightBumper, new InstantCommand(() -> driverController.setSlowMode(Mode.NORMAL)));

    // auto steer
    // Controller.onPress(driverController.A, new InstantCommand(() -> {
    //   drivetrain.isAutoSteer = true; 
    // }));
    // Controller.onPressCancel(driverController.A, new InstantCommand(() -> {
    //   drivetrain.isAutoSteer = false; 
    // }));
  }

  public void configureAutos() {
    autoChooser.addOption("Move Forward", new MoveForward(drivetrain, 3, 1, 0.5));
    autoChooser.addOption("Move Backward", new MoveBackward(drivetrain, 3, 1, 0.5));
    autoChooser.addOption("Top Auto High Cube", new TopLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.HIGH_CUBE));
    autoChooser.addOption("Mid Auto High Cube", new MidLaneAuto(drivetrain, gyro, arm, intake, Constants.Arm.ScoringPosition.HIGH_CUBE));
    autoChooser.addOption("Bottom Auto High Cube", new BottomLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.HIGH_CUBE));
  }

  public Command getAutonomousCommand() {
    // return new InstantCommand(() -> {});
    return autoChooser.getSelected(); 
  }

  public void doSendables() {
    SmartDashboard.putData(autoChooser);
  }
}
