package frc.robot;

import org.bananasamirite.robotmotionprofile.Waypoint;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.gyro.commands.Balance;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.MoveBackward;
import frc.robot.subsystems.drivetrain.commands.MoveForward;
import frc.robot.subsystems.drivetrain.commands.TeleopDrive;
import frc.robot.subsystems.drivetrain.commands.TurnBy;
import frc.robot.subsystems.drivetrain.commands.TurnToAngle;
import frc.robot.commands.BottomLaneAuto;
import frc.robot.commands.MidLaneAuto;
import frc.robot.commands.TopLaneAuto;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.arm.commands.RunToSetpoints;
import frc.robot.subsystems.arm.commands.Zero;
import frc.robot.util.Controller;
import frc.robot.util.ControllerUtils;
import frc.robot.util.DriverController;
import frc.robot.util.InstantiatorCommand;
import frc.robot.util.ManipulatorController;
import frc.robot.util.DriverController.Mode;

public class RobotContainer {
  private final DriverController driverController = new DriverController(0);
  private final ManipulatorController manipulatorController = new ManipulatorController(1);

  private Drivetrain drivetrain = new Drivetrain();
  private Arm arm = new Arm();
  private Intake intake = new Intake();
  private Gyro gyro = new Gyro();
    
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  public RobotContainer() {
      this.drivetrain.setDefaultCommand(new TeleopDrive(drivetrain, driverController));

      this.arm.setDefaultCommand(new RunToSetpoints(arm));

      this.intake.setDefaultCommand(new RunCommand(intake::off, intake));

      CameraServer.startAutomaticCapture(); 

      configureButtonBindings();
      configureAutos(); 
      doSendables();
    }

  private void configureButtonBindings() {
    // driver slow mode
    Controller.onHold(driverController.RightBumper, new InstantCommand(() -> driverController.setSlowMode(Mode.SLOW)));
    Controller.onRelease(driverController.RightBumper, new InstantCommand(() -> driverController.setSlowMode(Mode.NORMAL)));

    // zero the arm
    // Controller.onPress(manipulatorController.RightTrigger, new Zero(arm));

    // driver intake
    Controller.onHold(driverController.RightTrigger, new RunCommand(intake::intakeFast, intake));
    Controller.onHold(driverController.LeftTrigger, new RunCommand(intake::outtakeFast, intake));
    // manipulator intake
    Controller.onHold(manipulatorController.intakeElementTriggerFast, new RunCommand(intake::intakeFast));
    Controller.onHold(manipulatorController.outtakeElementTriggerFast, new RunCommand(intake::outtakeFast));
    Controller.onHold(manipulatorController.intakeElementTriggerSlow, new RunCommand(intake::intakeSlow));
    Controller.onHold(manipulatorController.outtakeElementTriggerSlow, new RunCommand(intake::outtakeSlow));

    // manipulator toggle cube
    Controller.onPress(manipulatorController.RightBumper, new InstantCommand(() -> { this.arm.armMode = true; }));
    // manipulator toggle cone
    Controller.onPress(manipulatorController.LeftBumper, new InstantCommand(() -> { this.arm.armMode = false; }));

    /*
      Manipulator Arm State
    */
    // ground
    Controller.onPress(manipulatorController.A, new MoveToPos(arm, Constants.Arm.Position.GROUND));
    // contract
    Controller.onPress(manipulatorController.B, new MoveToPos(arm, Constants.Arm.Position.CONTRACTED));
    // mid
    Controller.onPress(manipulatorController.X, new InstantiatorCommand(() -> {
      return new MoveToPos(arm, () -> arm.armMode ? Constants.Arm.Position.MID_CUBE : (arm.isAt(Constants.Arm.Position.MID_CONE) ? Constants.Arm.Position.MID_CONE_AIMING : Constants.Arm.Position.MID_CONE));
    }));
    // high
    Controller.onPress(manipulatorController.Y, new InstantiatorCommand(() -> {
      return new MoveToPos(arm, () -> arm.armMode ? Constants.Arm.Position.HIGH_CUBE : (arm.isAt(Constants.Arm.Position.HIGH_CONE) ? Constants.Arm.Position.HIGH_CONE_AIMING : Constants.Arm.Position.HIGH_CONE));
    }));
    // station
    Controller.onPress(driverController.A, new MoveToPos(arm, Constants.Arm.Position.STATION));
    // shelf
    Controller.onPress(driverController.B, new MoveToPos(arm, Constants.Arm.Position.SHELF));






    /*
    
      TESTING ONLY

    */

    //  * dynamic arm setpoint
    // SmartDashboard.putNumber("anchor-setpoint", 16.0);
    // SmartDashboard.putNumber("floating-setpoint", 45.0);
    // Controller.onPress(driverController.X, new MoveToPos(
    //   arm,
    //   () -> ControllerUtils.clamp(SmartDashboard.getNumber("anchor-setpoint", 0.0), 16.0, 95.0),
    //   () -> ControllerUtils.clamp(SmartDashboard.getNumber("floating-setpoint", 0.0), 22.0, 180.0)
    // ));

    // * balance
    // Controller.onPress(driverController.Y, new Balance(drivetrain, gyro, 0)); 

    // SmartDashboard.putNumber("turn by", 30); 
    // Controller.onPress(driverController.XX, new TurnBy(drivetrain, () -> ControllerUtils.clamp(SmartDashboard.getNumber("turn by", 30), -90, 90)));
  }

