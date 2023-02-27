package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.Controller;
import frc.robot.util.DriverController.Mode;

public class TeleopDrive extends RunCommand {

    public TeleopDrive(Drivetrain drivetrain, Controller driverController){
        super(() -> {
            drivetrain.curvatureDrive(
                -driverController.getLeftStickY(),
                -driverController.getRightStickX(),
                Mode.NORMAL
            );
        }, drivetrain);
    }
}
