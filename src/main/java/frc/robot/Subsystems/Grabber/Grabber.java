package frc.robot.Subsystems.Grabber;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Subsystems.Grabber.commands.UseGrabber;


public class Grabber extends SubsystemBase {
    public DoubleSolenoid grabberPiston;
    public boolean grabberClosed = false;
    private DigitalInput grabberSensor;
    public boolean getGrabberSensor; 


    public Grabber() {
        this.grabberPiston = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Grabber.kPistonDeploy, Constants.Grabber.kPistonRetract);
        grabberPiston.set(Value.kReverse);
        this.grabberSensor = new DigitalInput(Constants.Grabber.kGrabberSensor);
        getGrabberSensor = !grabberSensor.get();

    } 

    public void toggleDeploy() {
        grabberPiston.toggle();
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Intake Sensor", getGrabberSensor);

    }

    private void initDefaultCommand(){
        setDefaultCommand(new UseGrabber(this));
    }


    public Value isGrabberClosed() {
        return (grabberPiston.get());
    }

}
