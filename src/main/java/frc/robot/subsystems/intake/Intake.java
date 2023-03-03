package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private CANSparkMax motor;

    private int direction = 0;
    
    public Intake() {
        this.motor = new CANSparkMax(Constants.Intake.kPort, MotorType.kBrushless);
    }

    @Override
    public void periodic() {
        double power = direction == 0 ? 0 : direction == 1 ? Constants.Intake.kForwardPower : Constants.Intake.kBackwardPower;

        this.motor.set(power);
    }

    public void off() {
        this.direction = 0;
    }

    // TODO: modify these or motor direction
    public void outtake() {
        this.direction = 1;
    }

    public void intake() {
        this.direction = -1;
    }
}
