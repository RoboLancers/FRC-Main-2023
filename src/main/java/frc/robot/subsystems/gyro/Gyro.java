package frc.robot.subsystems.gyro;

import com.kauailabs.navx.frc.AHRS;
//import frc.robot.Robot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Gyro extends SubsystemBase {
    private AHRS _gyro = new AHRS(SPI.Port.kMXP);
    
        
        
    double rollOffset;
    boolean initialized;

    public Gyro(){
        this.rollOffset = 0;
        this.initialized = false;
        
        _gyro.calibrate();

        SmartDashboard.putNumber("gyro offset", this.rollOffset);
    }

    public double getRoll(){
        return _gyro.getRoll() - this.rollOffset;
    }

    @Override
    public void periodic(){
        if (! this.initialized && _gyro.getRoll() != 0){
            this.rollOffset = _gyro.getRoll();
            this.initialized = true;
        }
    }
}
