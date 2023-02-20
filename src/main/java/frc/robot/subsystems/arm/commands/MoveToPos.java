package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.Arm;
public class MoveToPos extends SequentialCommandGroup {

    private Arm arm;
    
    public MoveToPos(Arm arm, double desiredAnchor, double desiredFloating){
        this.arm = arm;
        addCommands(
            new SequentialCommandGroup(
                new MoveAnchorJoint(0, arm)
                // new MoveAnchorJoint(Math.PI / 2, this.arm), 
                // new MoveFloatingJoint(desiredFloating, this.arm),
                // new MoveAnchorJoint(desiredAnchor, this.arm)
            )       
        );
        addRequirements(this.arm);
    }
}
