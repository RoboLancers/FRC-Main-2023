package frc.robot;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

public final class Constants {
    public static final class Arms {

        public static final class FloatingJoint{
            public static final double kP = 0.0;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
            public static final double kFF = 0.0;
            public static final double kMinAngle = 0.0;
            public static final double kMaxAngle = 0.0;
            public static double kFloatingJointErrorThreshold = 0.0;
            
        }

        public static final class AnchorJoint{
            public static final double kP = 0.0;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
            public static final double kFF = 0.0;
            public static final double kMinAngle = 0.0;
            public static final double kMaxAngle = 0.0;
            public static double kanchorJointErrorThreshold = 0.0;
            
        }

        public static final class Ports {
            public static final int kAnchorJointPort = 0;
            public static final int kFloatingArmPort = 0;
            public static final int kAnchorLimitSwitchPort = 0;
            public static final int kFloatingLimitSwitchPort = 0;
        }

        public static final class Positions{
            public static final double kLowFloating = 0.0;
            public static final double kLowAnchor = 0.0;
            public static final double kMidNodeFloating = 0.0;
            public static final double kMidNodeAnchor = 0.0;
            public static final double kMidShelfFloating = 0.0;
            public static final double kMidShelfAnchor = 0.0;
            public static final double kHighNodeFloating = 0.0;
            public static final double kHighNodeAnchor = 0.0;
            public static final double kHighShelfFloating = 0.0;
            public static final double kHighShelfAnchor = 0.0;
            public static final double kIntakeShelfFloating = 0.0;
            public static final double kIntakeShelfAnchor = 0.0; 
        }
        
        public static final class Miscellaneous {
            public static final double kAnchorArmLength = 0.0;
            public static final double kFloatingArmLength = 0.0;
            public static final double kDegreesPerTick = 360 / 42;
            public static final double kContractedAnchorAngle = 0.0;
            public static final double kContractedFloatingAngle = 0.0;
            public static final double kUndershotAngle = 0.0;
            public static final double kLowPower = 0.0;
        }
    }

// TODO: update constants to fit new robot

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
    }
    public static class Drivetrain {
        public static final double kDistPerRot = (3.072/100);
        public static class LeftMotors {
            public static final int kLeftMotor1_Port = 1;
            public static final int kLeftMotor2_Port = 2;
            public static final int kLeftMotor3_Port = 3;
        }
        public static class RightMotors {
            public static final int kRightMotor1_Port = 4;
            public static final int kRightMotor2_Port = 5;
            public static final int kRightMotor3_Port = 6;
        }
    }
}
