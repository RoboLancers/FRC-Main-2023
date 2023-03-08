package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.Arm;

public class MoveToPos extends ParallelCommandGroup {
    public MoveToPos(Arm arm){
        addCommands(
            new MoveAnchorJoint(() -> arm.anchorSetpoint, arm),
            new MoveFloatingJoint(() -> arm.floatingSetpoint, arm)
        );

        addRequirements(arm);
    }
}