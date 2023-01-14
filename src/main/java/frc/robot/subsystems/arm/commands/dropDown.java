package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import frc.robot.Constants;

public class DropDown extends CommandBase{
    public boolean isAnchorAngleCorrect;
    public boolean isFloatingAngleCorrect;
    
    @Override
    public void initialize() { 
        // Must check to see if it's in a good position
    }
    @Override
    public void execute() {
        // Check which constant we want to access depending on the place we want to place it
        // Put an if loop I guess
        double y = 0.0;
        double z = 0.0;
        Arm arm = null; // Pass this in to inputs
        double[] desiredAngles = arm.calculateAngles(y, z);
        arm.floatingJointMotor.setPower(Constants.Arm.kMotorPower);
        if (arm.getAnchorAngleFromEncoder() != Constants.Arm.kLowY) {
            // TODO: Research using built in PID
            arm.anchorJointMotor.setPower(Constants.Arm.kMotorPower);
        }
        else if (arm.getAnchorAngleFromEncoder() == Constants.Arm.kMidNodeY) {
            arm.anchorJointMotor.setPower(Constants.Arm.kMotorPower);
        }
        else if (arm.getAnchorAngleFromEncoder() == Constants.Arm.kHighNodeY) {
            arm.anchorJointMotor.setPower(Constants.Arm.kMotorPower);
        }
        else if (arm.getAnchorAngleFromEncoder() == Constants.Arm.kMidShelfY) {
            arm.anchorJointMotor.setPower(Constants.Arm.kMotorPower);
        }
        else if (arm.getAnchorAngleFromEncoder() == Constants.Arm.kHighShelfY) {
            arm.anchorJointMotor.setPower(Constants.Arm.kMotorPower);
        }
        if (arm.getFloatingAngleFromEncoder() == Constants.Arm.kLowZ) {
            arm.floatingJointMotor.setPower(Constants.Arm.kMotorPower);
        }
        else if (arm.getFloatingAngleFromEncoder() == Constants.Arm.kHighNodeZ) {
            arm.floatingJointMotor.setPower(Constants.Arm.kMotorPower);
        }
        else if (arm.getFloatingAngleFromEncoder() == Constants.Arm.kHighNodeZ) {
            arm.floatingJointMotor.setPower(Constants.Arm.kMotorPower);
        }
        else if (arm.getFloatingAngleFromEncoder() == Constants.Arm.kMidShelfZ) {
            arm.floatingJointMotor.setPower(Constants.Arm.kMotorPower);
        }
        else if (arm.getFloatingAngleFromEncoder() == Constants.Arm.kHighShelfZ) {
            arm.floatingJointMotor.setPower(Constants.Arm.kMotorPower);
        }
    }

    @Override
    public void end(){
        
    }

    @Override
    public boolean isFinished () {
        if ((arm.getAnchorAngleFromEncoder() == Constants.Arm.kLowY) && (arm.getFloatingAngleFromEncoder() == Constants.Arm.kLowZ)) {
            return true;
        }
        // TODO: Will it automatically halt even when we don't want it to
        else if ((arm.getAnchorAngleFromEncoder() == Constants.Arm.kMidNodeY) && (arm.getFloatingAngleFromEncoder() == Constants.Arm.kMidNodeZ)) {
            return true;
        }
        else if ((arm.getAnchorAngleFromEncoder() == Constants.Arm.kHighNodeY) && (arm.getFloatingAngleFromEncoder() == Constants.Arm.kHighNodeZ)) {
            return true;
        }
        else if ((arm.getAnchorAngleFromEncoder() == Constants.Arm.kMidShelfY) && (arm.getFloatingAngleFromEncoder() == Constants.Arm.kMidShelfZ)) {
            return true;
        }
        else if ((arm.getAnchorAngleFromEncoder() == Constants.Arm.kHighShelfY) && (arm.getFloatingAngleFromEncoder() == Constants.Arm.kHighShelfZ)) {
            return true;
        }
    }
}
