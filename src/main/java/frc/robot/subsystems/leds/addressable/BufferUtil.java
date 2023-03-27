package frc.robot.subsystems.leds.addressable;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class BufferUtil {

    public static void setAll(AddressableLEDBufferSection buffer, Color color) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setLED(i, color);
        }
    }

    public static void setAll(AddressableLEDBufferSection buffer, Color8Bit color) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setLED(i, color);
        }
    }

    public static void setAllRGB(AddressableLEDBufferSection buffer, int r, int g, int b) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setRGB(i, r, g, b);
        }
    }

    public static void setAllHSV(AddressableLEDBufferSection buffer, int h, int s, int v) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setHSV(i, h, s, v);
        }
    }
}
