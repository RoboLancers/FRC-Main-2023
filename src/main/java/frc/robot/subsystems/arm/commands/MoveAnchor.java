package frc.robot.subsystems.arm.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.arm.Arm;

public class MoveAnchor extends CommandBase {
    Arm arm;

    public MoveAnchor(Arm arm, double setpoint){
        this.arm = arm;

        arm.anchorSetpoint = setpoint;
    }

    public MoveAnchor(Arm arm, DoubleSupplier setpoint){
        this.arm = arm;

        arm.anchorSetpoint = setpoint.getAsDouble();
    }

    @Override
    public boolean isFinished(){
        return arm.isAnchorAtAngle(arm.anchorSetpoint);
    }
}
