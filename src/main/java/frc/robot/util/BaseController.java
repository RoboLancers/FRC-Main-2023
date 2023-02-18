package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.POVButton;

abstract class BaseController {
    double deadzone = 0;

    Joystick joystick;
    Button[] buttons;
    TriggerButton[] triggerButtons;
    POVButton[] povButtons;


    BaseController(int port) {
        joystick = new Joystick(port);
    }
}