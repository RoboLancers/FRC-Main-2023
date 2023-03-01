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
        double maintainTerm = Constants.Arm.Anchor.kFF * Math.sin(arm.getAnchorAngle() * Math.PI / 180);
        double correctionTerm = Constants.Arm.Anchor.kP * (arm.getAnchorAngle() - desiredAngle.getAsDouble());

        double output = maintainTerm + correctionTerm;

        SmartDashboard.putNumber("anchor-output", output);

        // arm.anchorMotor.set(output);
    }

    @Override
    public boolean isFinished() {
        return arm.isAnchorAtAngle(desiredAngle.getAsDouble());

        // return arm.isAnchorAtAngle(desiredAngle.getAsDouble()) && (arm.getAnchorMotorPower() < Constants.Arm.Miscellaneous.minOscillationThreshold);
    }
}
