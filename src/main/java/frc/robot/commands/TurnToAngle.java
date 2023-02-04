package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class TurnToAngle extends PIDCommand {
    public TurnToAngle(Drivetrain drivetrain, DoubleSupplier angleSupplier){
        super(
            new PIDController(
                Constants.Drivetrain.TurnToAngle.kP,
                Constants.Drivetrain.TurnToAngle.kI,
                Constants.Drivetrain.TurnToAngle.kD
            ),
            drivetrain::getHeading,
            angleSupplier,
            (output) -> drivetrain.arcadeDrive(0, output),
            drivetrain
        );

        this.m_controller.enableContinuousInput(-180, 180);
    }

    public TurnToAngle(Drivetrain drivetrain, double angle){
        this(drivetrain, () -> angle);
    }

    @Override
    public void execute(){
        double output = this.m_controller.calculate(this.m_measurement.getAsDouble(), this.m_setpoint.getAsDouble());

        if(Math.abs(output) < Constants.Drivetrain.TurnToAngle.kMinOutputMagnitude){
            output = Math.signum(output) * Constants.Drivetrain.TurnToAngle.kMinOutputMagnitude;
        } else if(Math.abs(output) > Constants.Drivetrain.TurnToAngle.kMaxOutputMagnitude){
            output = Math.signum(output) * Constants.Drivetrain.TurnToAngle.kMaxOutputMagnitude;
        }
        
        this.m_useOutput.accept(output);
    }

    // TODO: built in set tolerance does not work? look into error velocity tolerance
    @Override
    public boolean isFinished(){
        return Math.abs(this.m_measurement.getAsDouble() - this.m_setpoint.getAsDouble()) < 2;
    }

    @Override
    public void end(boolean interrupted){
        this.m_useOutput.accept(0);
    }
}