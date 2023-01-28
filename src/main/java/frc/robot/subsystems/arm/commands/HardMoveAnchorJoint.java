package frc.robot.subsystems.arm.commands;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import java.time.chrono.IsoChronology;
import javax.sound.midi.Sequence;
import frc.robot.Constants;

import frc.robot.subsystems.arm.Arm;

public class HardMoveAnchorJoint extends CommandBase {
    public Arm arm;
    private RelativeEncoder encoder;

    public HardMoveAnchorJoint(Arm arm, RelativeEncoder encoder) {
        this.arm = arm;
        this.encoder = encoder;
    }

    @Override
    public void execute() {
        arm.setAnchorAngle(Constants.Arms.Miscellaneous.kContractedAnchorAngle + Constants.Arms.Miscellaneous.kCorrectedAnchorAngle);
    }

    @Override
    public void end(boolean interrupted){
        if (interrupted) {
            this.encoder.setPosition(0);
        }
    }

    @Override
    public boolean isFinished() {
        return return this.arm.limitSwitchTriggered();
    }
}
