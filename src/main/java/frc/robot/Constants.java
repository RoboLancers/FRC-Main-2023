package frc.robot;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants.Intake.ScoreSpeed;
import frc.robot.trajectory.TrajectoryCreator;

public final class Constants {
    public static final class Arm {
        public static final class Anchor {
            public static final boolean kInverted = true;
            public static final double kRatio = (90.0 - 13.0) / (27.0); // sigh, do this right later

            public static final double kP = 0.016;
            public static final double kI = 0.0;
            public static final double kD = 0.0;
            public static final double kFF = 0.042;
            public static double kErrorThreshold = 5.0;
            public static double kMaxDownwardOutput = -0.4;
            public static double kMaxUpwardOutput = 0.5;

            public static final double kContracted = 16.0;
            public static final double kMinAngle = 16.0;
            public static final double kMaxAngle = 95.0; // TODO: idk about this one
        }
        
        public static final class Floating {
            public static final boolean kInverted = true;
            public static final double kRatio = 360; // 360.0/72;

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

        public enum Position {
            // TODO: added 3 to all anchor setpoints because of change from 13 to 16, look into the validity of setpoints after this change
            
            CONTRACTED(16.0, 22.0), 
            GROUND(16.0, 113.0), 

            MID_CUBE(16.0, 40.0),
            HIGH_CUBE(55.0, 82.0),

            HIGH_CONE(96.0, 134.0),
            HIGH_CONE_AIMING(96.0, 22.0),

            MID_CONE(55.0, 95.0),
            MID_CONE_AIMING(55.0, 22.0),

            SHELF(87.0, 111.0), 
            STATION(16.0, 14.0);

            private final double anchor; 
            private final double floating; 

            Position(double anchor, double floating) {
                this.anchor = anchor; 
                this.floating = floating; 
            }

            public double getAnchor() {
                return anchor; 
            }

            public double getFloating() {
                return floating; 
            }
        }

        public enum ScoringPosition {
            HIGH_CUBE(Position.HIGH_CUBE, ScoreSpeed.FAST), 
            MID_CUBE(Position.CONTRACTED, ScoreSpeed.SLOW), 
            LOW_CUBE(Position.GROUND, ScoreSpeed.SLOW), 

            HIGH_CONE(Position.HIGH_CONE, ScoreSpeed.SLOW), 
            MID_CONE(Position.MID_CONE, ScoreSpeed.SLOW),
            LOW_CONE(Position.GROUND, ScoreSpeed.SLOW); 

            private final Position position; 
            private final ScoreSpeed speed; 

            ScoringPosition(Position pos, ScoreSpeed speed) {
                this.speed = speed; 
                this.position = pos; 
            } 

            public Position getPosition() {
                return position; 
            }

            public ScoreSpeed getSpeed() {
                return speed; 
            }
        }
        
        public static final class Misc {
            public static final double kUndershotAngle = 0.0;
            public static final double kLowPower = 0.001;
            public static final double distanceBetweenPivotLimelight = 48.26;
        }
    }

    public static class Intake {
        public static final int kPort = 24;
        public static final double kLowPower = 0.2;
        public static final double kHighPower = 0.9;
        public static final double kAutoIntakeSeconds = 1; 
        public static final double kAutoOuttakeSeconds = 1; 

        public enum ScoreSpeed {
            FAST, 
            SLOW; 
        }
    }

    public static class Trajectory {
        public static final double ksVolts = 0.28433; // 0.24855;
        public static final double ksVoltSecondsPerMeter = 1.8493; // 1.7848;
        public static final double kaVoltSecondsSquaredPerMeter = 0.4429; // 0.47551;

        public static final double kMaxSpeedMetersPerSecond = 2.5;
        public static final double kMaxAccelerationMetersPerSecondSquared = 0.8;

        public static final double kMaxVoltage = 10;

        // TODO: redo drivetrain angular characterization
        public static final double kTrackWidthMeters = 0.59261; // 1.2546; // Units.inchesToMeters(23);   
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidthMeters);

        public static final TrajectoryCreator trajectoryCreator = new TrajectoryCreator(
                kDriveKinematics,
                new DifferentialDriveVoltageConstraint(
                        new SimpleMotorFeedforward(ksVolts, ksVoltSecondsPerMeter, kaVoltSecondsSquaredPerMeter),
                        kDriveKinematics, kMaxVoltage
                )
        );

        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;

        public static final double kPDriveVel = 2.6346; // 0; // 2.5614;

        public static final double kGearRatio = 6.8027597438; 
        public static final double kWheelRadiusInches = 3; 
        public static final double kMetersPerRot = Units.inchesToMeters(2 * Math.PI * kWheelRadiusInches / kGearRatio); 

        public static final double kMetersPerSecondPerRPM = kMetersPerRot / 60; 
    }

    public static class GridAlign {
        public static final double kRumbleTime = 1;
        public static final double kInitialWeight = 1;
        public static final double kGridWeight = 1.0; 

        public static final double kRightOffset = 0.35;
        public static final double kLeftOffset = -0.35;
      
        public static final double kAdjustZ = 0.32;

        public static final double kCamSanityXMax = 4;
        public static final double kCamSanityXMin = -4;

        public static final double kCamSanityZMax = 0;
        public static final double kCamSanityZMin = -10;

        // TODO: tune next week
        public static final double kSteer = 0.0; 
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

        public static final int kMaxFreeAmps = 35;
        public static final int kMaxStallAmps = 45;  
        public static final double kThrottleMultiplier = 0.70;
        public static final double kQuickTurnMultiplier = 0.23;
        public static final double kSlowThrottleTurnMultiplier = 0.55; 
        public static final double kFastThrottleTurnMultiplier = 0.23; 
        public static final double kThrottleMultiplierSM = 0.18;
        public static final double kTurnMultiplierSM = 0.15;

        public static final double kForwardThrottleAccelFilter = 0.85;
        public static final double kForwardThrottleDecelFilter = 0.8;
        public static final double kBackwardThrottleAccelFilter = 0.85;
        public static final double kBackwardThrottleDecelFilter = 0.8;
        public static final double kTurnFilter = 3;

        // TODO: tune these
        public static final double kTurnP = 0.048; 
        public static final double kTurnI = 0; 
        public static final double kTurnD = 0; 
        public static final double kTurnErrorThreshold = 2; 
    }

    public static class Balance {
        public static final double kP = 0.009;
        public static final double kI = 0;
        public static final double kD = 0.0003;
        public static final double kPositionTolerance = 2.0; // TODO: tune this, also keep in mind gyro alignment is trash
        public static final double kVelocityTolerance = 1.0; // TODO: tune this, 1 degree per second seems pretty reasonable for stopped state
    }


    // ! TODO: get this value from Electrical/Mechanical
    public static int LED_PWM_PORT = 0;
}