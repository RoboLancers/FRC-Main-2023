package frc.robot.subsystems.leds.addressable.patterns;

import edu.wpi.first.wpilibj.util.Color;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class EscalatingRandomColorPattern extends LEDPattern {

    private int size; 
    
    public EscalatingRandomColorPattern(int size, double duration) {
        super(duration);
        this.size = size; 
    }

    @Override
    protected void updateLEDs(AddressableLEDBuffer buffer, double time) {
        int indexStart = (int) (time * buffer.getLength() / getLoopTime()); 
        for (int i = 0; i < buffer.getLength(); i++) buffer.setLED(i, Color.kBlack);

        for (int i = 0; i < size; i++) {
            int index = (indexStart + i) % buffer.getLength(); 
            buffer.setRGB(index, (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        }
    }
}
