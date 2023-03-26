package frc.robot.subsystems.leds.addressable;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.robot.subsystems.leds.addressable.patterns.LEDPattern;

public class LEDStrip {
    public AddressableLED led;
    public AddressableLEDBuffer ledBuffer;

    private LEDPattern pattern;

    public LEDStrip(int port, int length) {
        this.led = new AddressableLED(port);
        this.ledBuffer = new AddressableLEDBuffer(length);
        this.led.setLength(length);
        this.led.start();
    }

    public LEDStrip(int port, int length, LEDPattern pattern) {
        this(port, length);
        this.pattern = pattern;
    }

    public void setPattern(LEDPattern pattern) {
        this.pattern = pattern;
    }

    public void update(double time) {
        if (pattern != null) {
            pattern.update(ledBuffer, time);
        }
        led.setData(ledBuffer);
    }
}