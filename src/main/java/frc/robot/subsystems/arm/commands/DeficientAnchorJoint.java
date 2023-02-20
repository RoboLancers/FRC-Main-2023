package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.Arm;
import frc.robot.Constants;
import com.revrobotics.RelativeEncoder;

public class DeficientAnchorJoint extends SequentialCommandGroup {
    public Arm arm;
    public RelativeEncoder encoder;

    public DeficientAnchorJoint(Arm arm, RelativeEncoder encoder) {
        this.arm = arm;
        this.encoder = encoder;
        addCommands(
            new MoveFloatingJoint(Constants.Arms.Miscellaneous.kContractedFloatingAngle, arm),
            new MoveAnchorJoint(Constants.Arms.Miscellaneous.kUndershotAngle, arm),
            new RollUntilSwitch(arm, encoder));
        addRequirements(this.arm);
    }
}
