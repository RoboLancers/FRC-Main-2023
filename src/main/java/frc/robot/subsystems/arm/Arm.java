package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Constants;

public class Arm extends SubsystemBase {
   public CANSparkMax anchorMotor, floatingMotor;
   public RelativeEncoder anchorEncoder, floatingEncoder;
   public SparkMaxPIDController anchorPIDController, floatingPIDController;

   public double anchorSetpoint = Constants.Arm.Anchor.kContracted;
   public double floatingSetpoint = Constants.Arm.Floating.kContracted;

   public boolean armMode = true; // true: cube, false: cone

   // TODO: port error, uncomment when limit switches actually exist
   // public DigitalInput anchorLimitSwitch, floatingLimitSwitch;

   public Arm() {
      this.anchorMotor = new CANSparkMax(Constants.Arm.Ports.kAnchorPort, CANSparkMax.MotorType.kBrushless);
      this.floatingMotor = new CANSparkMax(Constants.Arm.Ports.kFloatingPort, CANSparkMax.MotorType.kBrushless);

      this.configureMotors();

      this.anchorEncoder = this.anchorMotor.getEncoder();
      this.floatingEncoder = this.floatingMotor.getEncoder();

      this.configureEncoders();

      this.anchorPIDController = this.anchorMotor.getPIDController();
      this.floatingPIDController = this.floatingMotor.getPIDController();

      this.configureControllers();

      // this.initTuneControllers();
   }

   public void configureMotors() {
      this.anchorMotor.setInverted(Constants.Arm.Anchor.kInverted);
      this.floatingMotor.setInverted(Constants.Arm.Anchor.kInverted);

      // TODO: test these
      this.anchorMotor.setSoftLimit(SoftLimitDirection.kReverse, (float) Constants.Arm.Anchor.kMinAngle);
      this.anchorMotor.setSoftLimit(SoftLimitDirection.kForward, (float) Constants.Arm.Anchor.kMaxAngle);
      this.anchorMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
      this.anchorMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
   
      this.floatingMotor.setSoftLimit(SoftLimitDirection.kReverse, (float) Constants.Arm.Floating.kMinAngle);
      this.floatingMotor.setSoftLimit(SoftLimitDirection.kForward, (float) Constants.Arm.Floating.kMaxAngle);
      this.floatingMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
      this.floatingMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
   }

   public void configureEncoders(){
      // TODO: find these conversion rates
      this.anchorEncoder.setPositionConversionFactor(Constants.Arm.Anchor.kRatio);
      this.floatingEncoder.setPositionConversionFactor(Constants.Arm.Floating.kRatio);

      this.anchorEncoder.setPosition(Constants.Arm.Anchor.kContracted);
      this.floatingEncoder.setPosition(Constants.Arm.Floating.kContracted);
   }

   public void configureControllers(){
      this.anchorPIDController.setP(Constants.Arm.Anchor.kP);
      this.anchorPIDController.setI(Constants.Arm.Anchor.kI);
      this.anchorPIDController.setD(Constants.Arm.Anchor.kD);
      this.anchorPIDController.setFF(Constants.Arm.Anchor.kFF);
      // this.anchorPIDController.set 

      this.floatingPIDController.setP(Constants.Arm.Floating.kP);
      this.floatingPIDController.setI(Constants.Arm.Floating.kI);
      this.floatingPIDController.setD(Constants.Arm.Floating.kD);
      this.floatingPIDController.setFF(Constants.Arm.Floating.kFF);
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

   public void tuneControllers(){
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
      this.anchorPIDController.setP(anchorKP);
      this.anchorPIDController.setI(anchorKI);
      this.anchorPIDController.setD(anchorKD);
      this.anchorPIDController.setFF(anchorKFF);
   }
   
   // TODO: double check formula transcription and maybe try to break it up
   //finds the two angles for the arm - will be above the line from joint to obj
   //angle calculated from joint so maybe change to arm 2 horizontal
   // public double[] calculateAngles(double dy, double dz) {
   //    double adjustedY = dy - Constants.Arm.Misc.distanceBetweenPivotLimelight;

   //    double distanceToObj = Math.sqrt(adjustedY * adjustedY + dz * dz);
   //    double alpha = Math.acos((Constants.Arm.Floating.kLength * Constants.Arm.Floating.kLength + distanceToObj * distanceToObj - Constants.Arm.Anchor.kLength * Constants.Arm.Anchor.kLength) / (2 * Constants.Arm.Anchor.kLength * distanceToObj));
   //    double gamma = Math.atan2(adjustedY, dz);

   //    double[] angles = new double[2];
   //    angles[0] = alpha + gamma;
   //    angles[1] = Math.PI - Math.acos((Constants.Arm.Anchor.kLength * Constants.Arm.Anchor.kLength + Constants.Arm.Floating.kLength * Constants.Arm.Floating.kLength - distanceToObj * distanceToObj) / (2 * Constants.Arm.Anchor.kLength * Constants.Arm.Floating.kLength));
      
   //    return angles;
   // }

   public double getAnchorAngle() {
      return this.anchorEncoder.getPosition();
   }

   public double getFloatingAngle() {
      return this.floatingEncoder.getPosition();
   }

   public void setFloatingAngle(double floatingAngle) {
      this.floatingPIDController.setReference(floatingAngle, CANSparkMax.ControlType.kPosition);
      // this.desiredFloating = floatingAngle; 
   }

   public void setAnchorAngle(double anchorAngle) {
      this.anchorPIDController.setReference(anchorAngle, CANSparkMax.ControlType.kPosition);
   }

   public boolean isAnchorAtAngle(double anchorAngleTarget){
      return Math.abs(this.getAnchorAngle() - anchorAngleTarget ) < Constants.Arm.Anchor.kErrorThreshold;
   }

   public boolean isFloatingAtAngle(double floatingAngleTarget){
      return Math.abs(this.getFloatingAngle() - floatingAngleTarget ) < Constants.Arm.Floating.kErrorThreshold;
   }

   @Override
   public void periodic(){
      // TODO: comment out tuneControllers() at comp
      // tuneControllers();

      SmartDashboard.putNumber("Anchor Angle", this.getAnchorAngle());
      SmartDashboard.putNumber("Floating Angle", this.getFloatingAngle());
   }
}