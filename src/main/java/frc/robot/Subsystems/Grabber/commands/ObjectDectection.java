package frc.robot.Subsystems.Grabber.commands;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Subsystems.Grabber.Grabber;

public class ObjectDectection extends CommandBase {

    public Grabber grabber;
    public boolean getGrabberSensor;
    public AnalogPotentiometer potentiometer;
    public AnalogInput input;
    public boolean objectGrabbed;

    
    public ObjectDectection(DigitalInput grabberSensor, boolean getGrabberSensor) {
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
