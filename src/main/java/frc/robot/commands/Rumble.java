package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.util.Controller;

// TODO: fix this
public class Rumble extends SequentialCommandGroup {
    public Rumble(Controller controller, double time){
        super(
            new InstantCommand(() -> {
                // controller.setRumble(true);
            }),
            new WaitCommand(time),
            new InstantCommand(() -> {
                // controller.setRumble(false);
            })
        );
    }
}
