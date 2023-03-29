package frc.robot.commands;

import org.bananasamirite.robotmotionprofile.Waypoint;

import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.Constants.Intake.ScoreSpeed;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.MoveBackward;
import frc.robot.subsystems.drivetrain.commands.MoveForward;
import frc.robot.subsystems.drivetrain.commands.TurnBy;
import frc.robot.subsystems.drivetrain.commands.TurnToAngle;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.commands.IntakeElement;

public class TopLaneAuto extends SequentialCommandGroup {

    public TopLaneAuto(Drivetrain drivetrain, Arm arm, Gyro gyro, Intake intake, Constants.Arm.ScoringPosition scoreFirst, Constants.Arm.ScoringPosition scoreSecond) {
        this(drivetrain, arm, gyro, intake, scoreFirst, scoreSecond, true); 
    }

    public TopLaneAuto(Drivetrain drivetrain, Arm arm, Gyro gyro, Intake intake, Constants.Arm.ScoringPosition scoreFirst, Constants.Arm.ScoringPosition scoreSecond, boolean secondTop) {

        final double allianceMultiplier = DriverStation.getAlliance() == Alliance.Red ? 1 : -1; 

        Waypoint startWaypoint; 
        Waypoint TOP_PIECE = new Waypoint(-6.2, -3.44 * allianceMultiplier, 0, 1, 1); 
        Waypoint ALIGN_POINT = new Waypoint(-2.54, -3.36 * allianceMultiplier, 0, 1, 1); 
        Waypoint endWaypoint; 



        switch (scoreFirst) {
            case HIGH_CUBE: 
            case MID_CUBE:
            case LOW_CUBE: 
                startWaypoint = new Waypoint(-1.86, -3.64 * allianceMultiplier, 0, 1, 1); 
                break;
            case HIGH_CONE: 
            case MID_CONE:
            case LOW_CONE:
            default: 
                startWaypoint = new Waypoint(-1.86, -3.06 * allianceMultiplier, 0, 1, 1); 
                break; 
        }

        switch (scoreSecond) {
            case HIGH_CUBE: 
            case MID_CUBE:
            case LOW_CUBE: 
                endWaypoint = new Waypoint(-1.86, -3.64 * allianceMultiplier, 0, 1, 1); 
                break;
            case HIGH_CONE: 
            case MID_CONE:
            case LOW_CONE:
            default:
                endWaypoint = new Waypoint(-1.86, -3.06 * allianceMultiplier, 0, 1, 1); 
                break; 
        }

        addCommands(
            // new InstantCommand(() -> {
            //     gyro.reset();
            // }), 
            new Score(arm, intake, scoreFirst), 
        Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
            startWaypoint, 
            TOP_PIECE
        }, new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared).setReversed(true)),
        // new ParallelRaceGroup(
            new TurnToAngle(drivetrain, 180), 
            // new WaitCommand(2)
        // ), 
        new MoveToPos(arm, Constants.Arm.Position.GROUND),
        new ParallelCommandGroup(new MoveForward(drivetrain, 0.5), new IntakeElement(intake, ScoreSpeed.FAST)), 
        new ParallelCommandGroup(
            new MoveToPos(arm, Constants.Arm.Position.CONTRACTED),
            new MoveBackward(drivetrain, 0.5)
        ),
        // new ParallelRaceGroup(
            new TurnToAngle(drivetrain, 0), 
            // new WaitCommand(2)
        // ),
        Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
            TOP_PIECE, 
            // ALIGN_POINT, 
            endWaypoint
        }, new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared).setReversed(false)), 
        new Score(arm, intake, scoreSecond)
        );
        // addCommands(new Score(arm, intake, scoreFirst), new MoveBackward(drivetrain, 4));
    }
}