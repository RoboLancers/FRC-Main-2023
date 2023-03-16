package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.subsystems.arm.Arm;

public class Zero extends SequentialCommandGroup {
    public Zero(Arm arm){
        addCommands(
            new InstantCommand(() -> arm.toggleSoftLimits(false)),
            new ParallelRaceGroup(
                new RunCommand(() -> arm.anchorMotor.set(-0.05)),
                new WaitUntilCommand(arm.anchorLimitSwitch::get)
            ),
            new InstantCommand(() -> {
                arm.toggleSoftLimits(true);
                // arm.zeroEncoders();
                arm.anchorMotor.set(0);
            })
        );

        addRequirements(arm);
    }
}