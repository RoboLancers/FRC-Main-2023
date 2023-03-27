package frc.robot.team254;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.team254.DriveMotionPlanner;


public class TrajectoryGenerator {
    Drivetrain drivetrain;
    DriveMotionPlanner motionPlanner;
    TrajectoryConstraint voltageConstraint = Constants.Trajectory.voltageConstraint;
    TrajectoryConstraint centripetalAccelConstraint = Constants.Trajectory.centripetalAccelerationConstraint;
    List<TrajectoryConstraint> constraints = Arrays.asList(voltageConstraint, centripetalAccelConstraint);
    double fieldWidth = 8.02;

    //BLUE ALLIANCE
    Pose2d kStart = new Pose2d(new Translation2d(0, 0), new Rotation2d(0));
    Pose2d kInitVelStart = new Pose2d(new Translation2d(1, 0), new Rotation2d(0));
    Pose2d kInitVelEnd = new Pose2d(new Translation2d(2, 1), new Rotation2d(90));
    Pose2d kSplineBlue = new Pose2d(new Translation2d(1, 1), new Rotation2d(0));

    //RED ALLIANCE
    Pose2d kSplineRed = transformStateForAlliance(kSplineBlue);

    public TrajectoryGenerator(Drivetrain drivetrain){
        this.drivetrain = drivetrain;
        this.motionPlanner = new DriveMotionPlanner();
    }

    public Pose2d transformStateForAlliance(Pose2d pose){
        /*
        The 2023 FRC game has non-standard field mirroring that would prevent using the same auto path for both alliances.
        To work around this, PathPlannerLib has added functionality to automatically transform paths to work 
        for the correct alliance depending on the current alliance color while using PathPlannerLib\'s path following commands.
        In order for this to work correctly, you MUST create all of your paths on the blue (left) side of the field.

        yeah I copied from path planner. tbh idk if this works b/c 
        if blue = (1, 0) reversed - going backwards
        then red = (-1, 0) reversed - going forwards
        */

        Translation2d transformedTranslation = new Translation2d(pose.getX(), fieldWidth - pose.getY());
        Rotation2d transformedRotation = pose.getRotation().times(-1);
        Pose2d transformedPose = new Pose2d(transformedTranslation, transformedRotation);
        return transformedPose;
    }

    public Command generateTrajectory(
            Drivetrain drivetrain,
            boolean reversed,
            final List<Pose2d> waypoints,
            final List<TrajectoryConstraint> constraints
            ) {
        return motionPlanner.generateTrajectory(drivetrain, reversed, waypoints, constraints);
    }

     public Command generateTrajectory(
            Drivetrain drivetrain,
            boolean reversed,
            final List<Pose2d> waypoints,
            final List<TrajectoryConstraint> constraints,
            double start_vel,  // inches/s
            double end_vel  // inches/s
            ) {
        return motionPlanner.generateTrajectory(drivetrain, reversed, waypoints, constraints, start_vel, end_vel);
    }

    public Command getInitVelTest1() {
        List<Pose2d> waypoints = new ArrayList<>();
        waypoints.add(kStart);
        waypoints.add(kInitVelStart);
        return generateTrajectory(drivetrain, false, waypoints, constraints, 0, 1.0);
    }

    public Command getInitVelTest2() {
        List<Pose2d> waypoints = new ArrayList<>();
        waypoints.add(kInitVelStart);
        waypoints.add(kInitVelEnd);
        return generateTrajectory(drivetrain, false, waypoints, constraints, 1.0, 0);
    }

    public Command getStraightTest() {
        List<Pose2d> waypoints = new ArrayList<>();
        waypoints.add(kStart);
        waypoints.add(kInitVelStart);
        return generateTrajectory(drivetrain, false, waypoints, constraints);
    }

    public Command getSplineTestBlue() {
        List<Pose2d> waypoints = new ArrayList<>();
        waypoints.add(kStart);
        waypoints.add(kSplineBlue);
        return generateTrajectory(drivetrain, false, waypoints, constraints);
    }

    public Command getSplineTestRed() {
        List<Pose2d> waypoints = new ArrayList<>();
        waypoints.add(kStart);
        waypoints.add(kSplineRed);
        return generateTrajectory(drivetrain, false, waypoints, constraints);
    }


}
