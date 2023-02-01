package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class TurnToAngle extends PIDCommand {
    private Drivetrain drivetrain;

    public TurnToAngle(Drivetrain drivetrain, double target){
        super(
            new PIDController(0.01, 0, 0),
            () -> target,
            drivetrain::getHeading,
            (output) -> drivetrain.arcadeDrive(0, output),
            drivetrain
        );

        this.drivetrain = drivetrain;

        this.m_controller.enableContinuousInput(-180, 180);
    }

    @Override
    public void execute(){
        double rawOutput = this.m_controller.calculate(
            this.m_measurement.getAsDouble(),
            this.m_setpoint.getAsDouble()
        );

        if(Math.abs(rawOutput) > 0.2){
            rawOutput = Math.signum(rawOutput) * 0.2;
        } else if(Math.abs(rawOutput) < 0.05){
            rawOutput = Math.signum(rawOutput) * 0.05;
        }

        this.m_useOutput.accept(rawOutput);
    }

    @Override
    public boolean isFinished(){
        double error = this.m_measurement.getAsDouble() - this.m_setpoint.getAsDouble();

        return Math.abs(error) < 0.2;
    }

    @Override
    public void end(boolean interrupted){
        this.drivetrain.arcadeDrive(0, 0);
    }
}
