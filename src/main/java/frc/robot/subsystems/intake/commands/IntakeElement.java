package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;

public class IntakeElement extends ParallelRaceGroup {
    public IntakeElement(Intake intake) {
        addCommands(new RunCommand(() -> {
            intake.intakeFast();
        }), new WaitCommand(Constants.Intake.kAutoIntakeSeconds));

        addRequirements(intake);
    }
    
}
