package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class TriggerButton extends Trigger {
    private Joystick joystick;
    private int port;
    private boolean negative;

    private static final double kDeadzone = 0.5;

    TriggerButton(Joystick joystick, int port, boolean negative){
        this.joystick = joystick;
        this.port = port;
        this.negative = negative;
    }

    public boolean get() {
        return negative ? (ControllerUtils.applyDeadband(joystick.getRawAxis(port), kDeadzone) < 0) : (ControllerUtils.applyDeadband(joystick.getRawAxis(port), kDeadzone) > 0);
    }
}