package frc.robot.subsystems.gyro.commands;

import edu.wpi.first.wpilibj2.command.PIDCommand;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.gyro.Gyro;

public class Balance extends PIDCommand {
    private Gyro gyro;

    public Balance(Drivetrain drivetrain, Gyro gyro, double setpoint) {
        super(
            new PIDController(Constants.Balance.kP, Constants.Balance.kI, Constants.Balance.kD),
            gyro::getPitch,
            // Set reference to target
            () -> setpoint,
            // Pipe output to turn robot
            (outputPower) -> drivetrain.arcadeDrive(outputPower, 0, frc.robot.util.DriverController.Mode.NORMAL), // TODO: was this negative?
            drivetrain
        );

        this.gyro = gyro;

        SmartDashboard.putBoolean("Balance Running", true);
        SmartDashboard.putNumber("balance-kP", SmartDashboard.getNumber("balance-kP", Constants.Balance.kP));
        SmartDashboard.putNumber("balance-kD", SmartDashboard.getNumber("balance-kD", Constants.Balance.kD));
    
        addRequirements(drivetrain);
    }

    @Override
    public void execute(){
        SmartDashboard.putNumber("pitch-error", Math.abs(this.m_measurement.getAsDouble()));
        SmartDashboard.putNumber("pitch-velocity", Math.abs(this.gyro.getPitchVelocity()));

        this.m_controller.setP(SmartDashboard.getNumber("balance-kP", 0));
        this.m_controller.setD(SmartDashboard.getNumber("balance-kD", 0));

        double output = this.m_controller.calculate(this.m_measurement.getAsDouble(), this.m_setpoint.getAsDouble());

        this.m_useOutput.accept(-output);

        // TODO: we can tune this more if necessary, but 0.008 works well
    }

    @Override
    public void end(boolean interrupted){
        SmartDashboard.putBoolean("Balance Running", false);
        
        this.m_useOutput.accept(0);
    }

    @Override
    public boolean isFinished(){
        return false;
        // return (
        //     Math.abs(this.m_measurement.getAsDouble()) < Constants.Balance.kPositionTolerance &&
        //     Math.abs(this.gyro.getPitchVelocity()) < Constants.Balance.kVelocityTolerance
        // );
    }
}