package frc.robot;

public final class Constants {
    public static final class Arm {

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
        }

        public static final class Positions{
            public static final double kLowY = 0.0;
            public static final double kLowZ = 0.0;
            public static final double kMidNodeY = 0.0;
            public static final double kMidNodeZ = 0.0;
            public static final double kMidShelfY = 0.0;
            public static final double kMidShelfZ = 0.0;
            public static final double kHighNodeY = 0.0;
            public static final double kHighNodeZ = 0.0;
            public static final double kHighShelfY = 0.0;
            public static final double kHighShelfZ = 0.0;
            public static final double kIntakeShelfY = 0.0;
            public static final double kIntakeShelfZ = 0.0; 
        }
        
        public static final class Miscellaneous {
            public static final double kAnchorArmLength = 0.0;
            public static final double kFloatingArmLength = 0.0;
            public static final double kDegreesPerTick = 360 / 42;
            public static final double kContractedAnchorAngle = 0.0;
            public static final double kContractedFloatingAngle = 0.0;
            public static final double kMotorPower = 0.0;
        }
    }

}
