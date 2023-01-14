package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.MotorType;

public class Arm extends SubsystemBase{

    public CANSparkMax anchorJointMotor, floatingJointMotor;
    public double floatingJointAngle, anchorJointAngle;
    boolean holdingObject;
    

 public Arm(CANSparkMax anchorJointMotor, CANSparkMax floatingJointMotor) {
     this.anchorJointMotor = anchorJointMotor;
     this.floatingJointMotor = floatingJointMotor;
     this.anchorJointAngle = Constants.Arm.kContractedAnchorAngle;
     this.floatingJointAngle = Constants.Arm.kContractedFloatingAngle;
 }   

//finds the two angles for the arm - will be above the line from joint to obj
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
    double angle = anchorJointMotor.getEncoder * Constants.Arm.kTicksPerRevolution;
    this.anchorJointAngle = angle;
    return angle;
 }

 public double getFloatingAngleFromEncoder() {
    double angle = floatingJointMotor.getEncoder * Constants.Arm.kTicksPerRevolution;
    this.floatingJointAngle = angle;
    return angle;
 }

 public void setAnchorMotorPower(double power) {
   anchorJointMotor.set(power);
 }

 public void setFloatingMotorPower(double power) {
   floatingJointMotor.set(power);
 }
}