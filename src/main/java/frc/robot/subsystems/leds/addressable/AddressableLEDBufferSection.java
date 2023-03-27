package frc.robot.subsystems.leds.addressable;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class AddressableLEDBufferSection {
    private AddressableLEDBuffer buffer; 
    private int start; 
    private int end; 

    public AddressableLEDBufferSection(AddressableLEDBuffer buffer, int start, int end) {
        this.buffer = buffer; 
        this.start = start; 
        this.end = end; 
    }

    public AddressableLEDBufferSection getSection(int start, int end) {
        return new AddressableLEDBufferSection(buffer, getOffsetIndex(start), getOffsetIndex(end)); 
    }

    private int getOffsetIndex(int i) {
        int index = start + i; 
        if (index > end) throw new ArrayIndexOutOfBoundsException(); 
        return index; 
    }

    public void setRGB(int i, int r, int g, int b) {
        buffer.setRGB(getOffsetIndex(i), r, g, b);
    }

    public void setHSV(int i, int h, int s, int v) {
        buffer.setHSV(getOffsetIndex(i), h, s, v);
    }

    public void setLED(int i, Color color) {
        buffer.setLED(getOffsetIndex(i), color);
    }

    public void setLED(int i, Color8Bit color) {
        buffer.setLED(getOffsetIndex(i), color);
    }

    public int getLength() {
        return this.end - this.start; 
    }

    public int getStart() {
        return start; 
    }

    public int getEnd() {
        return end; 
    }
}
