package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.arm.Arm;
import java.beans.*;

public class RunToSetpoints extends CommandBase {
    private Arm arm;

    public RunToSetpoints(Arm arm){
        this.arm = arm;
    }

    @Override
    public void execute(){
        // custom pid control for anchor

        double maintainTerm = Constants.Arm.Anchor.kFF * Math.sin(arm.getAnchorAngle() * Math.PI / 180);
        double correctionTerm = Constants.Arm.Anchor.kP * (this.arm.anchorSetpoint - arm.getAnchorAngle());

        double output = maintainTerm + correctionTerm;

        arm.anchorMotor.set(output);

        // standard onboard pid control for floating

        arm.setFloatingAngle(arm.floatingSetpoint);
    }

    @Override
    public boolean isFinished(){
        return false;
    }

    @Override
    public void end(boolean interrupted){
        arm.anchorMotor.set(0);
        arm.floatingMotor.set(0);
    }
}