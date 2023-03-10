package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


public class TeleopGRR extends SequentialCommandGroup {
    // public TeleopGRR(Drivetrain drivetrain, PoseTracker tracker, Arm arm) {
    //     super(
    //         // TODO: factor in the displacement, but do it properly
    //         new InstantiatorCommand(() -> new GridAlign(drivetrain, tracker)),
    //         // Fix the arm BS stuff
    //         // new MoveToPos(arm, 0, 0),
    //         new InstantCommand(() -> grabber.toggleDeploy(), grabber)
    //     );

    //     addRequirements(drivetrain, arm, grabber);
    // }

}
