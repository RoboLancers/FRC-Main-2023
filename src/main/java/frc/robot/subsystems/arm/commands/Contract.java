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
            new MoveFloatingJoint(Constants.Arm.Miscellaneous.kContractedFloatingAngle, arm),
            new DeficientAnchorJoint(this.arm, this.arm.anchorJointMotor.getEncoder())
        );
        addRequirements(this.arm);
        
        }
    }