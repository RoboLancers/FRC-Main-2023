package frc.robot;

import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

// TODO: update constants to fit new robot
public final class Constants {

    public static final double kThrottleFilter = 1.25;
    public static final double kTurnFilter = 3;
    public static final int kGyroPort = 1;

    public static class Trajectory {
        public static final double ksVolts = 0.131;
        public static final double ksVoltSecondsPerMeter = 4.03;
        public static final double kaVoltSecondsSquaredPerMeter = 0.521;

        public static final double kTrackWidthMeters = 0.702;
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(
                kTrackWidthMeters);

        public static final double kMaxSpeedMetersPerSecond = 2.5;
        public static final double kMaxAccelerationMetersPerSecondSquared = 2;

        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;

        public static final double kPDriveVel = 0;
    }

    public static class GridAlign {
        public static final double kRumbleTime = 1;
        public static final double kInitialWeight = 1;
        public static final double kGridWeight = 2;

        public static final double kAdjustZ = 1;
        public static final double kAdjustX = 1;

        public static final int kAprilTagPipelineIndex = 0;
        public static final int kPythonPipelineIndex = 1;
        public static final int kManualAprilTagPipeline = 2;

        public static final double[] kCamSanityX = {
                -2, // min
                2 // max
        };

        public static final double[] kCamSanityZ = {
                -1, // min
                5 // max
        };
    }

    public static class Drivetrain {
        public static final double kDistPerRot = (3.072 / 100);

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