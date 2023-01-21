package frc.robot.subsystems.arm.commands;

import frc.robot.subsystems.Arm;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;

public class Contract extends CommandBase{
    public Arm arm;
     
    public Contract(Arm arm) {
        this.arm = arm;
        addRequirements(this.arm);
    }

    double[] angles = arm.calculateAngles(Constants.Arm.kContractedAnchorAngle, Constants.Arm.kContractedFloatingAngle);

    @Override
    public void execute() {
        if (arm.getAnchorAngleFromEncoder() != Constants.Arm.kContractedAnchorAngle) {
            // TODO: Make sure this is going in the right direction
            arm.setAnchorAngle(angles[0]);
        }
        if (arm.getFloatingAngleFromEncoder() != Constants.Arm.kContractedFloatingAngle) {
            arm.setFloatingAngle(angles[1]);
        }
        arm.anchorJointMotor.resetEncoder();
    }
    @Override
    public boolean isFinished() {
        if ((arm.getAnchorAngleFromEncoder() == Constants.Arm.kContractedAnchorAngle) && 
        (arm.getFloatingAngleFromEncoder() == Constants.Arm.kContractedFloatingAngle)) {
            return true;
        }
        return false;
    }
}
