package frc.robot.util;

import frc.robot.Constants;

public class DriverController extends Controller {
    double throttleMultiplier = Constants.Drivetrain.kThrottleMultiplier;
    double turnMultiplier = Constants.Drivetrain.kTurnMultiplier;

    private double deadzone; 

    public enum Mode {
        NORMAL,
        SLOW
    }
    
    private Mode mode;

    public DriverController(int port) {
        this(port, 0.15); 
    }

    public DriverController(int port, double deadzone) {
        super(port); 
        this.deadzone = deadzone; 
    }


    // TODO: dunno if this should being in Controller or DriverController
    public double getLeftStickX() {
        return ControllerUtils.applyDeadband(super.getLeftStickX(), deadzone);
    }

    public double getLeftStickY() {
        return ControllerUtils.applyDeadband(super.getLeftStickY(), deadzone);
    }

    public double getRightStickX() {
        return ControllerUtils.applyDeadband(super.getRightStickX(), deadzone); 
    }

    public double getRightStickY() {
        return ControllerUtils.applyDeadband(super.getRightStickY(), deadzone);
    }

    public double getThrottle() {
        return -getLeftStickY() * throttleMultiplier;
    }

    public double getTurn() {
        return -getRightStickX() * turnMultiplier;
    }

    public boolean getQuickTurn() {
        return Math.abs(getLeftStickY()) < 0.05;
    }
    
    public void setSlowMode(Mode m) {
        if (m == Mode.NORMAL) {
            throttleMultiplier = Constants.Drivetrain.kThrottleMultiplier;
            turnMultiplier = Constants.Drivetrain.kTurnMultiplier;
            mode = Mode.SLOW;
        } else {
            throttleMultiplier = Constants.Drivetrain.kThrottleMultiplierSM;
            turnMultiplier = Constants.Drivetrain.kTurnMultiplierSM;
            mode = Mode.NORMAL;
        }
    }
    
    public Mode getSlowMode() {
        return this.mode; 
    }
}
