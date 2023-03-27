package frc.robot.subsystems.leds.blinkin;

public class PWMLEDWriter extends BlinkinLEDWriter {

    private final double pattern; 

    public PWMLEDWriter(double pattern) {
        super(1);
        this.pattern = pattern; 
    }

    @Override
    protected double updateLEDs(double time) {
        return pattern; 
    }
    
}
