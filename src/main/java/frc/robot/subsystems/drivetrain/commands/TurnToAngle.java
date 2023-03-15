package frc.robot.subsystems.drivetrain.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.DriverController.Mode;

public class TurnToAngle extends PIDCommand {
        private Drivetrain drivetrain;  
    
        public TurnToAngle(Drivetrain drivetrain, DoubleSupplier setpoint) {
            super(
                new PIDController(
                    Constants.Drivetrain.kTurnP,
                    Constants.Drivetrain.kTurnI,
                    Constants.Drivetrain.kTurnD
                ),
                drivetrain::getHeading,
                () -> {
                    double setpointValue = setpoint.getAsDouble();
                    SmartDashboard.putNumber("Angular Error", setpointValue - drivetrain.getHeading());
                    return setpointValue;
                },
                (outputPower) -> {
                    drivetrain.arcadeDrive(0, MathUtil.clamp(outputPower, -1, 1) * Constants.Drivetrain.kQuickTurnMultiplier, Mode.NORMAL);
                },
                drivetrain
            );
            this.getController().setTolerance(Constants.Drivetrain.kTurnErrorThreshold);
            this.getController().enableContinuousInput(-180.0, 180.0);

            SmartDashboard.putNumber("Angular kP", 0.0); 
            SmartDashboard.putNumber("Angular kI", 0.0); 
            SmartDashboard.putNumber("Angular kD", 0.0); 

            SmartDashboard.putBoolean("Angular Running", true);
    
            this.drivetrain = drivetrain;
        }
    
        @Override
        public void execute() {
            // System.out.println(SmartDashboard.getNumber("Angular kP", 0.0));
            super.execute();
            this.getController().setPID(
                SmartDashboard.getNumber("Angular kP", 0.0),
                SmartDashboard.getNumber("Angular kI", 0.0),
                SmartDashboard.getNumber("Angular kD", 0.0)
            );

            // this.m_useOutput.accept(output);
        }
    
        @Override
        public void end(boolean interrupted) {
            SmartDashboard.putBoolean("Angular Running", false);
            drivetrain.leftMotors.set(0);
            drivetrain.rightMotors.set(0);
        }
    
        @Override
        public boolean isFinished() {
            return this.getController().atSetpoint();
        }
}