package frc.robot;

import java.util.ResourceBundle.Control;

import org.bananasamirite.robotmotionprofile.Waypoint;
import org.opencv.osgi.OpenCVInterface;
import org.opencv.osgi.OpenCVNativeLoader;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.simulation.AddressableLEDSim;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.leds.addressable.LED;
import frc.robot.subsystems.leds.addressable.patterns.EscalatingRandomColorPattern;
import frc.robot.subsystems.leds.addressable.patterns.LEDPattern;
import frc.robot.subsystems.leds.addressable.patterns.SplitPattern;
import frc.robot.subsystems.leds.addressable.patterns.MorseCodePattern;
import frc.robot.subsystems.leds.addressable.patterns.RainbowPattern;
import frc.robot.subsystems.sidecamera.SideCamera; 
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.TeleopDrive;
import frc.robot.subsystems.drivetrain.commands.TurnBy;
import frc.robot.subsystems.drivetrain.commands.TurnToAngle;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.arm.commands.RunToSetpoints;
import frc.robot.util.Controller;
import frc.robot.util.DriverController;
import frc.robot.util.InstantiatorCommand;
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
  private LED led = new LED();

  private final AutoPicker autoPicker; 
  
  private final SendableChooser<LEDPattern> patternChooser = new SendableChooser<>(); 

  //Camera 
  // private UsbCamera intakeCamera = new UsbCamera("Intake Camera ", 0);
  // private UsbCamera stationCamera = new UsbCamera("Station Camera ", 1);
  // private enum CameraMode { INTAKE, STATION }
  // private CameraMode cameraMode = CameraMode.INTAKE;
  // private final NetworkTableEntry cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");


  public RobotContainer() {
    // AddressableLEDSim ledSim = new AddressableLEDSim(led.getLed()); // <-- simulation purposes
    // led.setPattern(Constants.LEDs.Patterns.kBalanceFinished);

    this.autoPicker = new AutoPicker(drivetrain, arm, gyro, intake); 

    this.drivetrain.setDefaultCommand(new TeleopDrive(drivetrain, driverController));

    this.arm.setDefaultCommand(new RunToSetpoints(arm));

    this.intake.setDefaultCommand(new RunCommand(intake::off, intake));

    // CameraServer.startAutomaticCapture(0);
    // this.cameraSelection.setString(intakeCamera.getName());

    configureButtonBindings();
    configurePatterns(); 
    doSendables();
  }

  private void configureButtonBindings() {
    // driver slow mode
    Controller.onHold(driverController.RightBumper, new InstantCommand(() -> driverController.setSlowMode(Mode.SLOW)));
    Controller.onRelease(driverController.RightBumper, new InstantCommand(() -> driverController.setSlowMode(Mode.NORMAL)));

    // TODO: test this, driver camera switch
    // Controller.onPress(driverController.LeftBumper, new InstantCommand(() -> setCamera()));

    // driver intake
    // Controller.onHold(driverController.RightTrigger, new RunCommand(intake::intakeFast, intake));
    // Controller.onHold(driverController.LeftTrigger, new RunCommand(intake::outtakeFast, intake));
    // manipulator intake
    Controller.onHold(manipulatorController.intakeElementTriggerFast, new RunCommand(intake::intakeFast));
    Controller.onHold(manipulatorController.outtakeElementTriggerFast, new RunCommand(intake::outtakeFast));
    Controller.onHold(manipulatorController.intakeElementTriggerSlow, new RunCommand(intake::intakeSlow));
    Controller.onHold(manipulatorController.outtakeElementTriggerSlow, new RunCommand(intake::outtakeSlow));

    // manipulator toggle cube
    Controller.onPress(manipulatorController.RightBumper, new InstantCommand(() -> {
      this.arm.armMode = ArmMode.CUBE;
      this.led.cube();
    }));
    // manipulator toggle cone
    Controller.onPress(manipulatorController.LeftBumper, new InstantCommand(() -> {
      this.arm.armMode = ArmMode.CONE;
      this.led.cone();
    }));

    /*
      Manipulator Arm State
    */
    // ground
    Controller.onPress(manipulatorController.A, new MoveToPos(arm, Constants.Arm.Position.GROUND));
    // contract
    Controller.onPress(manipulatorController.B, new MoveToPos(arm, Constants.Arm.Position.CONTRACTED));
    // mid
    Controller.onPress(manipulatorController.X, new InstantiatorCommand(() -> {
      return new MoveToPos(arm, () -> arm.armMode == ArmMode.CUBE ? Constants.Arm.Position.MID_CUBE : (arm.isAt(Constants.Arm.Position.MID_CONE) ? Constants.Arm.Position.MID_CONE_AIMING : Constants.Arm.Position.MID_CONE));
    }));
    // high
    Controller.onPress(manipulatorController.Y, new InstantiatorCommand(() -> {
      return new MoveToPos(arm, () -> arm.armMode == ArmMode.CUBE ? Constants.Arm.Position.HIGH_CUBE : (arm.isAt(Constants.Arm.Position.HIGH_CONE) ? Constants.Arm.Position.HIGH_CONE_AIMING : Constants.Arm.Position.HIGH_CONE));
    }));
    // station
    Controller.onPress(driverController.A, new MoveToPos(arm, Constants.Arm.Position.STATION));
    // shelf
    Controller.onPress(driverController.B, new MoveToPos(arm, Constants.Arm.Position.SHELF));

    Controller.onPress(driverController.X, new InstantCommand(() -> {
      gyro.reset();
    }, gyro));



    // Controller.onPress(driverController.LeftTrigger, new TurnBy(drivetrain, 90));
    // Controller.onPress(driverController.RightTrigger, new TurnBy(drivetrain, -90));

    // TODO: test this stuff and retune
    // driver turning
    // Controller.onPress(driverController.X, new TurnToAngle(drivetrain, 0)); // align with grid
    // Controller.onPress(driverController.Y, new TurnToAngle(drivetrain, 90 * (DriverStation.getAlliance() == Alliance.Red ? -1 : 1))); // align with park

    SmartDashboard.putNumber("turn by", 0); 

    Controller.onBothPress(driverController.LeftTrigger, driverController.RightTrigger, new TurnToAngle(drivetrain, () -> SmartDashboard.getNumber("turn by", 0)));
    // Controller.onPress(driverController., getAutonomousCommand());

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

  // public void setCamera(){
  //   if(cameraMode == CameraMode.INTAKE){
  //     cameraMode = CameraMode.STATION;
  //     intakeCamera.setConnectionStrategy(ConnectionStrategy.kForceClose);
  //     stationCamera = CameraServer.startAutomaticCapture(1);
  //     cameraSelection.setString(stationCamera.getName());
  //   }
  //   else if(cameraMode == CameraMode.STATION){
  //     cameraMode = CameraMode.INTAKE;
  //     stationCamera.setConnectionStrategy(ConnectionStrategy.kForceClose);
  //     CameraServer.startAutomaticCapture(0);
  //     cameraSelection.setString(intakeCamera.getName());
  //   }
  // }

  public void configurePatterns() {

    Constants.LEDs.Patterns.kTeleop
      .addCondition(
        () -> arm.armMode == ArmMode.CONE, 
        Constants.LEDs.Patterns.kCone
        )
      .addCondition(
        () -> arm.armMode == ArmMode.CUBE, 
        Constants.LEDs.Patterns.kCube
      );

    this.patternChooser.addOption("Idle", Constants.LEDs.Patterns.kIdle);
    this.patternChooser.addOption("Balance Done", Constants.LEDs.Patterns.kBalanceFinished);
    this.patternChooser.addOption("Tipped", Constants.LEDs.Patterns.kDead);
    this.patternChooser.addOption("Teleop", Constants.LEDs.Patterns.kTeleop);
    // this.patternChooser.addOption("Cube", Constants.LEDs.Patterns.kCube);
    this.patternChooser.addOption("Tipped Alternate", Constants.LEDs.Patterns.kDeadAlternate);
    this.patternChooser.addOption("custom teleop", new SplitPattern().addSplit(0, 5, Constants.LEDs.Patterns.kTeleop).addSplit(5, 10, Constants.LEDs.Patterns.kIdle).addSplit(10, 15, Constants.LEDs.Patterns.kDead).addSplit(15, 20, Constants.LEDs.Patterns.kBalanceFinished));
  }

  public Command getAutonomousCommand() {
    return this.autoPicker.getAutoChooser().getSelected(); 
  }

  public void doSendables() {
    SmartDashboard.putData(this.autoPicker.getAutoChooser());
    SmartDashboard.putData(this.autoPicker.getScoringPosition1());
    SmartDashboard.putData(this.autoPicker.getScoringPosition2());
    SmartDashboard.putData(this.patternChooser);
  }

  public void periodic() {
    this.led.setPattern(this.patternChooser.getSelected());
  }

}