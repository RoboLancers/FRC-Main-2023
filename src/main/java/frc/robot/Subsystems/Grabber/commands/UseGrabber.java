package frc.robot.Subsystems.Grabber.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Subsystems.Grabber.Grabber;

public class UseGrabber extends CommandBase {
    private Grabber grabber;

    public UseGrabber(Grabber grabber) {
        this.grabber = grabber;
        addRequirements(grabber);
    }

    @Override
    public void execute() {        
        if (grabber.grabberSensorBroken) {
            grabber.toggleDeploy(); 
        }
    }


    @Override
    public boolean isFinished() {
        return false;
    }
}
