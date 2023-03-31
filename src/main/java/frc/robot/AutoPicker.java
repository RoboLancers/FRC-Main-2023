package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.intake.Intake;
import frc.robot.Constants.Arm.ScoringPosition;
import frc.robot.commands.BottomLaneAuto;
import frc.robot.commands.MidLaneAuto;
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
        Command noBumpLane = new TopLaneAuto(drivetrain, arm, gyro, intake, ScoringPosition.HIGH_CONE, ScoringPosition.HIGH_CUBE);
        Command balanceLaneCone = new MidLaneAuto(drivetrain, gyro, arm, intake, ScoringPosition.HIGH_CONE);
        Command balanceLaneCube = new MidLaneAuto(drivetrain, gyro, arm, intake, ScoringPosition.HIGH_CUBE);
        Command bumpLaneCone = new BottomLaneAuto(drivetrain, arm, intake, ScoringPosition.HIGH_CONE);
        Command bumpLaneCube = new BottomLaneAuto(drivetrain, arm, intake, ScoringPosition.HIGH_CUBE);

        autoChooser.addOption("No Bump Lane", noBumpLane);
        autoChooser.addOption("Balance Lane Cone", balanceLaneCone);
        autoChooser.addOption("Balance Lane Cube", balanceLaneCube);
        autoChooser.addOption("Bump Lane Cone", bumpLaneCone);
        autoChooser.addOption("Bump Lane Cube", bumpLaneCube);
    }

    public SendableChooser<Command> getAutoChooser() {
        return this.autoChooser; 
    }
}
