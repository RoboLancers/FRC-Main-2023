package frc.robot.team1678;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.team1678.PathWeaverTrajectory;
import frc.robot.subsystems.drivetrain.Drivetrain;


public class PathWeaverTest extends SequentialCommandGroup {
    public PathWeaverTest(Drivetrain drivetrain) {
        addCommands(
        new PathWeaverTrajectory().get_path_forward()
        );
    }
}
