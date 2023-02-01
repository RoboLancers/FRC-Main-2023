package frc.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Tunable {
    private String name;

    public Tunable(String name){
        this.name = name;
        SmartDashboard.putNumber(name, 0);
    }

    public Tunable(String name, double defaultValue){
        this.name = name;
        SmartDashboard.putNumber(name, defaultValue);
    }

    public double get(){
        return SmartDashboard.getNumber(this.name, 0);
    }
}