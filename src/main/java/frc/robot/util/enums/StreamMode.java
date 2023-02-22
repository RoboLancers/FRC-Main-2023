package frc.robot.util.enums;

public enum StreamMode {
    STANDARD(0), PIP_MAIN(1), PIP_SECONDARY(2);

    private int value;

    private StreamMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
