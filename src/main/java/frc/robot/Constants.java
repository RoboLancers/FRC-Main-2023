package frc.robot;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;

public final class Constants {
    public static final class Arm {
        public static final class Anchor {
            public static final boolean kInverted = true;
            public static final double kRatio = (90.0 - 13.0) / (27.0);

            public static final double kP = 0.016;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
            public static final double kFF = 0.042;
            public static double kErrorThreshold = 5.0;
            public static double kMaxDownwardOutput = -0.5;

            public static final double kContracted = 13.0;
            public static final double kMinAngle = 13.0;
            public static final double kMaxAngle = 95.0; // TODO: idk about this one
        }
        
        public static final class Floating {
            public static final boolean kInverted = true;
            public static final double kRatio = 360.0/72;

            public static final double kP = 0.01;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
            public static final double kFF = 0.0;
            public static double kErrorThreshold = 5.0;

            public static final double kContracted = 22.0;
            public static final double kMinAngle = 22.0;
            public static final double kMaxAngle = 180.0;
        }

        public static final class Ports {
            public static final int kAnchorPort = 22;
            public static final int kFloatingPort = 23;
            public static final int kAnchorLimitSwitchPort = 9;
            public static final int kFloatingLimitSwitchPort = 0;
        }
        
        public static final class Positions {
            public static final class Contracted {
                public static final double kAnchor = 13.0;
                public static final double kFloating = 22.0;
            }

            public static final class Ground {
                public static final double kAnchor = 13.0;
                public static final double kFloating = 92.0;
            }

            // TODO: all of the below positions

            // human player station
            public static final class Shelf {
                public static final double kAnchor = 95.0;
                public static final double kFloating = 133.0;
            }

            // cube upper shelf
            public static final class Cube {
                public static final double kAnchor = 25.0; // 85.0;
                public static final double kFloating = 40.0; // 115.0;
            }

            // cone middle shelf
            public static final class MiddleCone {
                public static final double kAnchor = 58.0;
                public static final double kFloating = 69.0;
            }

            // cone high shelf
            public static final class HighCone {
                public static final double kAnchor = 95.0;
                public static final double kFloating = 110.0;
            }
        }
        
        public static final class Misc {
            public static final double kUndershotAngle = 0.0;
            public static final double kLowPower = 0.001;
            public static final double distanceBetweenPivotLimelight = 48.26;
        }
    }

    public static class Intake {
        public static int kPort = 24;
        public static double kLowPower = 0.9;
        public static double kHighPower = 0.2;
    }

    public static class Trajectory {
        public static final double ksVolts = 0.23636;
        public static final double ksVoltSecondsPerMeter = 1.7953;
        public static final double kaVoltSecondsSquaredPerMeter = 0.35086;

        // TODO: redo drivetrain angular characterization
        public static final double kTrackWidthMeters = Units.inchesToMeters(23); 
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

    public static class GridAlign {
        public static final double kRumbleTime = 1;
        public static final double kInitialWeight = 1;
        public static final double kGridWeight = 1.0; 

        // Measure these constants, BS values for now
        public static final double kDispRight = 0.35;
        public static final double kDispLeft = -0.35;
      

        public static final double kAdjustZ = 0.32;

        public static final double kCamSanityXMax = 4;
        public static final double kCamSanityXMin = -4;

        public static final double kCamSanityZMax = 0;
        public static final double kCamSanityZMin = -10;
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

        public static final int kMaxAmps = 40; 
        public static final double kThrottleMultiplier = 0.75;
        public static final double kTurnMultiplier = 0.3;
        public static final double kThrottleMultiplierSM = 0.2;
        public static final double kTurnMultiplierSM = 0.15;

        public static final double kForwardThrottleAccelFilter = 0.85;
        public static final double kForwardThrottleDecelFilter = 0.80;
        public static final double kBackwardThrottleAccelFilter = 0.85;
        public static final double kBackwardThrottleDecelFilter = 0.80;
        public static final double kTurnFilter = 1.5;
    }

    public static class Balance {
        public static final double kP = 0.008;
        public static final double kI = 0;
        public static final double kD = 0;
        public static final double kPositionTolerance = 2.0; // TODO: tune this, also keep in mind gyro alignment is trash
        public static final double kVelocityTolerance = 1.0; // TODO: tune this, 1 degree per second seems pretty reasonable for stopped state
    }
}