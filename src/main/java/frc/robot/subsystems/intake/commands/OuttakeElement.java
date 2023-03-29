package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.Constants.Intake.ScoreSpeed;
import frc.robot.subsystems.intake.Intake;

public class OuttakeElement extends OuttakeFor {
    public OuttakeElement(Intake intake, ScoreSpeed speed) {
        super(intake, speed, Constants.Intake.kAutoOuttakeSeconds); 
    }
    
}
