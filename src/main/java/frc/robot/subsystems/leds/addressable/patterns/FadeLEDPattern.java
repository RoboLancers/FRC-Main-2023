package frc.robot.subsystems.leds.addressable.patterns;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;

public class FadeLEDPattern extends LEDPattern {

    private final Color c1; 
    private final Color c2; 
      
    public FadeLEDPattern(double loopTime, Color c1, Color c2) {
        super(loopTime);
        this.c1 = c1; 
        this.c2 = c2; 
    }

    @Override
    protected void updateLEDs(AddressableLEDBuffer buf, double time) {
        double r = time < getLoopTime() / 2 ? MathUtil.interpolate(c1.red, c2.red, time * 2 / getLoopTime()) : MathUtil.interpolate(c2.red, c1.red, (time - getLoopTime() / 2) * 2 / getLoopTime()); 
        double g = time < getLoopTime() / 2 ? MathUtil.interpolate(c1.green, c2.green, time * 2 / getLoopTime()) : MathUtil.interpolate(c2.green, c1.green, (time - getLoopTime() / 2) * 2 / getLoopTime()); 
        double b = time < getLoopTime() / 2 ? MathUtil.interpolate(c1.blue, c2.blue, time * 2 / getLoopTime()) : MathUtil.interpolate(c2.blue, c1.blue, (time - getLoopTime() / 2) * 2 / getLoopTime()); 

        for (int i = 0; i < buf.getLength(); i++) {
            buf.setRGB(i, (int) r, (int) g, (int) b);
        }
    }
    
}
