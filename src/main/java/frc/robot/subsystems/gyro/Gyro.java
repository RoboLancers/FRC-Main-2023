package frc.robot.subsystems.gyro;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Gyro extends SubsystemBase {
    private AHRS _gyro = new AHRS(SPI.Port.kMXP);
        
    private double pitchOffset;
    private boolean initialized;

    // manual angular velocity estimation, discrete single step implementation so it might have issues with latency or bad feedback
    private double lastTimeStamp;
    private double lastPitch;
    private double pitchVelocity;

    public Gyro() {
        this.pitchOffset = 0;
        this.initialized = false;

        this.lastTimeStamp = Timer.getFPGATimestamp();
        this.lastPitch = this._gyro.getPitch();
        this.pitchVelocity = 0;
        
        _gyro.reset();
    }

    public double getPitch() {
        return _gyro.getPitch() - this.pitchOffset;
    }

    public double getPitchVelocity() {
        return this.pitchVelocity;
    }

    public Rotation2d getRotation2d() {
        return this._gyro.getRotation2d();
    }

    public double getYaw() {
        return this._gyro.getYaw(); 
    }

    public double getRate() {
        return this._gyro.getRate();
    }

    public void zeroYaw() {
        this._gyro.zeroYaw();
    }

    public void reset() {
        this._gyro.reset();
    }

    @Override
    public void periodic(){
        if (!this.initialized && _gyro.getPitch() != 0){
            this.pitchOffset = _gyro.getPitch();
            this.initialized = true;
        }

        this.pitchVelocity = (_gyro.getPitch() - this.lastPitch) / (Timer.getFPGATimestamp() - this.lastTimeStamp);
        this.lastTimeStamp = Timer.getFPGATimestamp();
        this.lastPitch = _gyro.getPitch();

        SmartDashboard.putNumber("gyro-pitch", this.getPitch());
        SmartDashboard.putNumber("gyro-pitch-velocity", this.pitchVelocity);
    }
}