package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.Arm;

public class MoveToPos extends SequentialCommandGroup {
    public MoveToPos(Arm arm, double desiredAnchor, double desiredFloating){
        addCommands(
            new SequentialCommandGroup(
                // new MoveAnchorJoint(0, arm),
                // new MoveAnchorJoint(Math.PI / 2, arm), 
                new MoveFloatingJoint(desiredFloating, arm)
                // new MoveAnchorJoint(desiredAnchor, arm)
            )
        );

        addRequirements(arm);
    }
}
