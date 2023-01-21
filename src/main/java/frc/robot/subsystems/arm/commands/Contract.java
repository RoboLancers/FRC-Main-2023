package frc.robot.subsystems.arm.commands;

import frc.robot.subsystems.Arm;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;

public class Contract extends CommandBase{
    public Arm arm;

    @Override
    public void initialize() {
    }
    @Override
    public void execute() {
        if (arm.getAnchorAngleFromEncoder() != Constants.Arm.kContractedAnchorAngle) {
            // TODO: Make sure this is going in the right direction
            arm.setAnchorMotorPower(Constants.Arm.kMotorPower);
        }
        if (arm.getFloatingAngleFromEncoder() != Constants.Arm.kContractedFloatingAngle) {
            arm.setFloatingMotorPower(Constants.Arm.kMotorPower);
        }
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
