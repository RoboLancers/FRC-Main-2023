package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;

// TODO: update constants to fit new robot
public final class Constants {

    public static final double kThrottleAccelFilter = 1.25;
    public static final double kTurnAccelFilter = 3;
    public static final double kThrottleDecelFilter = -1;
    public static final double kTurnDecelFilter = -3;
    public static final int kGyroPort = 1;
  public static  class Grabber {
        public static int kPistonDeploy = 0;
        public static int kPistonRetract = 1;
        public static int kGrabberSensor;
        public static int kBits;
        public static int kChannel;
        public static double kMax;
        public static double kStart;
        public static double kMin;
    }

    public static class Trajectory {
        public static final double ksVolts = 0.23636;
        public static final double ksVoltSecondsPerMeter = 1.7953;
        public static final double kaVoltSecondsSquaredPerMeter = 0.35086;

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

        public static final double kMetersPerSecondPerRPM = kMetersPerRot / 60; 

    }
    public static class Drivetrain {
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

        public static final int kMaxAmps = 30; 
        public static final double kThrottleMultiplier = 0.75;
        public static final double kTurnMultiplier = 0.6;
    }

    /*TODO: provide the constants */

    // referenced in frc.robot.subsystems.gyro.Balance but never defined; TODO: edit these values
    public static class BalanceConstants {
        public static final double kP = 0.008;
        public static final double kI = 0;
        public static final double kD = 0;
        public static final double kErrorThreshold = 1;
    }
}