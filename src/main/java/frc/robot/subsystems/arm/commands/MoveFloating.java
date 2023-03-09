package frc.robot.subsystems.arm.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.arm.Arm;
import java.time.chrono.MinguoChronology;

public class MoveFloating extends CommandBase {
    Arm arm;

    public MoveFloating(Arm arm, double setpoint){
        this.arm = arm;

        arm.floatingSetpoint = setpoint;
    }

    public MoveFloating(Arm arm, DoubleSupplier setpoint){
        this.arm = arm;

        arm.floatingSetpoint = setpoint.getAsDouble();
    }

    @Override
    public boolean isFinished(){
        return this.arm.isFloatingAtAngle(arm.floatingSetpoint);
    }
}