  public void configureAutos() {
    // ! Pick one of these in comp
    autoChooser.addOption("No Bump Lane High Cube", new TopLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.HIGH_CUBE));
    autoChooser.addOption("No Bump Lane Mid Cube", new TopLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.MID_CUBE));
    autoChooser.addOption("No Bump Lane High Cone", new TopLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.HIGH_CONE));
    autoChooser.addOption("No Bump Lane Mid Cone", new TopLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.MID_CONE));
    autoChooser.addOption("No Bump Lane Low", new TopLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.LOW_CUBE));

    autoChooser.addOption("Balance High Cube", new MidLaneAuto(drivetrain, gyro, arm, intake, Constants.Arm.ScoringPosition.HIGH_CUBE));
    autoChooser.addOption("Balance Mid Cube", new MidLaneAuto(drivetrain, gyro, arm, intake, Constants.Arm.ScoringPosition.MID_CUBE));
    autoChooser.addOption("Balance High Cone", new MidLaneAuto(drivetrain, gyro, arm, intake, Constants.Arm.ScoringPosition.HIGH_CONE));
    autoChooser.addOption("Balance Mid Cone", new MidLaneAuto(drivetrain, gyro, arm, intake, Constants.Arm.ScoringPosition.MID_CONE));
    autoChooser.addOption("Balance Low", new MidLaneAuto(drivetrain, gyro, arm, intake, Constants.Arm.ScoringPosition.LOW_CUBE));

    // TODO: this is fucking stupid
    autoChooser.addOption("Bump Lane High Cube", new BottomLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.HIGH_CUBE));
    autoChooser.addOption("Bump Lane Mid Cube", new BottomLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.MID_CUBE));
    autoChooser.addOption("Bump Lane High Cone", new BottomLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.HIGH_CONE));
    autoChooser.addOption("Bump Lane Mid Cone", new BottomLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.MID_CONE));
    autoChooser.addOption("Bump Lane Low", new BottomLaneAuto(drivetrain, arm, intake, Constants.Arm.ScoringPosition.LOW_CUBE));

    /*
    
      TESTING ONLY

    */

        // autoChooser.addOption("Move Forward", new MoveForward(drivetrain, 3, 1, 0.5));
    // autoChooser.addOption("Move Backward", new MoveBackward(drivetrain, 3, 1, 0.5));
    // autoChooser.addOption("test pathfollowing", Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
    //   new Waypoint(0, 0, 0, 1, 1),
    //   new Waypoint(1, 1, 0, 1, 1), 
    //   new Waypoint(2, 0, Math.toRadians(90), 1, 1)
    // }, new TrajectoryConfig(1, 0.5)));
    // autoChooser.addOption("test drivetrain", new TurnToAngle(drivetrain, () -> SmartDashboard.getNumber("turn angle", 0)));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected(); 
  }

  public void doSendables() {
    SmartDashboard.putData(autoChooser);
  }
}
