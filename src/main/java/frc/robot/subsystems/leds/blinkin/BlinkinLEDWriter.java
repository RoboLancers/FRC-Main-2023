package frc.robot.subsystems.leds.blinkin;

import edu.wpi.first.wpilibj.Timer;

public abstract class BlinkinLEDWriter {

    private final Timer timer; 
    private final double loopTime; 

    public BlinkinLEDWriter(double loopTime) {
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


    public static class EmptyBlinkinLEDWriter extends BlinkinLEDWriter {
        public EmptyBlinkinLEDWriter() {
            super(1); 
        }

        @Override
        protected double updateLEDs(double time) {
            return 0.99; // BLACK
        }
    }
}