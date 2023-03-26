package frc.robot.subsystems.leds.addressable;

import java.util.List;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.leds.addressable.patterns.LEDPattern;

public class LED extends SubsystemBase {

    private AddressableLED led; 

    private List<LEDStrip> ledStrips;

    private AddressableLEDBuffer buffer; 

    private LEDPattern pattern; 
    // private Gyro gyro;

    private Timer timer = new Timer();

    public LED() {
        this(Constants.LEDs.kLedPort, Constants.LEDs.kLedLength); 
    }

    public LED(int port, int length) {
        // default pattern should be goose
        this(port, length, Constants.LEDs.Patterns.kDefault); 
    }

    public LED(int port, int length, LEDPattern pattern) {
        this.led = new AddressableLED(port); 
        this.led.setLength(length);
        this.buffer = new AddressableLEDBuffer(length); 
        this.ledStrips = List.of(
            new LEDStrip(this.buffer, Constants.LEDs.kLed1Start, Constants.LEDs.kLed1End), 
            new LEDStrip(this.buffer, Constants.LEDs.kLed2Start, Constants.LEDs.kLed2End)
        );

        this.timer = new Timer();

        this.timer.start();
        this.led.start();

        setPattern(pattern);
        // this.gyro = gyro;
    }

    public void setPattern(LEDPattern pattern) {
        if (this.pattern == pattern) return; 
        for (LEDStrip strip : this.ledStrips)
            strip.setPattern(pattern);
        this.pattern = pattern; 
        this.timer.restart();
    }

    public void weDiedLol(boolean morse, String message) {
        if (!morse)
            setPattern(Constants.LEDs.Patterns.kDead);
        else
            setPattern(Constants.LEDs.Patterns.kDeadAlternate);
    }

    public void setAllianceColor() {
        setPattern(DriverStation.getAlliance() == Alliance.Red ? Constants.LEDs.Patterns.kAllianceRed : Constants.LEDs.Patterns.kAllianceBlue);
    }

    public void cube() {
        setPattern(Constants.LEDs.Patterns.kCube);
    }

    public void cone() {
        setPattern(Constants.LEDs.Patterns.kCone);
    }

    @Override
    public void periodic() {

        if (DriverStation.isDisabled()) {
            setPattern(Constants.LEDs.Patterns.kDead);
        }

        // TODO: create patterns for emergency stop, autonomous

        // ordering of pattern changes is concerning (least to most urgent?)
        // need arm instance for armMode??? (not great)
        // which is why you probably shouldln't poll for these things
        // update it from their separate methods

        // TODO: impl tipped, balanced
        // if (Math.abs(this.gyro.getPitch()) > 45)
        // weDiedLol(true, SmartDashboard.getString("Morse Message",
        // Constants.LEDs.kDefaultMessage));

        for (int i = 0; i < ledStrips.size(); i++) {
            ledStrips.get(i).update(timer.get());
        }
        led.setData(buffer);
    }

    public List<LEDStrip> getLedStrips() {
        return this.ledStrips; 
    }

    public AddressableLED getLed() {
        return this.led; 
    }
}