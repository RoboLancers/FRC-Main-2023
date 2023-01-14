package frc.robot.subsystems.arm.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class dropDown extends CommandBase{
    initialize() { 
        // Must check to see if it's in a good position
    }
    execution() {
        // Check which constant we want to access depending on the place we want to place it
        // Put an if loop I guess
        double y = 0.0;
        double z = 0.0;
        Arm arm = 0.0; // Pass this in to inputs
        double[] desiredAngles = arm.calculateAngles(y, z);
    }

    end (bool interrupted) {
        if ()
    }
}
