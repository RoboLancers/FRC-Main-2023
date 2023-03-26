package frc.robot.subsystems.leds.addressable;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class SolidLEDWriter extends LEDWriter {

    private final int r;
    private final int g;
    private final int b; 

    public SolidLEDWriter(int r, int g, int b) {
        super(1);
        this.r = r; 
        this.g = g; 
        this.b = b; 
    }

    @Override
    protected void updateLEDs(AddressableLEDBuffer buf, double time) {
        for (int i = 0; i < buf.getLength(); i++) {
            buf.setRGB(i, r, g, b);
        }
    }
    
}
