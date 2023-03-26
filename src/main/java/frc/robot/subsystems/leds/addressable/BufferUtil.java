package frc.robot.subsystems.leds.addressable;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;

public class BufferUtil {

    public static void setAll(AddressableLEDBuffer buffer, Color color) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setLED(i, color);
        }
    }
}
