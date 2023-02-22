package frc.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardDB {
    public SmartDashboardDB() {

    }

    public double getDouble(String key) {
        return SmartDashboard.getNumber(key, 0);
    }

    public int getInt(String key) {
        return (int) this.getDouble(key);
    }

    public boolean getBoolean(String key) {
        return SmartDashboard.getBoolean(key, false);
    }

    public String getString(String key) {
        return SmartDashboard.getString(key, "");
    }

    public void setNumber(String key, double value) {
        SmartDashboard.putNumber(key, value);
    }

    public void setString(String key, String value) {
        SmartDashboard.putString(key, value);
    }

}
