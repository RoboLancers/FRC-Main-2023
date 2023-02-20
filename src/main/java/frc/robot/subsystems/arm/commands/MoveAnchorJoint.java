package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import java.time.chrono.IsoChronology;

import frc.robot.subsystems.arm.Arm;

public class MoveAnchorJoint extends CommandBase{
    public double desiredAngle;
    public Arm arm;

    public MoveAnchorJoint(double desiredAngle, Arm arm) {
        this.desiredAngle = desiredAngle;
        this.arm = arm;

    }

    @Override
    public void execute() {
        arm.setAnchorAngle(desiredAngle);
    }

    @Override
    public boolean isFinished() {
        return arm.isAnchorAtAngle(desiredAngle);
    }
}
