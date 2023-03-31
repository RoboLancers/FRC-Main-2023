package frc.robot.subsystems.leds.addressable.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import frc.robot.subsystems.leds.addressable.AddressableLEDBufferSection;

public class ConditionalPattern extends LEDPattern {

    private final List<LEDCondition> conditions = new ArrayList<>(); 
    private LEDPattern patternElse; 

    public ConditionalPattern() {
        this(LEDPattern.kEmpty); 
    }
    
    public ConditionalPattern(LEDPattern patternElse) {
        super(0); 
        this.patternElse = patternElse;  
    }

    @Override
    protected void updateLEDs(AddressableLEDBufferSection buffer, double time) {
        for (LEDCondition cond : this.conditions) {
            if (cond.getTrue()) {
                cond.getPattern().update(buffer, time);
                return; 
            }
        }
        patternElse.update(buffer, time); 
    }

    public ConditionalPattern addCondition(BooleanSupplier condition, LEDPattern pattern) {
        this.conditions.add(new LEDCondition(condition, pattern)); 
        return this; 
    }

    public ConditionalPattern clear() {
        this.conditions.clear();
        return this; 
    }

    public void setElse(LEDPattern pattern) {
        this.patternElse = pattern; 
    }

    private static class LEDCondition {
        private BooleanSupplier condition; 
        private LEDPattern pattern; 

        public LEDCondition(BooleanSupplier condition, LEDPattern pattern) {
            this.condition = condition; 
            this.pattern = pattern; 
        }

        public boolean getTrue() {
            return condition.getAsBoolean(); 
        }

        public LEDPattern getPattern() {
            return pattern; 
        }
    }
}
