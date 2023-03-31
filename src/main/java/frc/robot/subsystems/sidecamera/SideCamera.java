package frc.robot.subsystems.sidecamera;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.ShoulderCamera;

public class SideCamera extends SubsystemBase {
    private ShoulderCamera leftCamera;
    private ShoulderCamera rightCamera; 

    public SideCamera(int leftPort, int rightPort) {
        this.leftCamera = new ShoulderCamera("Left Camera", leftPort, 360, 360, Constants.SideCamera.leftXLow, Constants.SideCamera.leftXHigh); 
        this.rightCamera = new ShoulderCamera("Right Camera", rightPort, 360, 360, Constants.SideCamera.rightXLow, Constants.SideCamera.rightXHigh); 
    }
    
}
