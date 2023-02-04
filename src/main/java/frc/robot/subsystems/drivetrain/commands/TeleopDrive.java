package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.Controller;

public class TeleopDrive extends RunCommand {
    public TeleopDrive(Drivetrain drivetrain, Controller controller){
        super(() -> drivetrain.arcadeDrive(
            controller.getLeftStickY(),
            controller.getRightStickX()
        ), drivetrain);
    }
}