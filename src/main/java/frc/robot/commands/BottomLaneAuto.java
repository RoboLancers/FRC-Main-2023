package frc.robot.commands;

import org.bananasamirite.robotmotionprofile.Waypoint;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.MaxVelocityConstraint;
import edu.wpi.first.math.trajectory.constraint.RectangularRegionConstraint;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.Constants.Intake.ScoreSpeed;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.MoveBackward;
import frc.robot.subsystems.drivetrain.commands.MoveForward;
import frc.robot.subsystems.drivetrain.commands.TurnBy;
import frc.robot.subsystems.drivetrain.commands.TurnToAngle;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.commands.IntakeElement;
import frc.robot.subsystems.intake.commands.IntakeFor;

public class BottomLaneAuto extends SequentialCommandGroup {
    public BottomLaneAuto(Drivetrain drivetrain, Arm arm, Intake intake, Constants.Arm.ScoringPosition position, Alliance alliance) {

        // SAFE AUTO (lame tho)
        // addCommands(
        //     new Score(arm, intake, position),
        //     new MoveBackward(drivetrain, 5.0)
        // );
        

        // IN DEV
        
        final double allianceMultiplier = alliance == Alliance.Red ? 1 : -1; 

        Waypoint startWaypoint; 
        Waypoint BEFORE_BUMP = new Waypoint(-3.0, -7.25 * allianceMultiplier, 0, 1, 1); 
        Waypoint PAST_BUMP = new Waypoint(-6.0, -7.25 * allianceMultiplier, 0, 1, 1); 
        // Waypoint BOTTOM_PIECE = new Waypoint(-5.0, -7.14 * allianceMultiplier, 0, 1, 1); 

        switch (position) {
            case HIGH_CUBE: 
            case MID_CUBE:
            case LOW_CUBE: 
                startWaypoint = new Waypoint(-1.86, -6.98 * allianceMultiplier, 0, 1, 1); 
                break;
            case HIGH_CONE: 
            case MID_CONE:
            case LOW_CONE:
            default: 
                startWaypoint = new Waypoint(-1.86, -7.66 * allianceMultiplier, 0, 1, 1); 
                break; 
        }

        addCommands(
            new InstantCommand(() -> {
                drivetrain.resetYaw();
            }), 
            new Score(arm, intake, position), 
            new WaitUntilCommand(drivetrain.gyro._gyro::isConnected),
            Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
                startWaypoint, 
                BEFORE_BUMP,
                PAST_BUMP
                // BOTTOM_PIECE
            }, new TrajectoryConfig(
                1, 
                1
                ).setReversed(true)
                // .addConstraint(
                //     new RectangularRegionConstraint(
                //         new Translation2d(-6.0, -20), 
                //         new Translation2d(-3.0, 20), 
                //         new MaxVelocityConstraint(1)
                //     )
                // )
            ),
            new ParallelRaceGroup(
                new WaitCommand(2), 
                new TurnToAngle(drivetrain, 180)
            ),
            new MoveToPos(arm, Constants.Arm.Position.GROUND),
            new ParallelRaceGroup(new MoveForward(drivetrain, 1), new IntakeFor(intake, ScoreSpeed.FAST, 69)), 
            new ParallelRaceGroup(
                new MoveToPos(arm, Constants.Arm.Position.CONTRACTED),
                new MoveBackward(drivetrain, 1)
            ),
            new ParallelRaceGroup(
                new WaitCommand(2), 
                new TurnToAngle(drivetrain, 0)
            ),
            Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
                PAST_BUMP, 
                BEFORE_BUMP,
                startWaypoint 
                // BOTTOM_PIECE
            }, new TrajectoryConfig(
                1, 
                1).setReversed(false)
            // .addConstraint(
            //     new RectangularRegionConstraint(
            //         new Translation2d(-6.0, -20), 
            //         new Translation2d(-3.0, 20), 
            //         new MaxVelocityConstraint(1)
            //         ))
            ),
            new Score(arm, intake, Constants.Arm.ScoringPosition.LOW_CUBE)

        );

    }
}
