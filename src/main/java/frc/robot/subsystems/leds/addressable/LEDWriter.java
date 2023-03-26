package frc.robot.subsystems.leds.addressable;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;

public abstract class LEDWriter {

    private final Timer timer; 
    private final double loopTime; 

    public LEDWriter(double loopTime) {
        this.loopTime = loopTime; 
        this.timer = new Timer(); 
    }

    protected abstract void updateLEDs(AddressableLEDBuffer buffer, double time);
    
    public void update(AddressableLEDBuffer buffer) {
        updateLEDs(buffer, timer.get() % loopTime);
    }

    public void activate() {
        this.timer.reset();
        this.timer.start();
    }

    public void deactivate() {
        this.timer.stop();
    }


    public static class EmptyLEDWriter extends LEDWriter {
        public EmptyLEDWriter() {
            super(1); 
        }

        @Override
        protected void updateLEDs(AddressableLEDBuffer buffer, double time) {}
    }
}