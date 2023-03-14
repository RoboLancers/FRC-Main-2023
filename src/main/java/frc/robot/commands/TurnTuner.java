package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class TurnTuner extends CommandBase {
    private Drivetrain drivetrain;  
    private double turnThrottle = 0; 
 
    public TurnTuner(Drivetrain drivetrain) {
        this.drivetrain = drivetrain; 
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.rawCurvatureDrive(0, turnThrottle, true);
        turnThrottle += 0.001; 
        SmartDashboard.putNumber("turn throttle", turnThrottle);
    }
    
}
