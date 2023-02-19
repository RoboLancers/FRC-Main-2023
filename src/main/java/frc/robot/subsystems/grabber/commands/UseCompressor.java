package frc.robot.subsystems.grabber.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.grabber.Pneumatics;

public class UseCompressor extends CommandBase {
    private Pneumatics pneumatics;

    public UseCompressor(Pneumatics pneumatics) {
        this.pneumatics = pneumatics;
        addRequirements(pneumatics);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}