package frc.robot.subsystems.leds.addressable;

import edu.wpi.first.wpilibj.util.Color;

import java.util.List;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.leds.addressable.patterns.FadeLEDPattern;
import frc.robot.subsystems.leds.addressable.patterns.LEDPattern;
import frc.robot.subsystems.leds.addressable.patterns.MorseCodePattern;
import frc.robot.subsystems.leds.addressable.patterns.SolidLEDPattern;
import frc.robot.subsystems.leds.addressable.patterns.TimedPattern;

public class LED extends SubsystemBase {
    private List<LEDStrip> ledStrips;

    private Timer timer;

    public LED() {
        // default pattern should be goose
        this(new SolidLEDPattern(Color.kOrange)); 
    }

    public LED(LEDPattern pattern) {
        this.ledStrips = List.of(
            new LEDStrip(Constants.LEDs.kLed1Port, Constants.LEDs.kLed1Size),
            new LEDStrip(Constants.LEDs.kLed2Port, Constants.LEDs.kLed2Size)
        );

        setPattern(pattern);

        this.timer = new Timer();
        this.timer.start();
    }

    public void setPattern(LEDPattern pattern) {
        for (LEDStrip strip : this.ledStrips)
            strip.setPattern(pattern);
        this.timer.restart();
    }

    public void weDiedLol(boolean morse, String message) {
        if (!morse)
            setPattern(new FadeLEDPattern(1, Color.kRed, Color.kBlack));
        else
            setPattern(new MorseCodePattern(Color.kRed, Color.kBlue, message));
    }

    public void setAllianceColor() {
        setPattern(new SolidLEDPattern(DriverStation.getAlliance() == Alliance.Red ? Color.kRed : Color.kBlue));
    }

    public void cube() {
        setPattern(new TimedPattern(new SolidLEDPattern(Color.kPurple)));
    }

    public void cone() {
        setPattern(new SolidLEDPattern(Color.kYellow));
    }

    @Override
    public void periodic() {

        if (DriverStation.isDisabled() || DriverStation.isEStopped()) {
            setPattern(new FadeLEDPattern(2.5, Color.kOrange, Color.kWhite));
        }

        // ordering of pattern changes is concerning (least to most urgent?)
        // need arm instance for armMode??? (not great)
        // which is why you probably shouldln't poll for these things
        // update it from their separate methods
        // (FSM WHEN?!?!?)

        // TODO: impl tipped, balanced, default goooose orange heartbeat (FadedLEDPattern?)
        // if (Math.abs(this.gyro.getPitch()) > 45)
        // weDiedLol(true, SmartDashboard.getString("Morse Message",
        // Constants.LEDs.kDefaultMessage));

        for (int i = 0; i < ledStrips.size(); i++) {
            ledStrips.get(i).update(timer.get());
        }
    }
}