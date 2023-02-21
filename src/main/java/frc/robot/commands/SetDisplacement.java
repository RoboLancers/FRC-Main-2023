package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.util.SmartDashboardDB;
import frc.robot.util.enums.Displacement;

public class SetDisplacement extends CommandBase {
    private SmartDashboardDB db = new SmartDashboardDB();

    public SetDisplacement() {
        // Use addRequirements() here to declare subsystem dependencies.
    }

    @Override
    public void execute() {
        double angle = db.getDouble("grid-select");
        if (angle == 90) {
            db.setNumber("displacement", Displacement.RIGHT.getValue());
        } else if (angle == 270) {
            db.setNumber("displacement", Displacement.LEFT.getValue());
        } else {
            db.setNumber("displacement", Displacement.CENTER.getValue());
        }
    }

}
