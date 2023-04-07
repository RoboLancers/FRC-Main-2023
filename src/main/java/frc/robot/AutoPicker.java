package frc.robot;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.intake.Intake;
import frc.robot.Constants.Arm.ScoringPosition;
import frc.robot.commands.BottomLaneAuto;
import frc.robot.commands.MidLaneAuto;
import frc.robot.commands.MidLaneAutoTaxi;
import frc.robot.commands.TopLaneAuto;

public class AutoPicker {
    private SendableChooser<Command> autoChooser = new SendableChooser<>();

    private Drivetrain drivetrain; 
    private Arm arm;
    private Intake intake; 
    private Gyro gyro; 

    public AutoPicker(Drivetrain drivetrain, Arm arm, Gyro gyro, Intake intake) {
        this.drivetrain = drivetrain; 
        this.arm = arm; 
        this.gyro = gyro; 
        this.intake = intake;
        
        configureAutos();
    }

    private void configureAutos() {
        Command redNoBumpLane = new TopLaneAuto(drivetrain, arm, gyro, intake, ScoringPosition.HIGH_CONE, ScoringPosition.HIGH_CUBE, Alliance.Red);
        Command blueNoBumpLane = new TopLaneAuto(drivetrain, arm, gyro, intake, ScoringPosition.HIGH_CONE, ScoringPosition.HIGH_CUBE, Alliance.Blue);
        Command balanceLaneCone = new MidLaneAuto(drivetrain, gyro, arm, intake, ScoringPosition.HIGH_CONE);
        Command balanceLaneCube = new MidLaneAuto(drivetrain, gyro, arm, intake, ScoringPosition.HIGH_CUBE);
        Command taxiBalanceLaneCone = new MidLaneAutoTaxi(drivetrain, gyro, arm, intake, ScoringPosition.HIGH_CONE);
        Command taxiBalanceLaneCube = new MidLaneAutoTaxi(drivetrain, gyro, arm, intake, ScoringPosition.HIGH_CUBE);
        Command redBumpLaneCone = new BottomLaneAuto(drivetrain, arm, intake, ScoringPosition.HIGH_CONE, Alliance.Red);
        Command redBumpLaneCube = new BottomLaneAuto(drivetrain, arm, intake, ScoringPosition.HIGH_CUBE, Alliance.Red);
        Command blueBumpLaneCone = new BottomLaneAuto(drivetrain, arm, intake, ScoringPosition.HIGH_CONE, Alliance.Blue);
        Command blueBumpLaneCube = new BottomLaneAuto(drivetrain, arm, intake, ScoringPosition.HIGH_CUBE, Alliance.Blue);

        autoChooser.addOption("Red No Bump Lane", redNoBumpLane);
        autoChooser.addOption("Blue No Bump Lane", blueNoBumpLane);
        autoChooser.addOption("Balance Lane Cone", balanceLaneCone);
        autoChooser.addOption("Balance Lane Cube", balanceLaneCube);
        autoChooser.addOption("Taxi Balance Lane Cone", taxiBalanceLaneCone);
        autoChooser.addOption("Taxi Balance Lane Cube", taxiBalanceLaneCube);
        autoChooser.addOption("Red Bump Lane Cone", redBumpLaneCone);
        autoChooser.addOption("Red Bump Lane Cube", redBumpLaneCube);
        autoChooser.addOption("Blue Bump Lane Cone", blueBumpLaneCone);
        autoChooser.addOption("Blue Bump Lane Cube", blueBumpLaneCube);
    }

    public SendableChooser<Command> getAutoChooser() {
        return this.autoChooser; 
    }
}
