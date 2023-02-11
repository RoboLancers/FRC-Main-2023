package frc.robot.subsystems.gyro;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.math.controller.PIDController;
//import edu.wpi.first.wpilibj.SPI;
//import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import frc.robot.Constants;
import frc.robot.Constants.BalanceConstants;
import frc.robot.subsystems.drivetrain.Drivetrain;
//import frc.robot.util.XboxController;

public class Balance extends PIDCommand{
    
    private AHRS gyro;
    private Drivetrain drivetrain;//replace with actual drivetrain

    // public double gyroPitch()
    // {
    //     //gets the angle of the robot throughout auto
    //     return gyro.getPitch();
    // }

    public Balance(AHRS gyro, double setpoint, Drivetrain drivetrain) {
        super(
            new PIDController(BalanceConstants.kP, BalanceConstants.kI, BalanceConstants.kD),
            gyro::getPitch, 
            // Set reference to target
            () -> setpoint,
            // Pipe output to turn robot
            (outputPower) -> {
                SmartDashboard.putNumber("Balance Output", outputPower);
                drivetrain.arcadeDrive(outputPower, 0);
            },
            drivetrain
        );
    
        this.getController().setTolerance(BalanceConstants.kErrorThreshold);

        SmartDashboard.putBoolean("Balance Running", true);
    
        this.gyro = gyro;
        this.drivetrain = drivetrain;

        addRequirements(drivetrain);
    }

    @Override
    public void end(boolean interrupted){
        SmartDashboard.putBoolean("Balance Running", false);
        drivetrain.arcadeDrive(0, 0);
    }

    @Override
    public boolean isFinished(){
        return this.getController().atSetpoint();
    }
}