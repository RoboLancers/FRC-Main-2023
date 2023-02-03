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
    public DigitalInput anchorLimitSwitch, floatingLimitSwitch;
    public boolean anchorLimitSwitchTriggered, floatingLimitSwitchTriggered;
    
    

 public Arm() {
     
     this.anchorJointMotor = new CANSparkMax(Constants.Arms.Ports.kAnchorJointPort, CANSparkMax.MotorType.kBrushless);
     this.floatingJointMotor = new CANSparkMax(Constants.Arms.Ports.kFloatingArmPort, CANSparkMax.MotorType.kBrushless);

     this.anchorJointAngle = Constants.Arms.Miscellaneous.kContractedAnchorAngle;
     this.floatingJointAngle = Constants.Arms.Miscellaneous.kContractedFloatingAngle;
      

     // For PID
     //This order might need to change as setting to angle ^ should be done after this (maybe) and 
     // the code below might not be needed sense we are taking canSpark motors delete below

     
      // Set Limits for angles which arms can  go to 
     this.anchorJointMotor.setSoftLimit(SoftLimitDirection.kReverse, (float) Constants.Arms.AnchorJoint.kMinAngle);
     this.anchorJointMotor.setSoftLimit(SoftLimitDirection.kForward, (float) Constants.Arms.AnchorJoint.kMaxAngle);
     this.anchorJointMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
     this.anchorJointMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
     
     this.floatingJointMotor.setSoftLimit(SoftLimitDirection.kReverse, (float) Constants.Arms.FloatingJoint.kMinAngle);
     this.floatingJointMotor.setSoftLimit(SoftLimitDirection.kForward, (float) Constants.Arms.FloatingJoint.kMaxAngle);
     this.floatingJointMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
     this.floatingJointMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
     
     // Get PID controllers for each arm which is each have their own PID values
     this.anchorJointPIDController = this.anchorJointMotor.getPIDController();
     this.floatingJointPIDController = this.floatingJointMotor.getPIDController();

     this.anchorJointPIDController.setP(Constants.Arms.AnchorJoint.kP);
     this.anchorJointPIDController.setI(Constants.Arms.AnchorJoint.kI);
     this.anchorJointPIDController.setD(Constants.Arms.AnchorJoint.kD);
     this.anchorJointPIDController.setFF(Constants.Arms.AnchorJoint.kFF);

     this.floatingJointPIDController.setP(Constants.Arms.FloatingJoint.kP);
     this.floatingJointPIDController.setI(Constants.Arms.FloatingJoint.kI);
     this.floatingJointPIDController.setD(Constants.Arms.FloatingJoint.kD);
     this.floatingJointPIDController.setFF(Constants.Arms.FloatingJoint.kFF);

      SmartDashboard.putNumber("anchorKP", SmartDashboard.getNumber("anchorKP", Constants.Arms.AnchorJoint.kP));
      SmartDashboard.putNumber("anchorKI", SmartDashboard.getNumber("anchorKI", Constants.Arms.AnchorJoint.kI));
      SmartDashboard.putNumber("anchorKD", SmartDashboard.getNumber("anchorKD", Constants.Arms.AnchorJoint.kD));
      SmartDashboard.putNumber("anchorKFF", SmartDashboard.getNumber("anchorKFF", Constants.Arms.AnchorJoint.kFF));
   
      SmartDashboard.putNumber("floatingKP", SmartDashboard.getNumber("floatingKP", Constants.Arms.FloatingJoint.kP));
      SmartDashboard.putNumber("floatingKI", SmartDashboard.getNumber("floatingKI", Constants.Arms.FloatingJoint.kI));
      SmartDashboard.putNumber("floatingKD", SmartDashboard.getNumber("floatingKD", Constants.Arms.FloatingJoint.kD));
      SmartDashboard.putNumber("floatingKFF", SmartDashboard.getNumber("floatingKFF", Constants.Arms.FloatingJoint.kFF));
   }  
   

//finds the two angles for the arm - will be above the line from joint to obj
//angle calculated from joint so maybe change to arm 2 horizontal
 public double[] calculateAngles(double dy, double dz) {
    double[] angles = new double[2];
    double distanceToObj = Math.sqrt(dy*dy + dz*dz);
    double alpha = Math.acos((Constants.Arms.Miscellaneous.kFloatingArmLength*Constants.Arms.Miscellaneous.kFloatingArmLength + distanceToObj*distanceToObj- Constants.Arms.Miscellaneous.kAnchorArmLength*Constants.Arms.Miscellaneous.kAnchorArmLength)/(2*Constants.Arms.Miscellaneous.kAnchorArmLength*distanceToObj));
    double gamma = Math.atan2(dy, dz);
    //finds theta1, the angle between the horizontal and anchorJoint
    angles[0] = alpha+gamma;
    angles[1] = Math.PI - Math.acos((Constants.Arms.Miscellaneous.kAnchorArmLength * Constants.Arms.Miscellaneous.kAnchorArmLength + Constants.Arms.Miscellaneous.kFloatingArmLength * Constants.Arms.Miscellaneous.kFloatingArmLength - distanceToObj * distanceToObj) / (2 * Constants.Arms.Miscellaneous.kAnchorArmLength * Constants.Arms.Miscellaneous.kFloatingArmLength));
    return angles;
 }

 // TODO: We need a periodic for this
 // TODO: Is get position what we want
 public double getAnchorAngleFromEncoder() {
    double angle = anchorJointMotor.getEncoder().getPosition() * (Constants.Arms.Miscellaneous.kDegreesPerTick);
    this.anchorJointAngle = angle;
    return angle;
 }

 public double getFloatingAngleFromEncoder() {
    double angle = floatingJointMotor.getEncoder().getPosition() * Constants.Arms.Miscellaneous.kDegreesPerTick;
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
   return Math.abs(getAnchorAngleFromEncoder() - anchorJointAngleTarget ) < Constants.Arms.AnchorJoint.kanchorJointErrorThreshold;
}

public boolean isFloatingAtAngle(double floatingJointAngleTarget){
   return Math.abs(getFloatingAngleFromEncoder() - floatingJointAngleTarget ) < Constants.Arms.FloatingJoint.kFloatingJointErrorThreshold;
}

public void periodic() {
   this.anchorLimitSwitchTriggered = !this.anchorLimitSwitch.get();
   this.floatingLimitSwitchTriggered = !this.floatingLimitSwitch.get();
   double floatingKP = SmartDashboard.getEntry("floatingKP").getDouble(0);
   double floatingKI = SmartDashboard.getEntry("floatingKI").getDouble(0);
   double floatingKD = SmartDashboard.getEntry("floatingKD").getDouble(0);
   double floatingKFF = SmartDashboard.getEntry("floatingKFF").getDouble(0);
   double anchorKP = = SmartDashboard.getEntry("anchorKP").getDouble(0);
   double anchorKI = = SmartDashboard.getEntry("anchorKI").getDouble(0);
   double anchorKD = = SmartDashboard.getEntry("anchorKD").getDouble(0);
   double anchorKFF = = SmartDashboard.getEntry("anchorKFF").getDouble(0);
   this.floatingJointPIDController.setP(floatingKP);
   this.floatingJointPIDController.setI(floatingKI);
   this.floatingJointPIDController.setD(floatingKD);
   this.floatingJointPIDController.setFF(floatingKFF);
   this.anchorJointPIDController.setP(anchorKP);
   this.anchorJointPIDController.setI(anchorKI);
   this.anchorJointPIDController.setD(anchorKD);
   this.anchorJointPIDController.setFF(anchorKFF);
}

}
