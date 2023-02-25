package frc.robot.subsystems.arm.commands;
import java.time.chrono.ThaiBuddhistDate;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.arm.Arm;

public class MoveFloatingJoint extends CommandBase {
    public DoubleSupplier desiredAngle;
    public Arm arm;

    public MoveFloatingJoint(DoubleSupplier desiredAngle, Arm arm) {
        this.desiredAngle = desiredAngle;
        this.arm = arm;
    }

    @Override
    public void execute() {
        arm.setFloatingAngle(desiredAngle.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return arm.isFloatingAtAngle(desiredAngle.getAsDouble());
    }
}
