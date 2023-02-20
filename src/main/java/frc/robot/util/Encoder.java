package frc.robot.util;

import com.revrobotics.RelativeEncoder;

/*
* Introduced to fix the latency issue that happens when actually setting the position of encoders
* */
public class Encoder {
    private final RelativeEncoder encoder;
    private double offset;

    public Encoder(RelativeEncoder encoder) {
        this.encoder = encoder;
        this.offset = 0;

    }

    public void setPosition(double position) {
        this.offset = encoder.getPosition() - position;
    }

    public double getPosition() {
        return encoder.getPosition() - this.offset;
    }

    public RelativeEncoder getEncoder() {
        return encoder;
    }
}
