package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.MoveBackward;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.gyro.commands.Balance;
import frc.robot.subsystems.intake.Intake;

public class MidLaneAuto extends SequentialCommandGroup {
    public MidLaneAuto(Drivetrain drivetrain, Gyro gyro, Arm arm, Intake intake, Constants.Arm.ScoringPosition position) {
        addCommands(new Score(arm, intake, position), new MoveBackward(drivetrain, 3), new Balance(drivetrain, gyro, 0));
    }
}
