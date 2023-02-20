package frc.robot.commands.GRR;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.GridAlign;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.arm.commands.MoveToPos;
import frc.robot.subsystems.grabber.Grabber;

import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.util.Controller;
import frc.robot.util.InstantiatorCommand;
import frc.robot.util.enums.Displacement;

public class TeleopGRR extends SequentialCommandGroup {
    public static Controller controller = new Controller(0);
    public static Drivetrain drivetrain = new Drivetrain(controller);
    public static PoseTracker tracker = new PoseTracker(drivetrain);
    public static Arm arm = new Arm();
    public static Grabber grabber = new Grabber();

    public TeleopGRR(Supplier<Displacement> displacement) {

        super(
                new InstantiatorCommand(() -> new GridAlign(drivetrain, tracker, displacement)),
                // Fix the arm BS stuff
                new MoveToPos(arm, 0, 0),
                new RunCommand(() -> grabber.toggleDeploy(), grabber));

        addRequirements(drivetrain, arm, grabber);
    }

}
