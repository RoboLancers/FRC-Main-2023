package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
//import com.revrobotics.RelativeEncoder;
//import com.revrobotics.CANSparkMax.IdleMode;

import frc.robot.util.XboxController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.drive.arcadeDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Drivetrain extends SubsystemBase{
    private final CANSparkMax leftMotor1 = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax leftMotor2 = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax leftMotor3 = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);

    public final MotorControllerGroup leftMotors = new MotorControllerGroup(leftMotor1, leftMotor2, leftMotor3);

    private final CANSparkMax rightMotor1 = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rightMotor2 = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rightMotor3 = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);

    public final MotorControllerGroup rightMotors = new MotorControllerGroup(rightMotor1, rightMotor2, rightMotor3);

    DifferentialDrive drive = new DifferentialDrive(leftMotors, rightMotors);

    SlewRateLimiter throttleLimit = new SlewRateLimiter(1.5);
    SlewRateLimiter turnLimit = new SlewRateLimiter(1.5);

    public Drivetrain(XboxController driverController){
        rightMotor1.setInverted(true);
        rightMotor2.setInverted(true);
        rightMotor3.setInverted(true);
    }

    public void arcadeDrive(double turn, double throttle) {
        drive.arcadeDrive(throttleLimit.calculate(throttle), -turnLimit.calculate(turn));
    }

    }
    
