package frc.robot.util;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

@SuppressWarnings({"unused", "WeakerAccess"})
public class XboxController extends BaseController {

    public enum Axis {
        LEFT_X(0),
        LEFT_Y(1, true),
        LEFT_TRIGGER(2),
        RIGHT_TRIGGER(3),
        RIGHT_X(4),
        RIGHT_Y(5, true);

        int port;
        int inverted;

        Axis(int port){
            this(port, false);
        }

        Axis(int port, boolean inverted){
            this.port = port;
            this.inverted = inverted ? -1 : 1;
        }
    }

    public enum Button {
        A(1),
        B(2),
        X(3),
        Y(4),
        LEFT_BUMPER(5),
        RIGHT_BUMPER(6),
        SELECT(7),
        START(8),
        LEFT_JOYSTICK_BUTTON(9),
        RIGHT_JOYSTICK_BUTTON(10);

        int port;

        Button(int port){
            this.port = port;
        }
    }

    public enum POV {
        UP(0,0),
        RIGHT(1,90),
        DOWN(2,180),
        LEFT(3,270),
        
        UPPER_RIGHT(4,45),
        LOWER_RIGHT(5,135),
        LOWER_LEFT(6,225),
        UPPER_LEFT(7,315);

        int index;
        int angle;

        POV(int index, int angle){
            this.index = index;
            this.angle = angle;
        }
    }

    public enum Trigger {
        //joysticks
        LEFT_X(Axis.LEFT_X.port),
        LEFT_Y(Axis.LEFT_Y.port),
        LEFT_TRIGGER(Axis.LEFT_TRIGGER.port),
        RIGHT_TRIGGER(Axis.RIGHT_TRIGGER.port),
        RIGHT_X(Axis.RIGHT_X.port),
        RIGHT_Y(Axis.RIGHT_Y.port);

        int port;
        boolean negative;

        Trigger(int port){
            this(port, false);
        }

        Trigger(int port, boolean negative){
            this.port = port;
            this.negative = negative;
        }
    }

    public XboxController(int port) {
        this(port, 0.15);
    }

    public XboxController(int port, double deadzone) {
        super(port);

        buttons = new JoystickButton[Button.values().length + 1];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JoystickButton(joystick, i);
        }

        triggerButtons = new TriggerButton[Axis.values().length];

        for(int i = 0; i < triggerButtons.length; i++){
            triggerButtons[i] = new TriggerButton(joystick, i, Trigger.values()[i].negative);
        }

        povButtons = new POVButton[POV.values().length];

        for(int i = 0; i < povButtons.length; i++){
            povButtons[i] = new POVButton(joystick, POV.values()[i].angle);
        }

