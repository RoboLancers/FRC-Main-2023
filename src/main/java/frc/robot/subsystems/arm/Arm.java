package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Constants;

public class Arm extends SubsystemBase {

    public CANSparkMax anchorJointMotor, floatingJointMotor;
    public SparkMaxPIDController anchorJointPIDController, floatingJointPIDController;
    public double floatingJointAngle, anchorJointAngle;
    // public DigitalInput anchorLimitSwitch, floatingLimitSwitch;
    public boolean anchorLimitSwitchTriggered, floatingLimitSwitchTriggered;
    
    

 public Arm() {
     
     this.anchorJointMotor = new CANSparkMax(Constants.Arm.Ports.kAnchorJointPort, CANSparkMax.MotorType.kBrushless);
     this.floatingJointMotor = new CANSparkMax(Constants.Arm.Ports.kFloatingJointPort, CANSparkMax.MotorType.kBrushless);

     this.anchorJointMotor.setInverted(true);
     this.floatingJointMotor.setInverted(true);

   this.anchorJointMotor.getEncoder().setPositionConversionFactor(1);
   this.floatingJointMotor.getEncoder().setPositionConversionFactor(1);

   //   this.anchorJointMotor.getEncoder().setPositionConversionFactor(Constants.Arm.Miscellaneous.kDegreesPerTick);
   //   this.floatingJointMotor.getEncoder().setPositionConversionFactor(Constants.Arm.Miscellaneous.kDegreesPerTick);

     this.anchorJointAngle = Constants.Arm.Miscellaneous.kContractedAnchorAngle;
     this.floatingJointAngle = Constants.Arm.Miscellaneous.kContractedFloatingAngle;



   this.anchorJointMotor.getEncoder().setPosition(Constants.Arm.Miscellaneous.kContractedAnchorAngle);
   this.floatingJointMotor.getEncoder().setPosition(Constants.Arm.Miscellaneous.kContractedFloatingAngle);
      

     // For PID
     //This order might need to change as setting to angle ^ should be done after this (maybe) and 
     // the code below might not be needed sense we are taking canSpark motors delete below

     
      // Set Limits for angles which arms can  go to 
   //   this.anchorJointMotor.setSoftLimit(SoftLimitDirection.kReverse, (float) Constants.Arm.AnchorJoint.kMinAngle);
   //   this.anchorJointMotor.setSoftLimit(SoftLimitDirection.kForward, (float) Constants.Arm.AnchorJoint.kMaxAngle);
   //   this.anchorJointMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
   //   this.anchorJointMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
     
   //   this.floatingJointMotor.setSoftLimit(SoftLimitDirection.kReverse, (float) Constants.Arm.FloatingJoint.kMinAngle);
   //   this.floatingJointMotor.setSoftLimit(SoftLimitDirection.kForward, (float) Constants.Arm.FloatingJoint.kMaxAngle);
   //   this.floatingJointMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
   //   this.floatingJointMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
     
     // Get PID controllers for each arm which is each have their own PID values
     this.anchorJointPIDController = this.anchorJointMotor.getPIDController();
     this.floatingJointPIDController = this.floatingJointMotor.getPIDController();

     this.anchorJointPIDController.setP(Constants.Arm.AnchorJoint.kP);
     this.anchorJointPIDController.setI(Constants.Arm.AnchorJoint.kI);
     this.anchorJointPIDController.setD(Constants.Arm.AnchorJoint.kD);
     this.anchorJointPIDController.setFF(Constants.Arm.AnchorJoint.kFF);

     this.floatingJointPIDController.setP(Constants.Arm.FloatingJoint.kP);
     this.floatingJointPIDController.setI(Constants.Arm.FloatingJoint.kI);
     this.floatingJointPIDController.setD(Constants.Arm.FloatingJoint.kD);
     this.floatingJointPIDController.setFF(Constants.Arm.FloatingJoint.kFF);

      SmartDashboard.putNumber("anchorKP", SmartDashboard.getNumber("anchorKP", Constants.Arm.AnchorJoint.kP));
      SmartDashboard.putNumber("anchorKI", SmartDashboard.getNumber("anchorKI", Constants.Arm.AnchorJoint.kI));
      SmartDashboard.putNumber("anchorKD", SmartDashboard.getNumber("anchorKD", Constants.Arm.AnchorJoint.kD));
      SmartDashboard.putNumber("anchorKFF", SmartDashboard.getNumber("anchorKFF", Constants.Arm.AnchorJoint.kFF));
   
      SmartDashboard.putNumber("floatingKP", SmartDashboard.getNumber("floatingKP", Constants.Arm.FloatingJoint.kP));
      SmartDashboard.putNumber("floatingKI", SmartDashboard.getNumber("floatingKI", Constants.Arm.FloatingJoint.kI));
      SmartDashboard.putNumber("floatingKD", SmartDashboard.getNumber("floatingKD", Constants.Arm.FloatingJoint.kD));
      SmartDashboard.putNumber("floatingKFF", SmartDashboard.getNumber("floatingKFF", Constants.Arm.FloatingJoint.kFF));
   }  
   

//finds the two angles for the arm - will be above the line from joint to obj
//angle calculated from joint so maybe change to arm 2 horizontal
 public double[] calculateAngles(double dy, double dz) {
    double adjustedY = dy - Constants.Arm.Miscellaneous.distanceBetweenPivotLimelight;
    double[] angles = new double[2];
    double distanceToObj = Math.sqrt(adjustedY*adjustedY + dz*dz);
    double alpha = Math.acos((Constants.Arm.Miscellaneous.kFloatingArmLength*Constants.Arm.Miscellaneous.kFloatingArmLength + distanceToObj*distanceToObj- Constants.Arm.Miscellaneous.kAnchorArmLength*Constants.Arm.Miscellaneous.kAnchorArmLength)/(2*Constants.Arm.Miscellaneous.kAnchorArmLength*distanceToObj));
    double gamma = Math.atan2(adjustedY, dz);
    //finds theta1, the angle between the horizontal and anchorJoint
    angles[0] = alpha+gamma;
    angles[1] = Math.PI - Math.acos((Constants.Arm.Miscellaneous.kAnchorArmLength * Constants.Arm.Miscellaneous.kAnchorArmLength + Constants.Arm.Miscellaneous.kFloatingArmLength * Constants.Arm.Miscellaneous.kFloatingArmLength - distanceToObj * distanceToObj) / (2 * Constants.Arm.Miscellaneous.kAnchorArmLength * Constants.Arm.Miscellaneous.kFloatingArmLength));
    return angles;
 }

