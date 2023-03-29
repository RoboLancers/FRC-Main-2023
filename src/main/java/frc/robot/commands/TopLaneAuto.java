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
import frc.robot.subsystems.intake.commands.IntakeFor;

public class TopLaneAuto extends SequentialCommandGroup {

    public TopLaneAuto(Drivetrain drivetrain, Arm arm, Gyro gyro, Intake intake, Constants.Arm.ScoringPosition scoreFirst, Constants.Arm.ScoringPosition scoreSecond) {
        this(drivetrain, arm, gyro, intake, scoreFirst, scoreSecond, true); 
    }

    public TopLaneAuto(Drivetrain drivetrain, Arm arm, Gyro gyro, Intake intake, Constants.Arm.ScoringPosition scoreFirst, Constants.Arm.ScoringPosition scoreSecond, boolean secondTop) {

        final double allianceMultiplier = DriverStation.getAlliance() == Alliance.Red ? 1 : -1; 

        Waypoint startWaypoint; 
        Waypoint OUT_FIELD = new Waypoint(-4.28, -3.31 * allianceMultiplier, 0, 1, 0.5); 
        Waypoint PRE_TOP_PIECE = new Waypoint(-7.03, -2.55 * allianceMultiplier, Math.toRadians(-90 * allianceMultiplier), 0.5, 1); 
        Waypoint TOP_PIECE = new Waypoint(-7.05, -3 * allianceMultiplier, Math.toRadians(-90 * allianceMultiplier), 0.6, 1); 
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
            //     gyro.zeroYaw();
            // }), 
            new Score(arm, intake, scoreFirst),
            new ParallelCommandGroup(
                Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
                    startWaypoint, 
                    OUT_FIELD, 
                    PRE_TOP_PIECE, 
                }, new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared).setReversed(true)),
                new MoveToPos(arm, Constants.Arm.Position.GROUND)
            ),
            new ParallelCommandGroup(
                Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
                    PRE_TOP_PIECE, 
                    TOP_PIECE, 
                    OUT_FIELD, 
                    endWaypoint
                }, new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared).setReversed(false)),
                new SequentialCommandGroup(
                    new IntakeFor(intake, ScoreSpeed.FAST, 2), 
                    new MoveToPos(arm, Constants.Arm.Position.CONTRACTED)
                )
            )
        );
        // addCommands(new Score(arm, intake, scoreFirst), new MoveBackward(drivetrain, 4));
    }
}