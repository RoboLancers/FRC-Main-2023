package frc.robot.util.limelight;

public enum CamMode {
    VISION(0), DRIVER(1);

    private int value;

    private CamMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
