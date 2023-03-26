package frc.robot.subsystems.leds.addressable.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;

public class TimedPattern extends LEDPattern {
    private List<TimedLEDPattern> patterns = new ArrayList<>(); 

    public TimedPattern() {
        this(new ArrayList<>()); 
    }

    public TimedPattern(List<TimedLEDPattern> patterns) {
        super(getTotalTime(patterns)); 
        this.patterns = patterns; 
    }

    private static double getTotalTime(List<TimedLEDPattern> patterns) {
        return IntStream.range(0, patterns.size()).mapToDouble(a -> a).reduce(0, (a, b) -> a + patterns.get((int) b).getTime()); // no idea if this works
    }

    public void addPattern(LEDPattern pattern, double time) {
        this.patterns.add(new TimedLEDPattern(pattern, time)); 
        setLoopTime(getTotalTime(this.patterns));
    }

    public void addPattern(LEDPattern pattern) {
        addPattern(pattern, pattern.getLoopTime()); // do it for one loop
    }

    public void timedWait(double time) {
        addPattern(new SolidLEDPattern(Color.kBlack), time); 
    }

    @Override
    protected void updateLEDs(AddressableLEDBuffer buffer, double time) {
        double total = 0; 
        for (TimedLEDPattern pattern : patterns) {
            if (total <= time && total + pattern.getTime() >= time) {
                pattern.getPattern().update(buffer, time - total);
                return;
            }
            total += pattern.getTime(); 
        }
    }
    
    public static class TimedLEDPattern {
        private LEDPattern pattern; 
        private double time; 

        public TimedLEDPattern(LEDPattern pattern, double time) {
            this.pattern = pattern; 
            this.time = time; 
        }

        public LEDPattern getPattern() {
            return this.pattern; 
        }

        public double getTime() {
            return this.time; 
        }
    }

    public static class TimedPatternBuilder {
        public List<TimedPattern.TimedLEDPattern> patterns = new ArrayList<>(); 
        public TimedPatternBuilder() {}

        public void displayFor(LEDPattern pattern, double time) {
            this.patterns.add(new TimedLEDPattern(pattern, time)); 
        }

        public List<TimedPattern.TimedLEDPattern> getPatterns() {
            return patterns; 
        }

        public TimedPattern build() {
            return new TimedPattern(patterns); 
        }

    }
}