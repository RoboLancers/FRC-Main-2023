package frc.robot.subsystems.Grabber;

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
        SmartDashboard.putString("solenoid state", grabberPiston.get().toString());
    }
    public void toggleDeploy() {
        grabberPiston.toggle();
    }

    public Value getPosition() {
        return grabberPiston.get();
    }

    public boolean getGrabberSensor() {
        return !grabberSensor.get();
    }

}
