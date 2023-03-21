package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.SparkMaxAlternateEncoder.Type;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Constants;
import frc.robot.Constants.Arm.Position;

public class Arm extends SubsystemBase {
   public CANSparkMax anchorMotor, floatingMotor;
   public RelativeEncoder anchorEncoder, floatingEncoder;
   public SparkMaxPIDController anchorPIDController, floatingPIDController;

   public double anchorSetpoint = Constants.Arm.Anchor.kContracted;
   public double floatingSetpoint = Constants.Arm.Floating.kContracted;

   public boolean armMode = true; // true: cube, false: cone

   // TODO: port
   public DigitalInput anchorLimitSwitch;

   public Arm() {
      this.anchorMotor = new CANSparkMax(Constants.Arm.Ports.kAnchorPort, CANSparkMax.MotorType.kBrushless);
      this.floatingMotor = new CANSparkMax(Constants.Arm.Ports.kFloatingPort, CANSparkMax.MotorType.kBrushless);
      this.configureMotors();

      this.anchorEncoder = this.anchorMotor.getEncoder(); ; // this.anchorMotor.getAlternateEncoder(Type.kQuadrature, 8192); 
      this.floatingEncoder = this.floatingMotor.getAlternateEncoder(Type.kQuadrature, 8192); // this.floatingMotor.getEncoder();
      this.configureEncoders();

      this.anchorPIDController = this.anchorMotor.getPIDController(); 
      this.floatingPIDController = this.floatingMotor.getPIDController();
      this.configureControllers();

      this.anchorLimitSwitch = new DigitalInput(Constants.Arm.Ports.kAnchorLimitSwitchPort);

      // this.initTuneControllers();
   }

   public void toggleSoftLimits(boolean shouldLimit) {
      this.anchorMotor.enableSoftLimit(SoftLimitDirection.kReverse, shouldLimit);
      this.anchorMotor.enableSoftLimit(SoftLimitDirection.kForward, shouldLimit);

      this.floatingMotor.enableSoftLimit(SoftLimitDirection.kReverse, shouldLimit);
      this.floatingMotor.enableSoftLimit(SoftLimitDirection.kForward, shouldLimit);
   }


   public void configureMotors() {
      this.anchorMotor.setInverted(Constants.Arm.Anchor.kInverted);
      this.floatingMotor.setInverted(Constants.Arm.Floating.kInverted);
      this.anchorMotor.setIdleMode(IdleMode.kBrake);
      this.floatingMotor.setIdleMode(IdleMode.kBrake);

      this.anchorMotor.setSmartCurrentLimit(60); 
      this.floatingMotor.setSmartCurrentLimit(60); 

      // TODO: test these
      this.anchorMotor.setSoftLimit(SoftLimitDirection.kReverse, (float) Constants.Arm.Anchor.kMinAngle); 
      this.anchorMotor.setSoftLimit(SoftLimitDirection.kForward, (float) Constants.Arm.Anchor.kMaxAngle);
      this.anchorMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
      this.anchorMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
      this.anchorMotor.setIdleMode(IdleMode.kBrake); 
   
      this.floatingMotor.setSoftLimit(SoftLimitDirection.kReverse, (float) Constants.Arm.Floating.kMinAngle);
      this.floatingMotor.setSoftLimit(SoftLimitDirection.kForward, (float) Constants.Arm.Floating.kMaxAngle);
      this.floatingMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
      this.floatingMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
      this.anchorMotor.setIdleMode(IdleMode.kBrake); 

      this.toggleSoftLimits(true);
   }

   public void zeroEncoders() {
      this.anchorEncoder.setPosition(Constants.Arm.Anchor.kContracted);
      this.floatingEncoder.setPosition(Constants.Arm.Floating.kContracted);
   }

   public void configureEncoders() {
      this.anchorEncoder.setPositionConversionFactor(Constants.Arm.Anchor.kRatio);
      this.floatingEncoder.setPositionConversionFactor(Constants.Arm.Floating.kRatio);
      // this.anchorEncoder.setInverted(true); 

      this.anchorEncoder.setPosition(Constants.Arm.Anchor.kContracted);
      this.floatingEncoder.setPosition(Constants.Arm.Floating.kContracted);
   }

