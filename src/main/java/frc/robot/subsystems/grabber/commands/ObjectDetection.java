package frc.robot.subsystems.grabber.commands;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.grabber.Grabber;

public class ObjectDetection extends CommandBase {

    public Grabber grabber;
    public boolean getGrabberSensor;
    public AnalogPotentiometer potentiometer;
    public AnalogInput input;
    public boolean objectGrabbed;

    
    public ObjectDetection(DigitalInput grabberSensor, boolean getGrabberSensor) {
        this. input = new AnalogInput(Constants.Grabber.kChannel);
        input.setAverageBits(Constants.Grabber.kBits);
        this.potentiometer = new AnalogPotentiometer(input, Constants.Grabber.kMax, Constants.Grabber.kStart);
    }

    @Override
    public void execute() {
        if (getGrabberSensor) {
            grabber.toggleDeploy();
        }
        
        if (potentiometer.get() < Constants.Grabber.kMin) {
            objectGrabbed = false;
        } else {
            objectGrabbed = true;
        }
    }

}
