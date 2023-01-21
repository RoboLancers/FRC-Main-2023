package frc.robot;

public static final class Constants {
    public static final class Arm {

        public static final class FloatingJoint{
            public static final double kP;
            public static final double kI;
            public static final double kD;
            public static final double kFF;
            public static final double kMinAngle;
            public static final double kMaxAngle;
            public static double kFloatingJointErrorThreshold;
            
        }

        public static final class AnchorJoint{
            public static final double kP;
            public static final double kI;
            public static final double kD;
            public static final double kFF;
            public static final double kMinAngle;
            public static final double kMaxAngle;
            public static double kanchorJointErrorThreshold;
            
        }

        public static final class Ports{
        public static final int kAnchorJointPort;
        public static final int kFloatingArmPort;
        
        }

        public static final class Positions{
            public static final double kLowY;
            public static final double kLowZ;
            public static final double kMidNodeY;
            public static final double kMidNodeZ;
            public static final double kMidShelfY;
            public static final double kMidShelfZ;
            public static final double kHighNodeY;
            public static final double kHighNodeZ;
            public static final double kHighShelfY;
            public static final double kHighShelfZ;    
        }
        
        public static final double kAnchorArmLength;
        public static final double kFloatingArmLength;
        
        public static final double kDegreesPerTick = 360 / 42;
        public static final double kContractedAnchorAngle;
        public static final double kContractedFloatingAngle;
        public static final double kMotorPower;
      

    }

}
