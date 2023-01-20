package frc.robot.subsystems.gyro;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Gyro extends SubsystemBase
{
    private static final String drivetrain = null;

    private static final double rcw = 0;

    AHRS gyro = new AHRS(SPI.Port.kMXP);

        //temporary measurement
        double pitch;
        double setpoint;

        //pid variables 
        double kP = 1;//temporary
        double kI = 0;
        double kD = 0;
        double dt = 1;//change in time, temporary
        double integral, previous_error = 0;
        DifferentialDrive robotDrive;
    
    public void robotInit() 
    {
        Shuffleboard.getTab("gyro").add(gyro);
    }
    
    public double gyroPitchPeriodic()
    {
        //gets the angle of the robot throughout auto
        double pitch = gyro.getAngle();
        return pitch;
    }

    public void setSetpoint(double setpoint)
    {
        this.setpoint = setpoint; //just zero?
    }

    public void PID(AHRS gyro, double setpoint, drivetrain) //VariableDeclaratorId 
    {
        double error = 0 - gyroPitchPeriodic();
        this.integral += (error*dt); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
        double derivative = (error - this.previous_error) / dt;
        this.rcw = kP*error + kI*this.integral + kD*derivative;
    }

    public void execute()
    {
        gyroPitchPeriodic();
        setSetpoint(setpoint);
        
        PID(gyro, setpoint, drivetrain);  
        robotDrive.arcadeDrive(0, rcw);
    }
    
    private void PID(AHRS gyro, double setpoint2, String drivetrain2) {
    }

    public boolean end()
    {
        setpoint = 0;
    }
    
    public boolean isFinished()
    {
        return true;
    }
    
    //drivetrainpower = kP * pitch;
        //when kP is within 1 degree of 0, stop

}
