package frc.robot.subsystems.leds;

import edu.wpi.first.wpilibj.Timer;

public abstract class LEDWriter {

    private final Timer timer; 
    private final double loopTime; 

    public LEDWriter(double loopTime) {
        this.loopTime = loopTime; 
        this.timer = new Timer(); 
    }

    protected abstract double updateLEDs(double time);
    
    public double update() {
        return updateLEDs(timer.get() % loopTime);
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
        protected double updateLEDs(double time) {
            return 0.99; // black
        }
    }
}