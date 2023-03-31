package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
    private CANSparkMax motor = new CANSparkMax(Constants.Intake.kIntakePort, MotorType.kBrushless);
    private DigitalInput beamBreak = new DigitalInput(Constants.Intake.kBeambreakPort); 

    public double power = 0;
    
    public Intake() {
        this.motor.setIdleMode(IdleMode.kBrake);
        this.motor.setSmartCurrentLimit(30);

        // SmartDashboard.putNumber("intake-fast", SmartDashboard.getNumber("intake-fast", Constants.Intake.kHighPower));
        // SmartDashboard.putNumber("intake-slow", SmartDashboard.getNumber("intake-slow", Constants.Intake.kLowPower));
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("has element", hasElement()); 
        this.motor.set(power);
    }

    public void off() {
        this.power = 0;
    }

    public void outtakeFast() {
        this.power = -Constants.Intake.kHighPower;
    }

    public void outtakeSlow() {
        this.power = -Constants.Intake.kLowPower;
    }

    public void intakeFast() {
        this.power = Constants.Intake.kHighPower;
    }

    public void intakeSlow() {
        this.power = Constants.Intake.kLowPower;
    }

    public boolean hasElement() {
        return beamBreak.get(); 
    }
}
