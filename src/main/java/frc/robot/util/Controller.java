package frc.robot.util;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;

public class Controller {
    private XboxController _controller;

    public Trigger A;
    public Trigger B;
    public Trigger X;
    public Trigger Y;
    public Trigger LeftBumper;
    public Trigger RightBumper;
    public Trigger LeftTrigger; 
    public Trigger RightTrigger; 

    public Controller(int port) {
        this._controller = new XboxController(port);

        this.A = new Trigger(this._controller::getAButton);
        this.B = new Trigger(this._controller::getBButton);
        this.X = new Trigger(this._controller::getXButton);
        this.Y = new Trigger(this._controller::getYButton);
        this.LeftBumper = new Trigger(this._controller::getLeftBumper);
        this.RightBumper = new Trigger(this._controller::getRightBumper);
        this.LeftTrigger = new Trigger(() -> this._controller.getLeftTriggerAxis() >= 0.5); 
        this.RightTrigger = new Trigger(() -> this._controller.getRightTriggerAxis() >= 0.5); 
    }
 
    private static void bindEvent(Consumer<Command> responder, Command response){
        responder.accept(response);
    }
    
    public static void onPress(Trigger capturer, Command response){
        Controller.bindEvent(capturer::whenActive, response);
    }

    public static void onHold(Trigger capturer, Command response){
        Controller.bindEvent(capturer::whileActiveContinuous, response);
    }

    public static void onRelease(Trigger capturer, Command response){
        Controller.bindEvent(capturer::whenInactive, response);
    }

    public static void onPressToggle(Trigger capturer, Command response){
        Controller.bindEvent(capturer::toggleWhenActive, response);
    }

    public static void onPressCancel(Trigger capturer, Command response){
        Controller.bindEvent(capturer::cancelWhenActive, response);
    }

    public double getLeftStickX() {
        return this._controller.getLeftX();
    }

    public double getLeftStickY() {
        return this._controller.getLeftY();
    }

    public double getRightStickX() {
        return this._controller.getRightX(); 
    }

    public double getRightStickY() {
        return this._controller.getRightY();
    }

    public double getLeftTrigger() {
        return this._controller.getLeftTriggerAxis();
    }

    public double getRightTrigger() {
        return this._controller.getRightTriggerAxis();
    }

    public int dPadAngle(){
        return this._controller.getPOV();
    }

    public void setRumble(boolean rumble){
        this._controller.setRumble(RumbleType.kBothRumble, rumble ? 1 : 0);
    }

    protected XboxController getController() {
        return this._controller; 
    }
}