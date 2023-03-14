package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.DriverController;

public class TeleopDrive extends RunCommand {
    public TeleopDrive(Drivetrain drivetrain, DriverController driverController){
        super(() -> {
            drivetrain.curvatureDrive(driverController.getThrottle(), driverController.getTurn(), driverController.getSlowMode());
        }, drivetrain);
    }
}
