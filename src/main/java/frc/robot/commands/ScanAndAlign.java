package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.util.ManipulatorController;
import frc.robot.util.enums.Displacement;

public class ScanAndAlign extends SequentialCommandGroup {
    public ScanAndAlign(Drivetrain drivetrain, Arm arm, PoseTracker poseTracker, ManipulatorController controller){
        addCommands(
            new InstantCommand(() -> {
                poseTracker.displacement = controller.dPadAngle() == 270 ? Displacement.kLeft : controller.dPadAngle() == 90 ? Displacement.kRight : Displacement.kCenter;
            }),
            new MoveToPos(arm, Constants.Arm.Positions.Ground.kAnchor, Constants.Arm.Positions.Ground.kFloating),
            new WaitCommand(0.3),
            new GridAlign(drivetrain, poseTracker)
        );

        addRequirements(drivetrain);
    }
}
