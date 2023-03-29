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

        //reversed forward 
        Waypoint START_BETWEEN_TOP = new Waypoint(-4.33, -3.06 * allianceMultiplier,0, 1, 1);

        //reversed forward and right -90 heading
        Waypoint SIDE_TOP_PIECE = new Waypoint(-6.7, -2.44 * allianceMultiplier, -90, 1, 1);

        //forward
        Waypoint INTAKE_POINT = new Waypoint(-6.9, -2.44 * allianceMultiplier, 0, 1, 1);

        //forward and left +90 heading
        Waypoint INTAKE_BETWEEN_END = new Waypoint(-7.1, -1.44 * allianceMultiplier, 90, 1, 1);
        
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
                endWaypoint = new Waypoint(-2.26, -1.56 * allianceMultiplier, 0, 1, 1); 
                break;
            case HIGH_CONE: 
            case MID_CONE:
            case LOW_CONE:
            default: 
                endWaypoint = new Waypoint(-2.26, -0.98 * allianceMultiplier, 0, 1, 1); 
                break; 
        }

        addCommands(
            // new InstantCommand(() -> {
            //     gyro.reset();
            // }), 
            new Score(arm, intake, scoreFirst), 
        Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
            startWaypoint, 
            START_BETWEEN_TOP
        }, new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared).setReversed(true), 0, 3),
     
         Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
            START_BETWEEN_TOP, 
            SIDE_TOP_PIECE
        }, new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared).setReversed(true), 3, 0),
     
        new MoveToPos(arm, Constants.Arm.Position.GROUND),

        new ParallelCommandGroup(
            Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
                SIDE_TOP_PIECE, 
                INTAKE_POINT
                }, new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared).setReversed(true), 0, 2),
            new IntakeElement(intake, ScoreSpeed.FAST)
            ), 
        new ParallelCommandGroup(
            Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
                INTAKE_POINT, 
                INTAKE_BETWEEN_END
            }, new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared).setReversed(true), 2, 3),
            new MoveToPos(arm, Constants.Arm.Position.CONTRACTED)
        ),

        Constants.Trajectory.trajectoryCreator.createCommand(drivetrain, new Waypoint[] {
            INTAKE_BETWEEN_END, 
            endWaypoint
        }, new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond, Constants.Trajectory.kMaxAccelerationMetersPerSecondSquared).setReversed(false), 3, 0), 
        new Score(arm, intake, scoreSecond)
        );
        // addCommands(new Score(arm, intake, scoreFirst), new MoveBackward(drivetrain, 4));
    }
}