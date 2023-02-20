package frc.robot.util.enums;

public enum Snapshot {
    RESET(0), TAKE(1);

    private int value;


    private Snapshot(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
