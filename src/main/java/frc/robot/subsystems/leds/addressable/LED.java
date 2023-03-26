package frc.robot.subsystems.leds.addressable;

import java.util.List;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.leds.addressable.patterns.LEDPattern;

public class LED extends SubsystemBase {
    private List<LEDStrip> ledStrips;

    private LEDPattern pattern; 
    // private Gyro gyro;

    private Timer timer = new Timer();

    public LED(Gyro gyro, LEDPattern pattern) {
        this.ledStrips = List.of(
            new LEDStrip(Constants.LEDs.kLed1Port, Constants.LEDs.kLed1Size)
            // new LEDStrip(Constants.LEDs.kLed2Port, Constants.LEDs.kLed2Size)
        );

        this.timer = new Timer();
        this.timer.start();

        // default pattern should be goose
        if (pattern == null) {
            setPattern(Constants.LEDs.Patterns.kDefault); 
        } else {
            setPattern(pattern);
        }
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

        // if (DriverStation.isDisabled()) {
        //     System.out.println("disabled");
        //     setPattern(new FadeLEDPattern(2.5, Color.kOrange, Color.kWhite));
        // }

        // ordering of pattern changes is concerning (least to most urgent?)
        // need arm instance for armMode??? (not great)
        // which is why you probably shouldln't poll for these things
        // update it from their separate methods
        // (FSM WHEN?!?!?)

        // if (Math.abs(this.gyro.getPitch()) > 45)
        // weDiedLol(true, SmartDashboard.getString("Morse Message",
        // Constants.LEDs.kDefaultMessage));

        // if (this.pattern != null)
        // this.pattern.update(buffer, timer.get());

        // this.LED.setData(buffer);
        for (int i = 0; i < ledStrips.size(); i++) {
            ledStrips.get(i).update(timer.get());
        }
    }

    public List<LEDStrip> getLedStrips() {
        return this.ledStrips; 
    }
}