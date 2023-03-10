package frc.robot.subsystems.arm.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.arm.Arm;
import java.time.chrono.MinguoChronology;

public class MoveFloating extends CommandBase {
    Arm arm;
    DoubleSupplier setpoint;

    public MoveFloating(Arm arm, double setpoint){
        this.arm = arm;
        this.setpoint = () -> setpoint;
    }

    public MoveFloating(Arm arm, DoubleSupplier setpoint){
        this.arm = arm;
        this.setpoint = setpoint;
    }

    @Override
    public void initialize(){
        this.arm.floatingSetpoint = this.setpoint.getAsDouble();
    }

    @Override
    public boolean isFinished(){
        return this.arm.isFloatingAtAngle(arm.floatingSetpoint);
    }
}
