package frc.robot.subsystems.leds.addressable.patterns;

import frc.robot.subsystems.leds.addressable.AddressableLEDBufferSection;

public abstract class LEDPattern {

    private double loopTime; 

    public LEDPattern(double loopTime) {
        this.loopTime = loopTime;
    }

    protected abstract void updateLEDs(AddressableLEDBufferSection buffer, double time);
    
    public void update(AddressableLEDBufferSection buffer, double time) {
        updateLEDs(buffer, loopTime == 0 ? time : time % loopTime);
    }

    public double getLoopTime() {
        return this.loopTime; 
    }

    public void setLoopTime(double loopTime) {
        this.loopTime = loopTime; 
    }

    public static class EmptyLEDPattern extends LEDPattern {
        public EmptyLEDPattern() {
            super(1); 
        }

        @Override
        protected void updateLEDs(AddressableLEDBufferSection buffer, double time) {}
    }
}