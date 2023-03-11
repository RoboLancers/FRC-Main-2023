package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.MoveBackward;
import frc.robot.subsystems.intake.Intake;

public class TopLaneAuto extends SequentialCommandGroup {
    public TopLaneAuto(Drivetrain drivetrain, Arm arm, Intake intake, Constants.Arm.ScoringPosition position) {
        addCommands(new Score(arm, intake, position), new MoveBackward(drivetrain, 3));
    }
}