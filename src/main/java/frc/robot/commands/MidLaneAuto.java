package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.MoveBackward; 
import frc.robot.subsystems.drivetrain.commands.TurnBy;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.gyro.commands.Balance;
import frc.robot.subsystems.intake.Intake;

public class MidLaneAuto extends SequentialCommandGroup {
    public MidLaneAuto(Drivetrain drivetrain, Gyro gyro, Arm arm, Intake intake, Constants.Arm.ScoringPosition position) {
        addCommands(
            // new Score(arm, intake, position)
            
            new Score(arm, intake, position),
            // TODO: tune this first such that it goes over charging, then find how much to go back
            new MoveBackward(drivetrain, 0.5), 
            new ParallelRaceGroup(
                new TurnBy(drivetrain, 180), 
                new WaitCommand(2)
            ), 
            new ParallelRaceGroup(
                new RunCommand(() -> {
                    drivetrain.arcadeDrive(0.29, 0);
                }, drivetrain), 
                new ParallelRaceGroup(
                    new WaitUntilCommand(
                        () -> {
                            return Math.abs(gyro.getPitch()) > 12; 
                        }
                    ).andThen(new WaitCommand(1.0)), 
                    new WaitCommand(2.5)
                )
            ),
            // new MoveBackward(drivetrain, 4) // <- this
            // new MoveForward(drivetrain, 2),
            new Balance(drivetrain, gyro, 0)  
        );
    }
}
