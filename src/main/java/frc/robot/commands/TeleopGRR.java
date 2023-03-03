package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.grabber.Grabber;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;

import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.util.InstantiatorCommand;


public class TeleopGRR extends SequentialCommandGroup {
    public TeleopGRR(Drivetrain drivetrain, PoseTracker tracker, Arm arm, Grabber grabber) {
        super(
            // TODO: factor in the displacement, but do it properly
            new InstantiatorCommand(() -> new GridAlign(drivetrain, tracker)),
            // Fix the arm BS stuff
            new MoveToPos(arm, 0, 0),
            new InstantCommand(() -> grabber.toggleDeploy(), grabber)
        );

        addRequirements(drivetrain, arm, grabber);
    }

}
