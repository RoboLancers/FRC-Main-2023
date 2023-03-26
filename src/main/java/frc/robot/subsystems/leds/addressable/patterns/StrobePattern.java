package frc.robot.subsystems.leds.addressable.patterns;

import edu.wpi.first.wpilibj.util.Color;

// how do we do this lmao I am so lost on the new stuff
// uhh good question
// 1s

// also off topic but we should implement an abstraction for multiple LED strips
// i was gonna just have multiple subsystems in that case 
// yea
// I think thats an oversight
// I think we should have them synced tho
// shit LEDs are more complicated than I thought 
// it shouldnt be too bad to get them synced

public class StrobePattern extends TimedPattern {

    public StrobePattern(Color c, double strobeTime) {
        addPattern(new SolidLEDPattern(c), strobeTime);
        timedWait(strobeTime);
    }

}
