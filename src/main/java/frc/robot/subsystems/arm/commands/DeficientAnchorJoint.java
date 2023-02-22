package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.Arm;
import frc.robot.Constants;

public class DeficientAnchorJoint extends SequentialCommandGroup {
    public DeficientAnchorJoint(Arm arm) {
        addCommands(
            new MoveFloatingJoint(Constants.Arm.Floating.kContracted, arm),
            // new MoveAnchorJoint(Constants.Arm.Misc.kUndershotAngle, arm),
            new RollUntilSwitch(arm)
        );

        addRequirements(arm);
    }
}