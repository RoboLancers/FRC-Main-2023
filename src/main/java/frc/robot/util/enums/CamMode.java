package frc.robot.util.enums;

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
