package frc.robot.subsystems.leds;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LED extends SubsystemBase {
    private PWMSparkMax LED; 

    private LEDWriter writer; 
    public LED(int port, int length) {
        this.LED = new PWMSparkMax(port);
        setWriter(new LEDWriter.EmptyLEDWriter()); 
    }

    public void setWriter(LEDWriter writer) {
        if (this.writer != null) this.writer.deactivate();
        this.writer = writer; 
        this.writer.activate();
    }

    @Override
    public void periodic() {
        if (this.writer != null) this.LED.set(this.writer.update());
    }
}