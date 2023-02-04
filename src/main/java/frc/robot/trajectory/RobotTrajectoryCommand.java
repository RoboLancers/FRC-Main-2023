package frc.robot.trajectory;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.trajectory.MotionProfileCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;
import org.bananasamirite.robotmotionprofile.data.Trajectory;
import org.bananasamirite.robotmotionprofile.data.task.CommandTask;
import org.bananasamirite.robotmotionprofile.data.task.WaypointTask;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class RobotTrajectoryCommand extends SequentialCommandGroup {
    public RobotTrajectoryCommand(Drivetrain drivetrain, Trajectory trajectory) {
        Object[] commands = trajectory.getTasks().stream().map(e -> {
            System.out.println(e);
            if (e instanceof WaypointTask && ((WaypointTask) e).getWaypoints().size() > 1) return new MotionProfileCommand(drivetrain, ((WaypointTask) e).createProfile());
            if (e instanceof CommandTask) {
                try {
                    return TrajectoryCommandsManager.getInstance().getCommandConfig(((CommandTask) e).getWaypoint().getCommandName()).createCommand(((CommandTask) e).getWaypoint().getParameters().toArray());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            return null;
        }).filter(Objects::nonNull).toArray();
        addCommands(Arrays.copyOf(commands, commands.length, Command[].class));
    }

    public static RobotTrajectoryCommand fromFile(Drivetrain drivetrain, File f) throws IOException {
        return new RobotTrajectoryCommand(drivetrain, Trajectory.fromFile(f));
    }
}
