package frc.robot.subsystems.leds.addressable;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LED extends SubsystemBase {
    private AddressableLED LED; 

    private AddressableLEDBuffer buffer; 

    private LEDWriter writer; 
    public LED(int port, int length) {
        this.LED = new AddressableLED(port);
        this.buffer = new AddressableLEDBuffer(length); 
        setWriter(new LEDWriter.EmptyLEDWriter()); 
    }

    public void setWriter(LEDWriter writer) {
        if (this.writer != null) this.writer.deactivate();
        this.writer = writer; 
        this.writer.activate();
    }

    @Override
    public void periodic() {
        if (this.writer != null) this.writer.update(buffer);
        this.LED.setData(buffer);
    }
}