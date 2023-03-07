package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import java.time.chrono.IsoChronology;
import java.util.function.DoubleSupplier;
import frc.robot.Constants;

import frc.robot.subsystems.arm.Arm;

public class MoveAnchorJoint extends CommandBase {
    public DoubleSupplier desiredAngle;
    public Arm arm;

    public MoveAnchorJoint(DoubleSupplier desiredAngle, Arm arm) {
        this.desiredAngle = desiredAngle;
        this.arm = arm;
        SmartDashboard.putBoolean("anchor stopped", false); 
    }

    @Override
    public void execute() {
        double maintainTerm = SmartDashboard.getNumber("anchorKFF", 0) * Math.sin(arm.getAnchorAngle() * Math.PI / 180);
        double correctionTerm = SmartDashboard.getNumber("anchorKP", 0) * -(arm.getAnchorAngle() - desiredAngle.getAsDouble());

        double output = maintainTerm + correctionTerm;

        SmartDashboard.putNumber("anchor-output", output);
        SmartDashboard.putNumber("error", -(arm.getAnchorAngle() - desiredAngle.getAsDouble())); 
        SmartDashboard.putNumber("anchor angle", arm.getAnchorAngle()); 

        arm.anchorMotor.set(output);
    }

    @Override
    public void end(boolean interrupted) {
        arm.anchorMotor.set(0);
    }

    @Override
    public boolean isFinished() {
        return arm.isAnchorAtAngle(desiredAngle.getAsDouble());

        // return arm.isAnchorAtAngle(desiredAngle.getAsDouble()) && (arm.getAnchorMotorPower() < Constants.Arm.Miscellaneous.minOscillationThreshold);
    }
}