 public double getAnchorAngleFromEncoder() {
    double angle = anchorJointMotor.getEncoder().getPosition();
    this.anchorJointAngle = angle;
    return angle;
 }

 public double getFloatingAngleFromEncoder() {
    double angle = floatingJointMotor.getEncoder().getPosition();
    this.floatingJointAngle = angle;
    return angle;
 }


 //Sets angles of joints 
 public void setFloatingAngle(double floatingJointAngle){
   this.floatingJointPIDController.setReference(floatingJointAngle, CANSparkMax.ControlType.kPosition);
}

public void setAnchorAngle(double anchorJointAngle) {
   this.anchorJointPIDController.setReference(anchorJointAngle, CANSparkMax.ControlType.kPosition);
}

public boolean isAnchorAtAngle(double anchorJointAngleTarget){
   return Math.abs(getAnchorAngleFromEncoder() - anchorJointAngleTarget ) < Constants.Arm.AnchorJoint.kanchorJointErrorThreshold;
}

public boolean isFloatingAtAngle(double floatingJointAngleTarget){
   return Math.abs(getFloatingAngleFromEncoder() - floatingJointAngleTarget ) < Constants.Arm.FloatingJoint.kFloatingJointErrorThreshold;
}

public void periodic() {
   SmartDashboard.putNumber("Anchor Angle", this.getAnchorAngleFromEncoder());
   SmartDashboard.putNumber("Floating Angle", this.getFloatingAngleFromEncoder());
   SmartDashboard.putNumber("Anchor Device ID", this.anchorJointMotor.getDeviceId()); 
   SmartDashboard.putNumber("Floating Device ID", this.floatingJointMotor.getDeviceId()); 

   // this.anchorLimitSwitchTriggered = !this.anchorLimitSwitch.get();
   // this.floatingLimitSwitchTriggered = !this.floatingLimitSwitch.get();
   double floatingKP = SmartDashboard.getEntry("floatingKP").getDouble(0);
   double floatingKI = SmartDashboard.getEntry("floatingKI").getDouble(0);
   double floatingKD = SmartDashboard.getEntry("floatingKD").getDouble(0);
   double floatingKFF = SmartDashboard.getEntry("floatingKFF").getDouble(0);
   double anchorKP = SmartDashboard.getEntry("anchorKP").getDouble(0);
   double anchorKI = SmartDashboard.getEntry("anchorKI").getDouble(0);
   double anchorKD = SmartDashboard.getEntry("anchorKD").getDouble(0);
   double anchorKFF = SmartDashboard.getEntry("anchorKFF").getDouble(0);
   this.floatingJointPIDController.setP(floatingKP);
   this.floatingJointPIDController.setI(floatingKI);
   this.floatingJointPIDController.setD(floatingKD);
   this.floatingJointPIDController.setFF(floatingKFF);
   this.anchorJointPIDController.setP(anchorKP);
   this.anchorJointPIDController.setI(anchorKI);
   this.anchorJointPIDController.setD(anchorKD);
   this.anchorJointPIDController.setFF(anchorKFF);

   // in order to update not streamed doubles members
   this.getAnchorAngleFromEncoder();
   this.getFloatingAngleFromEncoder();
}

}
