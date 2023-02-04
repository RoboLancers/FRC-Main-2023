package frc.robot.util;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class InstantiatorCommand extends InstantCommand {
    public InstantiatorCommand(Supplier<Command> command){
        super(() -> {
            CommandScheduler.getInstance().schedule(command.get());
        });
    }
}
