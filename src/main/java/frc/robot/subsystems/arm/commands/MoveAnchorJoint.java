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
        arm.setAnchorAngle(desiredAngle.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return arm.isAnchorAtAngle(desiredAngle.getAsDouble());

        // return arm.isAnchorAtAngle(desiredAngle.getAsDouble()) && (arm.getAnchorMotorPower() < Constants.Arm.Miscellaneous.minOscillationThreshold);
    }
}
