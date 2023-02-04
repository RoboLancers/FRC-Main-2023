package frc.robot.util;

public enum PipelineIndex {
    LIMELIGHT_APRIL(0), TAPE(1), MANUAL_APRIL(2), AUTOPICKUP(4);

    private int value;

    private PipelineIndex(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
