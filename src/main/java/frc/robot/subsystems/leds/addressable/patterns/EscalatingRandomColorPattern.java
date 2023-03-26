package frc.robot.subsystems.leds.addressable.patterns;

import edu.wpi.first.wpilibj.util.Color;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class EscalatingRandomColorPattern extends LEDPattern {

    public EscalatingRandomColorPattern() {
        super(1);
    }

    @Override
    protected void updateLEDs(AddressableLEDBuffer buffer, double time) {
        for (int i = 0; i < buffer.getLength(); i++) {
            if (i != 0) {
                buffer.setLED(i - 1, Color.kBlack);
            }
            buffer.setRGB(i, (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
            buffer.setRGB(i + 1, (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));

        }
    }
}
