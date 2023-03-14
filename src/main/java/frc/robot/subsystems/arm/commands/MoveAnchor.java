package frc.robot.subsystems.arm.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.arm.Arm;

public class MoveAnchor extends CommandBase {
    Arm arm;
    DoubleSupplier setpoint;

    public MoveAnchor(Arm arm, double setpoint) {
        this.arm = arm;
        this.setpoint = () -> setpoint;
    }

    public MoveAnchor(Arm arm, DoubleSupplier setpoint) {
        this.arm = arm;
        this.setpoint = setpoint;
    }

    @Override
    public void initialize() {
        this.arm.anchorSetpoint = setpoint.getAsDouble();
    }

    @Override
    public boolean isFinished() {
        return arm.isAnchorAtAngle(arm.anchorSetpoint);
    }
}
