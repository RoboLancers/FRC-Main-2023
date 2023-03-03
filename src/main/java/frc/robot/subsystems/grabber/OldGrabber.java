package frc.robot.subsystems.grabber;

import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator.Validity;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Grabber extends SubsystemBase {
    public DoubleSolenoid grabberPiston;
    public boolean grabberClosed;
    private DigitalInput grabberSensor;
    public boolean objectDetected;

    public Grabber() {
        this.grabberPiston = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Grabber.kPistonDeploy, Constants.Grabber.kPistonRetract);
        grabberClosed = false;

        this.grabberSensor = new DigitalInput(Constants.Grabber.kGrabberSensor);
        objectDetected = false;
    } 

    @Override
    public void periodic() {
        SmartDashboard.putString("Grabber Deployed", grabberPiston.get().toString());
    }
    public void toggleDeploy() {
        if (grabberPiston.get() == Value.kOff || grabberPiston.get() == Value.kReverse) {
            grabberPiston.set(Value.kForward);
        } else {
            grabberPiston.set(Value.kReverse);
        }
    }

    public boolean isForward(){
        return grabberPiston.get() == Value.kForward;
    }

    public boolean isReversed(){
        return grabberPiston.get() == Value.kReverse;
    }

    public boolean isOff(){
        return grabberPiston.get() == Value.kOff;
    }

    public boolean getGrabberSensor() {
        // TODO: why is this flipped
        return !grabberSensor.get();
    }
}
