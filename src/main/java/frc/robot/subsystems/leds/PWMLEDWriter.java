package frc.robot.subsystems.leds;

public class PWMLEDWriter extends LEDWriter {

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
