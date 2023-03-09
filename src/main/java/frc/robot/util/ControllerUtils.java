package frc.robot.util;

public class ControllerUtils {
    public static double applyDeadband(double value, double deadbandValue){
        return Math.abs(value) > deadbandValue ? value : 0;
    }

    public static double range(double value, double max){
        return range(value, -max, max);
    }

    public static double range(double value, double min, double max){
        return Math.min(max, Math.max(min, value));
    }

    public static double squareKeepSign(double value){
        return Math.signum(value) * Math.pow(value, 2);
    }

    public static double sqrtKeepSign(double value){
        return Math.signum(value) * Math.sqrt(Math.abs(value));
    }

    public static double RPMtoRPS(double value) {
        return value / 60;
    }

    public static double clamp(double value, double min, double max){
        if (value < min) return min;

        if (value > max) return max;

        return value;
    }
}