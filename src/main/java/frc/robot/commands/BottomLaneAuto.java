package frc.robot.commands;

import org.bananasamirite.robotmotionprofile.Waypoint;

import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Constants.Intake.ScoreSpeed;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.MoveForward;
import frc.robot.subsystems.drivetrain.commands.TurnBy;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.commands.IntakeElement;

public class BottomLaneAuto extends SequentialCommandGroup {
    public BottomLaneAuto(Drivetrain drivetrain, Arm arm, Intake intake, Constants.Arm.ScoringPosition scoreFirst) {
        // double BOTTOM_TURN_ANGLE_RED = 30; 
        // double allianceMultiplier = DriverStation.getAlliance() == Alliance.Red ? 1 : -1; 
        // switch(position) {
        //     case HIGH_CUBE: 
        //     case MID_CUBE:
        //     case LOW_CUBE: 
        //         addCommands(
        //             new Score(arm, intake, position), 
        //             new MoveBackward(drivetrain, 5)
        //         ); 
        //         // addCommands(
        //         //     new Score(arm, intake, position), 
        //         //     new MoveBackward(drivetrain, 0.5), 
        //         //     new TurnBy(drivetrain, BOTTOM_TURN_ANGLE_RED * allianceMultiplier), 
        //         //     new MoveBackward(drivetrain, 0.5), 
        //         //     new TurnBy(drivetrain, -BOTTOM_TURN_ANGLE_RED * allianceMultiplier), 
        //         //     new MoveBackward(drivetrain, 4)
        //         // );
        //         break;
        //     case HIGH_CONE: 
        //     case MID_CONE:
        //     case LOW_CONE:
        //         addCommands(
        //             new Score(arm, intake, position), 
        //             new MoveBackward(drivetrain, 5)
        //         ); 
        //         break; 
        // }
        // addCommands(
        //     new Score(arm, intake, position), 
        //     new MoveBackward(drivetrain, 5), 
        //     new TurnBy(drivetrain, 180)

        // ); 
        
        final double allianceMultiplier = DriverStation.getAlliance() == Alliance.Red ? 1 : -1; 

        Waypoint startWaypoint; 
        Waypoint BOTTOM_PIECE = new Waypoint(-5.9, -7.14 * allianceMultiplier, 0, 1, 1); 



        switch (scoreFirst) {
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

        addCommands(new Score(arm, intake, scoreFirst), 
        Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
            startWaypoint, 
            BOTTOM_PIECE
        }, new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared).setReversed(true)),
        new TurnBy(drivetrain, 180), 
        new MoveToPos(arm, Constants.Arm.Position.GROUND),
        new ParallelCommandGroup(new MoveForward(drivetrain, 0.5), new IntakeElement(intake, ScoreSpeed.FAST))
        );

    }
}
