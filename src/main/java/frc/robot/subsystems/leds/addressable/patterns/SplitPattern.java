package frc.robot.subsystems.leds.addressable.patterns;

import java.util.ArrayList;
import java.util.List;

import frc.robot.subsystems.leds.addressable.AddressableLEDBufferSection;

public class SplitPattern extends LEDPattern {

    private final List<SplitPatternSection> patterns = new ArrayList<>(); 

    public SplitPattern() {
        super(0);
    }

    @Override
    protected void updateLEDs(AddressableLEDBufferSection buffer, double time) {
        for (SplitPatternSection section : this.patterns) section.update(buffer, time);
    }

    public SplitPattern addSplit(int start, int end, LEDPattern pattern) {
        this.patterns.add(new SplitPatternSection(start, end, pattern)); 
        return this; 
    }

    public SplitPattern clear() {
        this.patterns.clear();
        return this; 
    }

    private static class SplitPatternSection {
        private final LEDPattern pattern; 
        private final int start; 
        private final int end;
        
        public SplitPatternSection(int start, int end, LEDPattern pattern) {
            this.start = start;
            this.end = end; 
            this.pattern = pattern; 
        }

        public void update(AddressableLEDBufferSection buffer, double time) {
            pattern.update(buffer.getSection(start, end), time);
        }
    }
    
    
}
