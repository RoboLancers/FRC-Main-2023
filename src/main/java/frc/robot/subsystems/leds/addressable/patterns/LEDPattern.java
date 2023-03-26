package frc.robot.subsystems.leds.addressable.patterns;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public abstract class LEDPattern {

    private double loopTime; 

    public LEDPattern(double loopTime) {
        this.loopTime = loopTime;
    }

    protected abstract void updateLEDs(AddressableLEDBuffer buffer, double time);
    
    public void update(AddressableLEDBuffer buffer, double time) {
        updateLEDs(buffer, time % loopTime);
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
        protected void updateLEDs(AddressableLEDBuffer buffer, double time) {}
    }
}