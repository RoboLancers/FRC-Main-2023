package frc.robot.subsystems.gridAlign.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.gridAlign.GridAlign;

public class AttemptAutoAlign extends ConditionalCommand {
    public AttemptAutoAlign(GridAlign gridAlign, Drivetrain drivetrain, double finalX, double placementHeight){
        super(
            new AutoAlign(gridAlign, drivetrain, finalX, placementHeight).alongWith(new InstantCommand(() -> {
                SmartDashboard.putBoolean("Can Auto Align", true);
            })),
            new InstantCommand(() -> {
                SmartDashboard.putBoolean("Can Auto Align", false);
            }),
            gridAlign::hasTarget
        );
    }
}