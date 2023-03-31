package frc.robot.trajectory;

import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
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
            if (e instanceof WaypointTask && ((WaypointTask) e).getWaypoints().size() > 1) {
                return Constants.Trajectory.trajectoryCreator.createCommand(
                        drivetrain,
                        ((WaypointTask) e).getWaypoints(),
                        new TrajectoryConfig(
                            ((WaypointTask) e).getConstraints().getMaxVelocity(),
                            ((WaypointTask) e).getConstraints().getMaxAcceleration()
                        ).setReversed(
                            ((WaypointTask) e).getReversed()
                        ), true
                );
            }
            if (e instanceof CommandTask && !((CommandTask) e).getWaypoint().getCommandName().equals("")) {
                try {
                    return TrajectoryCommandsManager.getInstance().getCommandConfig(((CommandTask) e).getWaypoint().getCommandName()).createCommand(((CommandTask) e).getWaypoint().getParameters().toArray());
                } catch (Exception ex) {
                    System.err.println("Error parsing Command Task with name, " + ((CommandTask) e).getWaypoint().getName() + ": Please check if the command, " + ((CommandTask) e).getWaypoint().getCommandName() + ", is registered. ");
                    ex.printStackTrace();
                    return null;
                }
            }
            return null;
        }).filter(Objects::nonNull).toArray();
        addCommands(Arrays.copyOf(commands, commands.length, Command[].class));
    }

    public static RobotTrajectoryCommand fromFile(Drivetrain drivetrain, File f) throws IOException {
        return new RobotTrajectoryCommand(drivetrain, Trajectory.parseFromFile(f));
    }
}
