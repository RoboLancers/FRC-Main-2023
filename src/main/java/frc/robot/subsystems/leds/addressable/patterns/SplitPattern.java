package frc.robot.subsystems.leds.addressable.patterns;

import frc.robot.subsystems.leds.addressable.AddressableLEDBufferSection;

public class SplitPattern extends LEDPattern {

    private final int start1; 
    private final int end1; 
    private final int start2; 
    private final int end2;  

    private final LEDPattern pattern1; 
    private final LEDPattern pattern2; 

    public SplitPattern(int start1, int end1, LEDPattern pattern1, int start2, int end2, LEDPattern pattern2) {
        super(0);
        this.start1 = start1; 
        this.end1 = end1; 
        this.start2 = start2; 
        this.end2 = end2; 

        this.pattern1 = pattern1; 
        this.pattern2 = pattern2; 
    }

    @Override
    protected void updateLEDs(AddressableLEDBufferSection buffer, double time) {
        pattern1.update(buffer.getSection(start1, end1), time);
        pattern2.update(buffer.getSection(start2, end2), time);
    }
    
    
}
