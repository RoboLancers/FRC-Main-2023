package frc.robot.subsystems.gyro;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.BalanceConstants;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.controller.PIDController;


public class Balance extends PIDCommand{
    
    private static final String drivetrain = null;
    
    AHRS gyro = new AHRS(SPI.Port.kMXP);
    
  

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



    public Balance(AHRS gyro, double setpoint, Drivetrain drivetrain) {
        super(
            new PIDController(BalanceConstants.kTurnP, BalanceConstants.kTurnI, BalanceConstants.kTurnD),
            gyro.getAngle(),
            // Set reference to target
            0,
            // Pipe output to turn robot
            output -> drive.arcadeDrive(0, output),
            // Require the drive
            drive);



    
    }
}



