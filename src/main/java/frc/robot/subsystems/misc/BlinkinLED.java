package frc.robot.subsystems.misc;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.gyro.Gyro;

public class BlinkinLED extends SubsystemBase {
    private PWMSparkMax ledController;
    // if this breaks, it's Json Y's fault
    private Gyro gyro;

    enum HEARTBEAT {
        SLOW(0.03),
        MEDIUM(0.05),
        FAST(0.07);

        private double value;

        private HEARTBEAT(double value) {
            this.value = value;

        }

        public double getValue() {
            return value;
        }

    }

    public BlinkinLED(Gyro gyro) {
        this.gyro = gyro;
        this.ledController = new PWMSparkMax(Constants.LED_PWM_PORT);

        // default to goose
        this.goose();

    }

    @Override
    public void periodic() {

        //
        if (this.gyro.getPitch() > 45.0) {
            this.weDiedLol();
        }
    }

    public void setLED(double value) {
        if (value >= -1.0 && value <= 1.0) {
            this.ledController.set(value);
        }
    }

    public void goose() {
        this.setLED(0.65);
    }

    public void weDiedLol() {
        this.setLED(-0.11);
    }

    public void rainbow() {
        this.setLED(-0.99);
    }

    public void partyRainbow() {
        this.setLED(-0.97);
    }

    public void oceanRainbow() {
        this.setLED(-0.95);
    }

    public void larsonRed() {
        this.setLED(-0.35);
    }

    public void colorOne(HEARTBEAT hearbeat) {
        this.setLED(hearbeat.getValue());
    }

    public void dualColorWave() {
        this.setLED(0.53);
    }

    public void setAllianceColor(Boolean pattern) {
        Boolean isBlue = NetworkTableInstance.getDefault().getTable("FMSInfo").getEntry("IsBlueAlliance")
                .getBoolean(true);

        // solid blue
        double blue = 0.87;

        // solid red
        double red = 0.61;

        if (pattern) {
            blue = -0.83;
            red = -0.85;
        }
        if (!isBlue) {
            this.setLED(red);
        } else {
            this.setLED(blue);
        }

    }

}