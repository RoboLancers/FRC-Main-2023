package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private CANSparkMax motor;

    public double power = 0;
    
    public Intake(){
        this.motor = new CANSparkMax(Constants.Intake.kPort, MotorType.kBrushless);
        this.motor.setIdleMode(IdleMode.kBrake);
    }

    @Override
    public void periodic(){
        this.motor.set(power);
    }

    public void off(){
        this.power = 0;
    }

    public void forwardFast(){
        this.power = Constants.Intake.kHighPower;
    }

    public void forwardSlow(){
        this.power = Constants.Intake.kLowPower;
    }

    public void backwardFast(){
        this.power = -Constants.Intake.kHighPower;
    }

    public void backwardSlow(){
        this.power = -Constants.Intake.kLowPower;
    }
}
