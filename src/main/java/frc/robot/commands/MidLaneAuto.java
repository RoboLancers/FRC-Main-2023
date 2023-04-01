package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
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
            new Score(arm, intake, position),
            new MoveBackward(drivetrain, 0.5), 
            new ParallelRaceGroup(
                new TurnBy(drivetrain, 180), 
                new WaitCommand(2)
            ), 
            new ParallelRaceGroup(
                new RunCommand(() -> {
                    drivetrain.arcadeDrive(0.3, 0); // 0.29
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
            new Balance(drivetrain, gyro, 0)  
        );




        /*
         * IN DEV: taxi during auto
         */

        // addCommands(
        //     new Score(arm, intake, position),
        //     new WaitCommand(0.5),
        //     new ParallelRaceGroup(
        //         new RunCommand(() -> {
        //             drivetrain.arcadeDrive(-0.3, 0);
        //         }, drivetrain),
        //         new WaitUntilCommand(
        //             () -> {
        //                 return Math.abs(gyro.getPitch()) > 12; 
        //             }
        //         )
        //     ),
        //     new ParallelRaceGroup(
        //         new RunCommand(() -> {
        //             drivetrain.arcadeDrive(-0.2, 0);
        //         }, drivetrain),
        //         new WaitUntilCommand(
        //             () -> {
        //                 return Math.abs(gyro.getPitch()) < 3; 
        //             }
        //         ).andThen(new WaitCommand(1.0))
        //     ),
        //     new ParallelRaceGroup(
        //         new RunCommand(() -> {
        //             drivetrain.arcadeDrive(0.3, 0);
        //         }, drivetrain),
        //         new WaitUntilCommand(
        //             () -> {
        //                 return Math.abs(gyro.getPitch()) > 12; 
        //             }
        //         ).andThen(new WaitCommand(1.0))
        //     ),
        //     new Balance(drivetrain, gyro, 0)
        // );
    }
}