   public void configureControllers() {
      // this.anchorPIDController.setP(Constants.Arm.Anchor.kP);
      // this.anchorPIDController.setI(Constants.Arm.Anchor.kI);
      // this.anchorPIDController.setD(Constants.Arm.Anchor.kD);
      // this.anchorPIDController.setFF(Constants.Arm.Anchor.kFF);
      this.floatingPIDController.setP(Constants.Arm.Floating.kP);
      this.floatingPIDController.setI(Constants.Arm.Floating.kI);
      this.floatingPIDController.setD(Constants.Arm.Floating.kD);
      this.floatingPIDController.setFF(Constants.Arm.Floating.kFF);

      this.floatingPIDController.setOutputRange(-0.3, 0.3);

      // this.anchorPIDController.setFeedbackDevice(anchorEncoder);
      this.floatingPIDController.setFeedbackDevice(floatingEncoder); 
   }

   public void initTuneControllers(){
      SmartDashboard.putNumber("anchorKP", SmartDashboard.getNumber("anchorKP", Constants.Arm.Anchor.kP));
      SmartDashboard.putNumber("anchorKI", SmartDashboard.getNumber("anchorKI", Constants.Arm.Anchor.kI));
      SmartDashboard.putNumber("anchorKD", SmartDashboard.getNumber("anchorKD", Constants.Arm.Anchor.kD));
      SmartDashboard.putNumber("anchorKFF", SmartDashboard.getNumber("anchorKFF", Constants.Arm.Anchor.kFF));
   
      SmartDashboard.putNumber("floating KP", SmartDashboard.getNumber("floatingKP", Constants.Arm.Floating.kP));
      SmartDashboard.putNumber("floatingKI", SmartDashboard.getNumber("floatingKI", Constants.Arm.Floating.kI));
      SmartDashboard.putNumber("floatingKD", SmartDashboard.getNumber("floatingKD", Constants.Arm.Floating.kD));
      SmartDashboard.putNumber("floatingKFF", SmartDashboard.getNumber("floatingKFF", Constants.Arm.Floating.kFF));
   }

   public void tuneControllers() {
      double floatingKP = SmartDashboard.getEntry("floating KP").getDouble(0);
      double floatingKI = SmartDashboard.getEntry("floatingKI").getDouble(0);
      double floatingKD = SmartDashboard.getEntry("floatingKD").getDouble(0);
      double floatingKFF = SmartDashboard.getEntry("floatingKFF").getDouble(0);
      double anchorKP = SmartDashboard.getEntry("anchorKP").getDouble(0);
      double anchorKI = SmartDashboard.getEntry("anchorKI").getDouble(0);
      double anchorKD = SmartDashboard.getEntry("anchorKD").getDouble(0);
      double anchorKFF = SmartDashboard.getEntry("anchorKFF").getDouble(0);

      this.floatingPIDController.setP(floatingKP);
      this.floatingPIDController.setI(floatingKI);
      this.floatingPIDController.setD(floatingKD);
      this.floatingPIDController.setFF(floatingKFF);
      // this.anchorPIDController.setP(anchorKP);
      // this.anchorPIDController.setI(anchorKI);
      // this.anchorPIDController.setD(anchorKD);
      // this.anchorPIDController.setFF(anchorKFF);
   }

   public double getAnchorAngle() {
      return this.anchorEncoder.getPosition();
   }

   public double getFloatingAngle() {
      return this.floatingEncoder.getPosition();
   }

   // public void setAnchorAngle(double anchorAngle) {
   //    this.anchorPIDController.setReference(anchorAngle, CANSparkMax.ControlType.kPosition);
   // }

   public void setFloatingAngle(double floatingAngle) {
      this.floatingPIDController.setReference(floatingAngle, CANSparkMax.ControlType.kPosition);
   }

   public boolean isAnchorAtAngle(double anchorAngleTarget){
      return Math.abs(this.getAnchorAngle() - anchorAngleTarget ) < Constants.Arm.Anchor.kErrorThreshold;
   }

   public boolean isFloatingAtAngle(double floatingAngleTarget) {
      return Math.abs(this.getFloatingAngle() - floatingAngleTarget ) < Constants.Arm.Floating.kErrorThreshold;
   }

   public boolean isAt(Position position) {
      return this.isAnchorAtAngle(position.getAnchor()) && this.isFloatingAtAngle(position.getFloating());
   }

   @Override
   public void periodic() {
      // TODO: comment out tuneControllers() at comp
      // tuneControllers();

      // ! this will fucking break everything if our limit switch unplugs
      // if(!this.anchorLimitSwitch.get()) {
      //    this.anchorEncoder.setPosition(Constants.Arm.Anchor.kContracted);
      // }

      SmartDashboard.putBoolean("limit switch contacted", !this.anchorLimitSwitch.get()); 

      SmartDashboard.putBoolean("on cube", this.armMode);

      SmartDashboard.putNumber("Anchor Angle", this.getAnchorAngle());
      SmartDashboard.putNumber("Floating Angle", this.getFloatingAngle());
   }
}