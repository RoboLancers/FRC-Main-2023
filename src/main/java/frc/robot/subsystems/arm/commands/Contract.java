package frc.robot.subsystems.arm.commands;

import frc.robot.subsystems.arm.Arm;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import java.time.chrono.ThaiBuddhistEra;

public class Contract extends SequentialCommandGroup{
    public Arm arm;
     
    public Contract(Arm arm) {
        this.arm = arm;
        addCommands(
            new MoveAnchorJoint(Math.PI / 2, arm),
            new MoveFloatingJoint(Constants.Arms.Miscellaneous.kContractedFloatingAngle, arm),
            new DeficientAnchorJoint(this.arm, this.arm.anchorJointMotor.getEncoder())
        );
        addRequirements(this.arm);
        
        }

public class Contract {
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
    }
}
