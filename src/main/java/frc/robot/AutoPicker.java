package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.gyro.Gyro;
import frc.robot.subsystems.intake.Intake;
import frc.robot.util.InstantiatorCommand;
import frc.robot.Constants.Arm.ScoringPosition;
import frc.robot.commands.BottomLaneAuto;
import frc.robot.commands.MidLaneAuto;
import frc.robot.commands.TopLaneAuto;

public class AutoPicker {

    private SendableChooser<Command> autoChooser = new SendableChooser<>(); 
    private SendableChooser<ScoringPosition> scorePosition1Chooser = new SendableChooser<>(); 
    private SendableChooser<ScoringPosition> scorePosition2Chooser = new SendableChooser<>(); 

    private Drivetrain drivetrain; 
    private Arm arm; 
    private Intake intake; 
    private Gyro gyro; 

    public AutoPicker(Drivetrain drivetrain, Arm arm, Gyro gyro, Intake intake) {
        this.drivetrain = drivetrain; 
        this.arm = arm; 
        this.gyro = gyro; 
        this.intake = intake;
        
        configureScorePositions(scorePosition1Chooser);
        configureScorePositions(scorePosition2Chooser);
        
        configureAutos();
    }

    private void configureScorePositions(SendableChooser<ScoringPosition> chooser) {
        chooser.addOption("High Cone", ScoringPosition.HIGH_CONE);
        chooser.addOption("Mid Cone", ScoringPosition.MID_CONE);
        chooser.addOption("Low Cone", ScoringPosition.LOW_CONE);
        chooser.addOption("High Cube", ScoringPosition.HIGH_CUBE);
        chooser.addOption("Mid Cube", ScoringPosition.MID_CUBE);
        chooser.addOption("Low Cube", ScoringPosition.LOW_CUBE);
    }

    private void configureAutos() {
        // ! Pick one of these in comp
        autoChooser.addOption("No Bump Auto", new InstantiatorCommand(() -> new TopLaneAuto(drivetrain, arm, gyro, intake, scorePosition1Chooser.getSelected(), scorePosition2Chooser.getSelected())));
        autoChooser.addOption("Mid Auto", new InstantiatorCommand(() -> new MidLaneAuto(drivetrain, gyro, arm, intake, scorePosition1Chooser.getSelected())));
        autoChooser.addOption("Bump Auto", new InstantiatorCommand(() -> new BottomLaneAuto(drivetrain, arm, intake, scorePosition1Chooser.getSelected())));
    }

    public SendableChooser<Command> getAutoChooser() {
        return this.autoChooser; 
    }

    public SendableChooser<ScoringPosition> getScoringPosition1() {
        return scorePosition1Chooser; 
    }

    public SendableChooser<ScoringPosition> getScoringPosition2() {
        return scorePosition2Chooser; 
    }
}
