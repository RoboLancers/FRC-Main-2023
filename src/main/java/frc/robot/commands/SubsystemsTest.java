
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import com.revrobotics.CANSparkMax;
import frc.robot.Constants;

public class SubsystemsTest extends ParallelCommandGroup {
    public CANSparkMax anchorJointMotor, floatingJointMotor, leftFrontWheelMotor, leftMiddleWheelMotor,
        leftBackWheelMotor, rightFrontWheelMotor, rightMiddleWheelMotor,
        rightBackWheelMotor;
     
    public SubsystemsTest() {
        
        /* addCommands(
            new ParallelRaceGroup(RunCommand(() -> this.anchorJointMotor.set(Constants.SubsystemsTest.kPowerToSet)), 
            new WaitCommand(Constants.SubsystemsTest.kWaitTime)),
            new ParallelRaceGroup(RunCommand(() -> this.floatingJointMotor.set(Constants.SubsystemsTest.kPowerToSet)), 
            new WaitCommand(Constants.SubsystemsTest.kWaitTime)),
            new ParallelRaceGroup(RunCommand(() -> this.leftFrontWheelMotor.set(Constants.SubsystemsTest.kPowerToSet)), 
            new WaitCommand(Constants.SubsystemsTest.kWaitTime)),
            new ParallelRaceGroup(RunCommand(() -> this.leftMiddleMotor.set(Constants.SubsystemsTest.kPowerToSet)), 
            new WaitCommand(Constants.SubsystemsTest.kWaitTime)),
            new ParallelRaceGroup(RunCommand(() -> this.leftBackMotor.set(Constants.SubsystemsTest.kPowerToSet)), 
            new WaitCommand(Constants.SubsystemsTest.kWaitTime)),
            new ParallelRaceGroup(RunCommand(() -> this.rightFrontMotor.set(Constants.SubsystemsTest.kPowerToSet)), 
            new WaitCommand(Constants.SubsystemsTest.kWaitTime)),
            new ParallelRaceGroup(RunCommand(() -> this.rightMiddleMotor.set(Constants.SubsystemsTest.kPowerToSet)), 
            new WaitCommand(Constants.SubsystemsTest.kWaitTime)),
            new ParallelRaceGroup(RunCommand(() -> this.rightBackMotor.set(Constants.SubsystemsTest.kPowerToSet)), 
            new WaitCommand(Constants.SubsystemsTest.kWaitTime))
        );
        addRequirements(); */
    }
}