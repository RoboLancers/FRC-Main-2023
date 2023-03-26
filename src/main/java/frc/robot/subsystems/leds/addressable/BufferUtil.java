package frc.robot.subsystems.leds.addressable;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class BufferUtil {

    public static void setAll(AddressableLEDBuffer buffer, Color color) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setLED(i, color);
        }
    }

    public static void setAll(AddressableLEDBuffer buffer, Color8Bit color) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setLED(i, color);
        }
    }

    public static void setAllRGB(AddressableLEDBuffer buffer, int r, int g, int b) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setRGB(i, r, g, b);
        }
    }

    public static void setAllHSV(AddressableLEDBuffer buffer, int h, int s, int v) {
        for (int i = 0; i < buffer.getLength(); i++) {
            buffer.setHSV(i, h, s, v);
        }
    }
}
