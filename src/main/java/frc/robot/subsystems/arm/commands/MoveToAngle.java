package frc.robot.subsystems.arm.commands;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.MotorType; 
import edu.wpi.first.hal.simulation.ConstBufferCallback;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import com.revrobotics.SparkMaxPIDController;

public class MoveToAngle extends CommandBase {
    double desiredY, desiredZ;
    private Arm arm;

    public MoveToAngle(double desiredY, double desiredZ ){
        this.desiredY = desiredY;
        this.desiredZ = desiredZ;
        addRequirements(this.arm);
        
    }

    // get current angle from encoders
    // get desired angle
    // use pid to move there while reducing error

    double desiredAngles[] = arm.calculateAngles(desiredY, desiredZ);
  
    @Override 
    public void execute(){
        arm.setAngles(desiredAngles[0], desiredAngles[1]);
    }

    @Override
    public boolean isFinished(){
        if(arm.isAnchorAtAngle(desiredAngles[0]) && arm.isFloatingAtAngle(desiredAngles[1])){
            return true;
        }
        return false;
        //look into finishing from PID controller 
    }


    



}
