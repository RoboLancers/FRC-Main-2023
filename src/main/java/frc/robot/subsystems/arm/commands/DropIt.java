package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import frc.robot.Constants;
import frc.robot.subsystems.arm.commands.MoveToAngle;
import edu.wpi.first.wpilibj2.command.InstantCommand;

enum DesiredHeight {
        MIDCONE,
        HIGHCONE,
        MIDCUBE,
        HIGHCUBE
    }

public class DropIt extends CommandBase{
    private final Arm arm;
    public DesiredHeight whatHeight;

    public DropIt(DesiredHeight whatHeight, Arm arm){
        this.arm = arm;
        this.whatHeight = whatHeight;
        addRequirements(this.arm);
    }
    
    @Override
    public void initialize(){
        
    }
    @Override
    public void execute() {
        double desiredY = Constants.Arm.Positions.kLowY;
        double desiredZ = Constants.Arm.Positions.kLowZ;
        switch(this.whatHeight) {

            case MIDCONE:
            desiredY = Constants.Arm.Positions.kMidNodeY;
            desiredZ = Constants.Arm.Positions.kMidNodeZ;
            break;
            case HIGHCONE:
            desiredY = Constants.Arm.Positions.kHighNodeY;
            desiredZ = Constants.Arm.Positions.kHighNodeZ;
            break;
            case MIDCUBE:
            desiredY = Constants.Arm.Positions.kMidShelfY;
            desiredZ = Constants.Arm.Positions.kMidShelfZ;
            break;
            case HIGHCUBE:
            desiredY = Constants.Arm.Positions.kHighShelfY;
            desiredZ = Constants.Arm.Positions.kHighShelfZ;
            break;
        }
        double[] calculatedAngles = arm.calculateAngles(desiredY, desiredZ);
        
    }
        // TODO: School stripper
        // Calculate angles, then move to position, then use grabber comands

    @Override
    public void end(){
        // When we can test decide if we want to contract
    }

    @Override
    public boolean isFinished(){
        //ask grabber group for variables
    }
}
