package frc.robot.util;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ManipulatorController extends Controller {

    public Trigger intakeElementTrigger; 
    public Trigger outtakeElementTrigger; 
    public Trigger intakeOffTrigger; 

    public ManipulatorController(int port) {
        super(port);
        
        this.intakeElementTrigger = new Trigger(() -> -getLeftStickY() > 0.2);
        this.outtakeElementTrigger = new Trigger(() -> -getLeftStickY() < -0.2);
        this.intakeOffTrigger = new Trigger(() -> -getLeftStickY() > -0.2 && -getLeftStickY() < 0.2); 
    }
}