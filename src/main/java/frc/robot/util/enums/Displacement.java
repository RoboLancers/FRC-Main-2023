package frc.robot.util.enums;

import frc.robot.Constants;

public enum Displacement {
    LEFT(Constants.GridAlign.kDispLeft), RIGHT(Constants.GridAlign.kDispRight), CENTER(0), NONE(-1);

    private double value;

    private Displacement(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