        this.deadzone = deadzone;
    }

    public boolean getState(Button buttons){
        return joystick.getRawButton(buttons.port);
    }

    public boolean getState(Trigger trigger){
        return triggerButtons[trigger.port].getAsBoolean(); 
    }

    public boolean getState(POV pov){
        return joystick.getPOV() == pov.angle;
    }

    public XboxController whileHeld(Trigger trigger, Command command){
        triggerButtons[trigger.port].whileActiveContinuous(command);
        return this;
    }

    public XboxController whileHeld(Button button, Command command){
        buttons[button.port].whileHeld(command);
        return this;
    }

    public XboxController whileHeld(POV pov, Command command){
        povButtons[pov.index].whileHeld(command);
        return this;
    }

    public XboxController whenPressed(Trigger trigger, Command command){
        triggerButtons[trigger.port].whenActive(command);
        return this;
    }

    public XboxController whenPressed(Button button, Command command){
        buttons[button.port].whenPressed(command);
        return this;
    }

    public XboxController whenPressed(POV pov, Command command){
        povButtons[pov.index].whenPressed(command);
        return this;
    }

    public XboxController whenReleased(Trigger trigger, Command command){
        triggerButtons[trigger.port].whenInactive(command);
        return this;
    }

    public XboxController whenReleased(Button button, Command command){
        buttons[button.port].whenReleased(command);
        return this;
    }

    public XboxController whenReleased(POV pov, Command command){
        povButtons[pov.index].whenReleased(command);
        return this;
    }

    public XboxController toggleWhenPressed(Trigger trigger, Command command){
        triggerButtons[trigger.port].toggleWhenActive(command);
        return this;
    }

    public XboxController toggleWhenPressed(Button button, Command command){
        buttons[button.port].toggleWhenPressed(command);
        return this;
    }

    public XboxController toggleWhenPressed(POV pov, Command command){
        povButtons[pov.index].toggleWhenPressed(command);
        return this;
    }

    public XboxController cancelWhenPressed(Trigger trigger, Command command){
        triggerButtons[trigger.port].cancelWhenActive(command);
        return this;
    }

    public XboxController cancelWhenPressed(Button button, Command command){
        buttons[button.port].cancelWhenPressed(command);
        return this;
    }

    public XboxController cancelWhenPressed(POV pov, Command command){
        povButtons[pov.index].cancelWhenPressed(command);
        return this;
    }

    public void setRumble(boolean rumble){
        joystick.setRumble(GenericHID.RumbleType.kLeftRumble, rumble ? 1 : 0);
        joystick.setRumble(GenericHID.RumbleType.kRightRumble, rumble ? 1 : 0);
    }

    public XboxController toggleWhenBothPressed(Button button1, Button button2, Command command) {
        if (getState(button1) && getState(button2)) {
            buttons[button1.port].toggleWhenPressed(command);
        }
        return this;
    }

    public XboxController toggleWhenBothPressed(Trigger trigger1, Trigger trigger2, Command command) {
        if (getState(trigger1) && getState(trigger2)) {
            triggerButtons[trigger1.port].toggleWhenActive(command);
        }
        return this;
    }

    public XboxController toggleWhenBothPressed(POV pov1, POV pov2, Command command) {
        if (getState(pov1) && getState(pov2)) {
            povButtons[pov1.index].toggleWhenPressed(command);
        }
        return this;
    }

    public XboxController toggleWhenBothPressed(Button button, Trigger trigger, Command command) {
        if (getState(button) && getState(trigger)) {
            buttons[button.port].toggleWhenPressed(command);
        }
        return this;
    }

    public XboxController toggleWhenBothPressed(Trigger trigger, POV pov, Command command) {
        if (getState(trigger) && getState(pov)) {
            triggerButtons[trigger.port].toggleWhenActive(command);
        }
        return this;
    }

    public XboxController toggleWhenBothPressed(POV pov, Button button, Command command) {
        if (getState(pov) && getState(button)) {
            povButtons[pov.index].toggleWhenPressed(command);
        }
        return this;
    }

    public XboxController toggleWhenBothPressed(Trigger trigger, Button button, Command command) {
        if (getState(button) && getState(trigger)) {
            buttons[button.port].toggleWhenPressed(command);
        }
        return this;
    }

    public XboxController toggleWhenBothPressed(POV pov, Trigger trigger, Command command) {
        if (getState(trigger) && getState(pov)) {
            triggerButtons[trigger.port].toggleWhenActive(command);
        }
        return this;
    }

    public XboxController toggleWhenBothPressed(Button button, POV pov, Command command) {
        if (getState(pov) && getState(button)) {
            povButtons[pov.index].toggleWhenPressed(command);
        }
        return this;
    }

    public XboxController whenBothPressed(Button button1, Button button2, Command command) {
        if (getState(button1) && getState(button2)) {
            buttons[button1.port].whenPressed(command);
        }
        return this;
    }

    public XboxController whenBothPressed(Trigger trigger1, Trigger trigger2, Command command) {
        if (getState(trigger1) && getState(trigger2)) {
            triggerButtons[trigger1.port].whenActive(command);
        }
        return this;
    }

    public XboxController whenBothPressed(POV pov1, POV pov2, Command command) {
        if (getState(pov1) && getState(pov2)) {
            povButtons[pov1.index].whenPressed(command);
        }
        return this;
    }

    public XboxController whenBothPressed(Button button, Trigger trigger, Command command) {
        if (getState(button) && getState(trigger)) {
            buttons[button.port].whenPressed(command);
        }
        return this;
    }

    public XboxController whenBothPressed(Trigger trigger, POV pov, Command command) {
        if (getState(trigger) && getState(pov)) {
            triggerButtons[trigger.port].whenActive(command);
        }
        return this;
    }

    public XboxController whenBothPressed(POV pov, Button button, Command command) {
        if (getState(pov) && getState(button)) {
            povButtons[pov.index].whenPressed(command);
        }
        return this;
    }

    public XboxController whenBothPressed(Trigger trigger, Button button, Command command) {
        if (getState(button) && getState(trigger)) {
            buttons[button.port].whenPressed(command);
        }
        return this;
    }

    public XboxController whenBothPressed(POV pov, Trigger trigger, Command command) {
        if (getState(trigger) && getState(pov)) {
            triggerButtons[trigger.port].whenActive(command);
        }
        return this;
    }

    public XboxController whenBothPressed(Button button, POV pov, Command command) {
        if (getState(pov) && getState(button)) {
            povButtons[pov.index].whenPressed(command);
        }
        return this;
    }
}