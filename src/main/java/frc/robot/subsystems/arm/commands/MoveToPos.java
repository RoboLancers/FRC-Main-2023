package frc.robot.subsystems.arm.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.arm.Arm;

public class MoveToPos extends SequentialCommandGroup {
    public MoveToPos(Arm arm, Constants.Arm.Position position) {
        this(arm, position.getAnchor(), position.getFloating()); 
    }

    public MoveToPos(Arm arm, double anchor, double floating) {
        addCommands(
            new MoveFloating(arm, Constants.Arm.Floating.kContracted),
            new MoveAnchor(arm, anchor),
            new MoveFloating(arm, floating)
        );
    }

    public MoveToPos(Arm arm, DoubleSupplier anchorSetpoint, DoubleSupplier floatingSetpoint) {
        addCommands(
            new MoveFloating(arm, Constants.Arm.Floating.kContracted),
            new MoveAnchor(arm, anchorSetpoint),
            new MoveFloating(arm, floatingSetpoint)
        );
    }
}
