package frc.robot.subsystems.arm.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Constants.Arm.Position;
import frc.robot.subsystems.arm.Arm;

public class MoveToPos extends SequentialCommandGroup {
    public MoveToPos(Arm arm, Position position) {
        this(arm, position.getAnchor(), position.getFloating()); 
    }

    public MoveToPos(Arm arm, Supplier<Position> position) {
        this(arm, () -> position.get().getAnchor(), () -> position.get().getFloating());
    }

    public MoveToPos(Arm arm, double anchor, double floating) {
        this(arm, () -> anchor, () -> floating); 
    }

    public MoveToPos(Arm arm, DoubleSupplier anchorSetpoint, DoubleSupplier floatingSetpoint) {
        addCommands(
            new MoveFloating(arm, Constants.Arm.Floating.kContracted),
            new MoveAnchor(arm, anchorSetpoint),
            new MoveFloating(arm, floatingSetpoint)
        );
    }
}
