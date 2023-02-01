package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class TurnToAngle extends PIDCommand {
    Drivetrain drivetrain;

    public TurnToAngle(Drivetrain drivetrain, double angle){
        super(
            new PIDController(0.02, 0, 0),
            drivetrain::getHeading,
            () -> angle,
            (output) -> drivetrain.arcadeDrive(0, output),
            drivetrain
        );

        this.drivetrain = drivetrain;
        this.m_controller.enableContinuousInput(-180, 180);
    }

    @Override
    public void execute(){
        double calculated = this.m_controller.calculate(this.m_measurement.getAsDouble());
        SmartDashboard.putNumber("calculated", calculated);
        if(true || Math.abs(calculated) < 0.08){
            calculated = Math.signum(calculated) * 0.15;
        }
        this.m_useOutput.accept(calculated);
        SmartDashboard.putNumber("gyro", this.drivetrain.getHeading());
        SmartDashboard.putNumber("error v", this.m_controller.getVelocityError());
    }

    @Override
    public boolean isFinished(){
        return Math.abs(this.m_measurement.getAsDouble() - this.m_setpoint.getAsDouble()) < 2;
    }

    @Override
    public void end(boolean interrupted){
        this.drivetrain.arcadeDrive(0, 0);
    }
}
