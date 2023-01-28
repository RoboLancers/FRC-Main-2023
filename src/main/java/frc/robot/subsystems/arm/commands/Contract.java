package frc.robot.subsystems.arm.commands;

import frc.robot.subsystems.arm.Arm;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Constants.Arms.FloatingJoint;

import java.time.chrono.ThaiBuddhistEra;

public class Contract extends SequentialCommandGroup{
    public Arm arm;
     
    public Contract(Arm arm) {
        this.arm = arm;
        addCommands(
            new MoveAnchorJoint(Math.PI / 2, arm),
            new MoveFloatingJoint(Constants.Arms.Miscellaneous.kContractedFloatingAngle, arm),
            new HardMoveAnchorJoint(arm)
        );
        addRequirements(this.arm);
    }
}
