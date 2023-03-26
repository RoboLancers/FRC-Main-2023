package frc.robot.subsystems.leds.addressable.patterns;

import edu.wpi.first.wpilibj.util.Color;
import frc.robot.util.StringToMorse;

public class MorseCodePattern extends TimedPattern {

    public MorseCodePattern(Color c1, Color c2, String input) {
        this(c1, c2, input, 1); 
    }

    public MorseCodePattern(Color c1, Color c2, String input, double morseCodeTimeout) {
        super(StringToMorse.convert(input).chars()
                .mapToObj(character -> new TimedPattern.TimedLEDPattern(
                        new SolidLEDPattern(character == '.' ? c1 : character == '-' ? c2 : Color.kBlack),
                        morseCodeTimeout)).toList());
    }

    public MorseCodePattern(Color c, String input, double morseCodeTimeout1, double morseCodeTimeout2) {
    super(StringToMorse.convert(input).chars()
            .mapToObj(character -> new TimedPattern.TimedLEDPattern(
                    new SolidLEDPattern(character == '.' || character == '-' ? c : Color.kBlack),
                    character == '.' ? morseCodeTimeout1 : character == '-' ? morseCodeTimeout2 : 1
                    )).toList());
    }


    // Potentially doesnt work cause of timing issues
    // protected void updateLEDs(AddressableLEDBuffer buffer, double time, Color color1, Color color2) {
    //     for (char c : morseCode.toCharArray()) {
    //         if (c == '.') {
    //             BufferUtil.setAll(buffer, color1);
    //         } else if (c == '-') {
    //             BufferUtil.setAll(buffer, color2);
    //         } else {
    //             BufferUtil.setAll(buffer, Color.kBlack);
    //         }
    //     }
    // }
}
