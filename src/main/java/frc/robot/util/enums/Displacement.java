package frc.robot.util.enums;

import frc.robot.Constants;

public enum Displacement {
    kLeft(Constants.GridAlign.kLeftOffset), kRight(Constants.GridAlign.kRightOffset), kCenter(0);

    private double offset;

    private Displacement(double offset) {
        this.offset = offset;
    }

    public double getOffset() {
        return offset;
    }
}
