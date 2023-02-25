package frc.robot.util;

import java.util.function.Consumer;

import org.opencv.imgproc.GeneralizedHoughBallard;

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

    private double deadzone;
    double throttleMultiplier = Constants.Drivetrain.kThrottleMultiplier;
    double turnMultiplier = Constants.Drivetrain.kTurnMultiplier;

    public enum Mode {
        NORMAL,
        SLOW
        }
    Mode mode = Mode.NORMAL;

    public Controller(int port) {
        this(port, 0.15); 
    }

    public Controller(int port, double deadzone){
        this._controller = new XboxController(port);

        this.A = new Trigger(this._controller::getAButton);
        this.B = new Trigger(this._controller::getBButton);
        this.X = new Trigger(this._controller::getXButton);
        this.Y = new Trigger(this._controller::getYButton);
        this.LeftBumper = new Trigger(this._controller::getLeftBumper);
        this.RightBumper = new Trigger(this._controller::getRightBumper);
        this.deadzone = deadzone; 
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

    // TODO: may be bad to do it this way
    public double getLeftStickX(){
        return ControllerUtils.applyDeadband(this._controller.getLeftX(), deadzone);
    }

    public double getLeftStickY(){
        return ControllerUtils.applyDeadband(this._controller.getLeftY(), deadzone);
    }

    public double getRightStickX(){
        return ControllerUtils.applyDeadband(this._controller.getRightX(), deadzone); 
    }

    public double getRightStickY(){
        return ControllerUtils.applyDeadband(this._controller.getRightY(), deadzone);
    }

    public double getLeftTrigger(){
        return this._controller.getLeftTriggerAxis();
    }

    public double getRightTrigger(){
        return this._controller.getRightTriggerAxis();
    }

    public int dPadAngle(){
        return this._controller.getPOV();
    }

    public void setRumble(boolean rumble){
        this._controller.setRumble(RumbleType.kBothRumble, rumble ? 1 : 0);
    }

    public double getThrottle(){
        return -getLeftStickY() * throttleMultiplier;
    }

    public double getTurn(){
        return -getRightStickX() * turnMultiplier;
    }

    public boolean getQuickTurn(){
        return Math.abs(-getLeftStickY()) < 0.05;
    }
    public void toggleSlowMode(){
        switch(mode){
            case NORMAL:
            throttleMultiplier = Constants.Drivetrain.kThrottleMultiplierSM;
            turnMultiplier = Constants.Drivetrain.kTurnMultiplierSM;
            mode = Mode.SLOW;
            break;

            case SLOW:
            throttleMultiplier = Constants.Drivetrain.kThrottleMultiplier;
            turnMultiplier = Constants.Drivetrain.kTurnMultiplier;
            mode = Mode.NORMAL;
            break;
        }
    }
}