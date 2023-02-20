package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.arm.Arm;

public class RollUntilSwitch extends CommandBase {
    public Arm arm;
    
    public RollUntilSwitch(Arm arm) {
        this.arm = arm;
    }

    @Override
    public void execute() {
        arm.anchorMotor.set(Constants.Arm.Misc.kLowPower);
    }

    @Override
    public void end(boolean interrupted){
        arm.anchorMotor.set(0);
        // TODO: wouldn't we set this to contracted position not 0?
        arm.anchorEncoder.setPosition(0);
    }

    @Override
    public boolean isFinished() {
        return true;

        // TODO: make arm method to read limit switch directly
        // return this.arm.anchorLimitSwitchTriggered;
    }
}
