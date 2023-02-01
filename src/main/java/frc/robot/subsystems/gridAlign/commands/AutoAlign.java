package frc.robot.subsystems.gridAlign.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.MotionProfileCommand;
import frc.robot.commands.TurnToAngle;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.gridAlign.GridAlign;

public class AutoAlign extends SequentialCommandGroup {
    public AutoAlign(GridAlign gridAlign, Drivetrain drivetrain, double finalX, double placementHeight){
        super(
            new InstantCommand(gridAlign::recordStartingCameraPosition),
            new InstantCommand(gridAlign::calculateFinalBotPosition) //,


            // new TurnToAngle(drivetrain, 0),
            // new MotionProfileCommand(drivetrain, gridAlign.generateMotionProfile())
            
            // new MoveArmToPosition()
            // TODO: using finalX and placementHeight
        );
    }
}

// TODO: does it make more sense to turn first and avoid calculations?
// Pros: math will be correct
// Cons: may focus on the wrong tag