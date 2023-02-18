package frc.robot.subsystems.arm.commands;

import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.arm.Arm;

public class RollUntilSwitch extends CommandBase{
    public Arm arm;
    public RelativeEncoder encoder;
    
    public RollUntilSwitch(Arm arm, RelativeEncoder encoder) {
        this.arm = arm;
        this.encoder = encoder;
    }

    @Override
    public void execute() {
        arm.anchorJointMotor.set(Constants.Arm.Miscellaneous.kLowPower);
    }

    @Override
    public void end(boolean interrupted){
        if (interrupted) {
            arm.anchorJointMotor.set(0);
            this.encoder.setPosition(0);
        }
    }

    @Override
    public boolean isFinished() {
        return this.arm.anchorLimitSwitchTriggered;
    }


}
