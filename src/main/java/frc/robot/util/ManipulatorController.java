package frc.robot.util;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ManipulatorController extends Controller {

    public Trigger intakeElementTriggerFast; 
    public Trigger outtakeElementTriggerFast;

    public Trigger intakeElementTriggerSlow; 
    public Trigger outtakeElementTriggerSlow;

    public Trigger dPadUp;
    public Trigger dPadDown;

    public ManipulatorController(int port) {
        super(port);
        
        this.outtakeElementTriggerFast = new Trigger(() -> -getRightStickY() > 0.2);
        this.intakeElementTriggerFast = new Trigger(() -> -getRightStickY() < -0.2);

        this.outtakeElementTriggerSlow = new Trigger(() -> -getLeftStickY() > 0.2);
        this.intakeElementTriggerSlow = new Trigger(() -> -getLeftStickY() < -0.2);

        this.dPadUp = new Trigger(() -> this.dPadAngle() == 0);
        this.dPadDown = new Trigger(() -> this.dPadAngle() == 180);
    }
}