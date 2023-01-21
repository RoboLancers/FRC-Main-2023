package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Arm extends SubsystemBase {

    private CANSparkMax anchorJointMotor, floatingJointMotor;
    private SparkMaxPIDController anchorJointPIDController, floatingJointPIDController;
    private double floatingJointAngle, anchorJointAngle;
    
    

 public Arm() {
     
     this.anchorJointMotor = new CANSparkMax(Constants.Arm.Ports.kAnchorJointPort, CANSparkMax.MotorType.kBrushless);
     this.floatingJointMotor = new CANSparkMax(Constants.Arm.Ports.kFloatingArmPort, CANSparkMax.MotorType.kBrushless);

     this.anchorJointAngle = Constants.Arm.kContractedAnchorAngle;
     this.floatingJointAngle = Constants.Arm.kContractedFloatingAngle;
      

     // For PID
     //This order might need to change as setting to angle ^ should be done after this (maybe) and 
     // the code below might not be needed sense we are taking canSpark motors delete below

     
      // Set Limits for angles which arms can  go to 
     this.anchorJointMotor.setSoftLimit(SoftLimitDirection.kReverse, Constants.Arm.AnchorJoint.kMinAngle);
     this.anchorJointMotor.setSoftLimit(SoftLimitDirection.kForward, Constants.Arm.AnchorJoint.kMaxAngle);
     this.anchorJointMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
     this.anchorJointMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
     
     this.floatingJointMotor.setSoftLimit(SoftLimitDirection.kReverse, Constants.Arm.FloatingJoint.kMinAngle);
     this.floatingJointMotor.setSoftLimit(SoftLimitDirection.kForward, Constants.Arm.FloatingJoint.kMaxAngle);
     this.floatingJointMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
     this.floatingJointMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
     
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
   
   }  
   

//finds the two angles for the arm - will be above the line from joint to obj
//angle calculated from joint so maybe change to arm 2 horizontal
 public double[] calculateAngles(double dy, double dz) {
    double[] angles = new double[2];
    double distanceToObj = Math.sqrt(dy*dy + dz*dz);
    double alpha = Math.acos((Constants.Arm.kFloatingArmLength*Constants.Arm.kFloatingArmLength - Constants.Arm.kAnchorArmLength*Constants.Arm.kAnchorArmLength - distanceToObj*distanceToObj)/(-2*Constants.Arm.kAnchorArmLength*distanceToObj));
    double gamma = Math.atan2(dy, dz);
    //finds theta1, the angle between the horizontal and anchorJoint
    angles[0] = alpha+gamma;
    angles[1] = Math.acos((distanceToObj*distanceToObj - Constants.Arm.kFloatingArmLength*Constants.Arm.kFloatingArmLength - Constants.Arm.kAnchorArmLength*Constants.Arm.kAnchorArmLength)/(-2*Constants.Arm.kFloatingArmLength*Constants.Arm.kAnchorArmLength));
    return angles;
 }

 // TODO: We need a periodic for this
 public double getAnchorAngleFromEncoder() {
    double angle = anchorJointMotor.getEncoder() * Constants.Arm.kDegreesPerTick;
    this.anchorJointAngle = angle;
    return angle;
 }

 public double getFloatingAngleFromEncoder() {
    double angle = floatingJointMotor.getEncoder() * Constants.Arm.kDegreesPerTick;
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

}