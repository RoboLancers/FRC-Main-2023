package frc.robot.subsystems.gyro;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Gyro extends SubsystemBase {
    private AHRS _gyro = new AHRS(SPI.Port.kMXP); 
        
    double pitchOffset;
    boolean initialized;

    public Gyro(){
        this.pitchOffset = 0;
        this.initialized = false;
        
        _gyro.calibrate();
    }

    public double getPitch(){
        return _gyro.getPitch() - this.pitchOffset;
    }

    @Override
    public void periodic(){
        if (! this.initialized && _gyro.getPitch() != 0){
            this.pitchOffset = _gyro.getPitch();
            this.initialized = true;
        }

        SmartDashboard.putNumber("gyro-roll", this._gyro.getRoll());
        SmartDashboard.putNumber("gyro-pitch", this.getPitch());
    }
}