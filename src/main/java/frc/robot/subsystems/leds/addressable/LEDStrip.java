package frc.robot.subsystems.leds.addressable;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.robot.subsystems.leds.addressable.patterns.LEDPattern;

public class LEDStrip {
    public AddressableLEDBufferSection ledBuffer;

    private LEDPattern pattern;

    public LEDStrip(AddressableLEDBuffer buffer, int start, int end) {
        this.ledBuffer = new AddressableLEDBufferSection(buffer, start, end);
    }

    public LEDStrip(AddressableLEDBuffer buffer, int port, int length, LEDPattern pattern) {
        this(buffer, port, length);
        this.pattern = pattern;
    }

    public void setPattern(LEDPattern pattern) {
        this.pattern = pattern;
    }

    public void update(double time) {
        if (pattern != null) pattern.update(ledBuffer, time);
    }
}