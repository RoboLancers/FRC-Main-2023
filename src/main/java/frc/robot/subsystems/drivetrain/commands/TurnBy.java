package frc.robot.subsystems.drivetrain.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.DriverController.Mode;

public class TurnBy extends CommandBase {
        private Drivetrain drivetrain;  
        private PIDController pidController; 
        private DoubleSupplier setpoint; 
        private double initialAngle; 

        public TurnBy(Drivetrain drivetrain, double angle) {
            this(drivetrain, () -> angle); 
        }
    
        public TurnBy(Drivetrain drivetrain, DoubleSupplier setpoint) {
            this.pidController = new PIDController(
                Constants.Drivetrain.kTurnP,
                Constants.Drivetrain.kTurnI,
                Constants.Drivetrain.kTurnD
            ); 
            this.pidController.setTolerance(Constants.Drivetrain.kTurnErrorThreshold);
            this.pidController.enableContinuousInput(-180.0, 180.0);

            SmartDashboard.putNumber("Angular kP", 0.0); 
            SmartDashboard.putNumber("Angular kI", 0.0); 
            SmartDashboard.putNumber("Angular kD", 0.0); 

            SmartDashboard.putBoolean("Angular Running", true);
    
            this.drivetrain = drivetrain;
            this.setpoint = setpoint; 
        }

        @Override
        public void initialize() {
            this.initialAngle = this.drivetrain.getHeading(); 
        }
    
        @Override
        public void execute() {
            // For tuning
            // System.out.println(SmartDashboard.getNumber("Angular kP", 0.0));
            // this.pidController.setPID(
            //     SmartDashboard.getNumber("Angular kP", 0.0),
            //     SmartDashboard.getNumber("Angular kI", 0.0),
            //     SmartDashboard.getNumber("Angular kD", 0.0)
            // );

            double output = pidController.calculate(drivetrain.getHeading() - this.initialAngle, setpoint.getAsDouble()); 
            SmartDashboard.putNumber("error", setpoint.getAsDouble() - drivetrain.getHeading() + this.initialAngle); 
            SmartDashboard.putNumber("output", output); 
            drivetrain.arcadeDrive(0, MathUtil.clamp(output, -1, 1) * Constants.Drivetrain.kQuickTurnMultiplier);
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
            return this.pidController.atSetpoint();
        }
}