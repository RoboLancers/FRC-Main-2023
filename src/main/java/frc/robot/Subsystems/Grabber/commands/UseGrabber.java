package frc.robot.subsystems.Grabber.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Grabber.Grabber;

public class UseGrabber extends CommandBase {
    private Grabber grabber;

    public UseGrabber(Grabber grabber) {
        this.grabber = grabber;
        addRequirements(grabber);
    }

    @Override
    public void execute() {        
        if (grabber.SensorTriggered()) {
            grabber.toggleDeploy(); 
        }
    }


    @Override
    public boolean isFinished() {
        return false;
    }
}
