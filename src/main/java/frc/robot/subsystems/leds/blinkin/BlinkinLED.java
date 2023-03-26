package frc.robot.subsystems.leds.blinkin;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BlinkinLED extends SubsystemBase {
    private PWMSparkMax LED; 

    private BlinkinLEDWriter writer; 
    public BlinkinLED(int port) {
        this.LED = new PWMSparkMax(port);
        setWriter(new BlinkinLEDWriter.EmptyBlinkinLEDWriter()); 
    }

    public void setWriter(BlinkinLEDWriter writer) {
        if (this.writer != null) this.writer.deactivate();
        this.writer = writer; 
        this.writer.activate();
    }

    @Override
    public void periodic() {
        if (this.writer != null) this.LED.set(this.writer.update());
    }
}