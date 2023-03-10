package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;

public class OuttakeElement extends ParallelRaceGroup {
    public OuttakeElement(Intake intake) {
        addCommands(new RunCommand(() -> {
            intake.outtakeFast();
        }), new WaitCommand(Constants.Intake.kAutoOuttakeSeconds));

        addRequirements(intake);
    }
    
}
