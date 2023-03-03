package frc.robot.util;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;

public class ManipulatorController extends Controller {

    public Trigger intakeElementTrigger; 
    public Trigger outtakeElementTrigger; 
    public Trigger intakeOffTrigger; 

    public ManipulatorController(int port) {
        super(port); 
        this.intakeElementTrigger = new Trigger(() -> getController().getLeftTriggerAxis() > 0.2);
        this.outtakeElementTrigger = new Trigger(() -> getController().getLeftTriggerAxis() < -0.2);
        this.intakeOffTrigger = new Trigger(() -> getController().getLeftTriggerAxis() > -0.2 && getController().getLeftTriggerAxis() < 0.2); 
    }
}