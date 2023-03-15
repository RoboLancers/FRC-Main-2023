package frc.robot.util;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import frc.robot.Constants;

public class DriverController extends Controller {
    double throttleMultiplier = Constants.Drivetrain.kThrottleMultiplier;
    double turnMultiplier = Constants.Drivetrain.kQuickTurnMultiplier;

    private double lastEffThrottle = 0; 

    private final SlewRateLimiter throttleForwardFilter = new SlewRateLimiter(Constants.Drivetrain.kForwardThrottleAccelFilter, -Constants.Drivetrain.kForwardThrottleDecelFilter, 0);
    private final SlewRateLimiter throttleBackwardFilter = new SlewRateLimiter(Constants.Drivetrain.kBackwardThrottleAccelFilter, -Constants.Drivetrain.kBackwardThrottleDecelFilter,0);

    private final SlewRateLimiter turnFilter = new SlewRateLimiter(Constants.Drivetrain.kTurnFilter);

    private double deadzone; 

    // TODO: refactor
    public enum Mode {
        NORMAL,
        SLOW
    }
    
    private Mode mode;

    public DriverController(int port) {
        this(port, 0.05); 
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
        double throttle = -getLeftStickY() * throttleMultiplier;
        double effThrottle = 0; 
        if (mode == Mode.SLOW) {
            effThrottle = throttle;
            throttleBackwardFilter.reset(0); 
            throttleForwardFilter.reset(0); 
        } else {
            if (lastEffThrottle > 0) {
                effThrottle = throttleForwardFilter.calculate(Math.max(throttle, 0)); 
                throttleBackwardFilter.reset(0);
            } else if (lastEffThrottle < 0) {
                effThrottle = -throttleBackwardFilter.calculate(-Math.min(throttle, 0)); 
                throttleForwardFilter.reset(0);
            } else {
                effThrottle = throttle > 0 ? throttleForwardFilter.calculate(throttle) : throttle < 0 ? -throttleBackwardFilter.calculate(-throttle) : 0; 
            }
        }
        
        // if (lastNonzeroThrottle != 0)
        lastEffThrottle = effThrottle; 
        return lastEffThrottle; 
    }

    public double getTurn() {
        if (turnMultiplier != Constants.Drivetrain.kTurnMultiplierSM) turnMultiplier = getQuickTurn() ? Constants.Drivetrain.kQuickTurnMultiplier : MathUtil.clamp(((Constants.Drivetrain.kFastThrottleTurnMultiplier - Constants.Drivetrain.kSlowThrottleTurnMultiplier) * (Math.abs(lastEffThrottle / throttleMultiplier) - 1) + Constants.Drivetrain.kFastThrottleTurnMultiplier), Constants.Drivetrain.kFastThrottleTurnMultiplier, Constants.Drivetrain.kSlowThrottleTurnMultiplier); 
        return turnFilter.calculate(-getRightStickX() * turnMultiplier); 
    }

    public boolean getQuickTurn() {
        boolean quickTurn = Math.abs(lastEffThrottle) < 0.05; 

        // if (turnMultiplier != Constants.Drivetrain.kTurnMultiplierSM) turnMultiplier = quickTurn ? Constants.Drivetrain.kQuickTurnMultiplier : Constants.Drivetrain.kTurnMultiplier;
        return quickTurn;
    }
    
    public void setSlowMode(Mode m) {
        if (m == Mode.NORMAL) {
            throttleMultiplier = Constants.Drivetrain.kThrottleMultiplier;
            turnMultiplier = Constants.Drivetrain.kQuickTurnMultiplier;
        } else {
            throttleMultiplier = Constants.Drivetrain.kThrottleMultiplierSM;
            turnMultiplier = Constants.Drivetrain.kTurnMultiplierSM;
        }
        this.mode = m; 
    }
    
    public Mode getSlowMode() {
        return this.mode; 
    }
}
