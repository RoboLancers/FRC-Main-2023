package frc.robot.subsystems.arm.commands;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.MotorType; 
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class MoveFloatingJoint extends CommandBase {
    public double desiredAngle;
    public Arm arm;

    public MoveFloatingJoint(double desiredAngle, Arm arm) {
        this.desiredAngle = desiredAngle;
        this.arm = arm;
    }

    @Override
    public void execute() {
        arm.setFloatingAngle(desiredAngle);
    }

    @Override
    public boolean isFinished() {
        return arm.isFloatingAtAngle(desiredAngle);
    }
}
