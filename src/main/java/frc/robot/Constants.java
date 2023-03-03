package frc.robot;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.Units;

public final class Constants {
    public static final class Arm {
        // L1
        public static final class Anchor {
            public static final boolean kInverted = true;
            public static final double kRatio = (90.0 - 13.0) / (27.0); // TODO: this probably isn't correct because of gearing

            public static final double kP = 0.025;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
            public static final double kFF = 0.0;
            public static double kErrorThreshold = 2.0;

            // TODO: this doesn't sound right?
            public static final double kContracted = 13.0;
            public static final double kMinAngle = 13.0;
            public static final double kMaxAngle = 85.0;

            public static final double kLength = 101.6;
        }

        // L2
        public static final class Floating {
            public static final boolean kInverted = true;
            public static final double kRatio = 360.0/72; // TODO: this probably isn't correct because of gearing

            public static final double kP = 0.001;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
            public static final double kFF = 0.0;
            public static double kErrorThreshold = 0.0;

            public static final double kContracted = 0.0;
            public static final double kMinAngle = 22.0;
            public static final double kMaxAngle = 180.0;

            public static final double kLength = 43.18; // TODO: might have to remeasure
        }

        public static final class Ports {
            public static final int kAnchorPort = 22;
            public static final int kFloatingPort = 23;
            public static final int kAnchorLimitSwitchPort = 9;
            public static final int kFloatingLimitSwitchPort = 0;
        }

        // TODO: calculate these or do it dynamically, but something needs to happen with them
        // TODO: if we do these statically, create a "ArmState" wrapper
        public static final class Positions {
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

        // public enum ArmState {
        //     LOW(0, 0),
        //     MID(0, 0), 
        //     HIGH(0, 0), 
        //     INTAKE(0, 0); 

        //     private final double floating; 
        //     private final double anchor; 

        //     ArmState(double floating, double anchor) {
        //         this.floating = floating; 
        //         this.anchor = anchor; 
        //     }

        //     public double getFloating() {
        //         return floating; 
        //     }

        //     public double getAnchor() {
        //         return anchor; 
        //     }
        // }
        
        public static final class Misc {
            public static final double kUndershotAngle = 0.0;
            public static final double kLowPower = 0.001;
            public static final double distanceBetweenPivotLimelight = 48.26;
        }
    }

    public static class Grabber {
        public static int kPistonDeploy = 0;
        public static int kPistonRetract = 1;
        public static int kGrabberSensor;
        public static int kBits;
        public static int kChannel;
        public static double kMax;
        public static double kStart;
        public static double kMin;
    }

    public static class Intake {
        public static int kPort; // TODO
        public static double kForwardPower = 0.2;
        public static double kBackwardPower = -0.2;
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

        public static final int kMaxAmps = 30; 
        public static final double kThrottleMultiplier = 0.75;
        public static final double kTurnMultiplier = 0.6;
        public static final double kThrottleMultiplierSM = 0.2;
        public static final double kTurnMultiplierSM = 0.15;

        public static final double kForwardThrottleAccelFilter = 1.25;
        public static final double kForwardThrottleDecelFilter = 1.00;
        public static final double kBackwardThrottleAccelFilter = 1.25;
        public static final double kBackwardThrottleDecelFilter = 1.00;
        public static final double kTurnFilter = 2;
    }

    public static class Balance {
        public static final double kP = 0.008;
        public static final double kI = 0;
        public static final double kD = 0;
        public static final double kPositionTolerance = 2.0; // TODO: tune this, also keep in mind gyro alignment is trash
        public static final double kVelocityTolerance = 1.0; // TODO: tune this, 1 degree per second seems pretty reasonable for stopped state
    }
}