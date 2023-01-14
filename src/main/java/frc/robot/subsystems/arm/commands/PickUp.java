package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import frc.robot.Constants;


public class PickUp extends CommandBase{
    
    @Override
    public void initialize(){
        // Check to make sure arm is in good position else reset arm to contracted position
    }

    @Override
    public void execute(){
        Arm arm = null;
        // set y and z of to target via presets 

        //Calcuate desired angles 
        double[] desiredAngles = arm.calculateAngles(Constants.kLowY, Constants.kLowZ);
        //Set power to motors until encoders get to the right values

        //Move to desired angles
        //Grab
        
    }

    @Override
    public void end(){
        
    }

    @Override
    public boolean isFinished(){
        
    }
}