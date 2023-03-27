package frc.robot.team254;

import edu.wpi.first.math.trajectory.TrajectoryParameterizer.TrajectoryGenerationException;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.team254.TrajectoryGenerator;
import frc.robot.util.ManipulatorController;
import frc.robot.util.enums.Displacement;

public class MotionPlannerTest extends SequentialCommandGroup {
    public MotionPlannerTest(Drivetrain drivetrain, TrajectoryGenerator trajectoryGenerator){
        addCommands(
           trajectoryGenerator.getStraightTest()
           //blah blah blah intake and stuff
        );
        addRequirements(drivetrain);
    }
}
