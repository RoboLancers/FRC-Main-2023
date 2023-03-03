package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;

public class IntakeElement extends SequentialCommandGroup {
    public IntakeElement(Intake intake) {
        addCommands(new InstantCommand(() -> {
            intake.intake();
        }), new WaitCommand(Constants.Intake.kAutoIntakeSeconds), new InstantCommand(() -> {
            intake.off();
        }));

        addRequirements(intake);
    }
    
}
