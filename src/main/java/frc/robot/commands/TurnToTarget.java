package frc.robot.commands;

import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.commands.TurnToAngle;
import frc.robot.util.limelight.LimelightAPI;
import frc.robot.util.limelight.PoseEstimator;

public class TurnToTarget extends TurnToAngle {
    public TurnToTarget(Drivetrain drivetrain){
        super(drivetrain, () -> LimelightAPI.validTargets() ? PoseEstimator.calculateHeadingError() : 0);
    }
}
