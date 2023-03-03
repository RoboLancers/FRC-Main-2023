package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;

public class OuttakeElement extends SequentialCommandGroup {
    public OuttakeElement(Intake intake) {
        addCommands(new InstantCommand(() -> {
            intake.outtake();
        }), new WaitCommand(Constants.Intake.kAutoOuttakeSeconds), new InstantCommand(() -> {
            intake.off();
        }));

        addRequirements(intake);
    }
    
}
