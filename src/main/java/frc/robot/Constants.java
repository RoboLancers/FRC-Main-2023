package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;

// TODO: update constants to fit new robot
public final class Constants {

    public static final double kThrottleFilter = 1.25;
    public static final double kTurnFilter = 3;
    public static final int kGyroPort = 1;

    public static class Trajectory {
        public static final double ksVolts = 0.131;
        public static final double ksVoltSecondsPerMeter =  4.03;
        public static final double kaVoltSecondsSquaredPerMeter = 0.521;

        public static final double kTrackWidthMeters = 0.702;
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidthMeters);

        public static final double kMaxSpeedMetersPerSecond = 2.5;
        public static final double kMaxAccelerationMetersPerSecondSquared = 2;

        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;

        public static final double kPDriveVel = 0;


        public static final double kGearRatio = 6.8027597438; 
        public static final double kWheelRadiusInches = 3; 
        public static final double kMetersPerRot = Units.inchesToMeters(2 * Math.PI * kWheelRadiusInches / kGearRatio); 
        // (3.072/100);
        // Units.inchesToMeters(kGearRatio * 2 * kWheelRadiusInches * Math.PI / 42); 

        public static final double kMetersPerSecondPerRPM = kMetersPerRot / 60; 
        

    }
    public static class Drivetrain {
        // public static final double kDistPerRot = (3.072/100);
        public static class LeftMotors {
            public static final int kLeftMotor1_Port = 10;
            public static final int kLeftMotor2_Port = 15;
            public static final int kLeftMotor3_Port = 11;
        }
        public static class RightMotors {
            public static final int kRightMotor1_Port = 19;
            public static final int kRightMotor2_Port = 18; 
            public static final int kRightMotor3_Port = 17;
        }
    }

    /*TODO: provide the constants */
    public static class BalanceConstants {
        public static final double kP = 0; 
        public static final double kI = 0; 
        public static final double kD = 0; 
        public static final double kErrorThreshold = 0; 
    }
}