package frc.robot.trajectory;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class TrajectoryCommandsManager {
    private static final TrajectoryCommandsManager instance = new TrajectoryCommandsManager();

    private final Map<String, TrajectoryCommandConfig> commands = new HashMap<>();

    public void registerCommand(String name, Class<? extends Command> clazz, Subsystem[] subsystems, Class<?>... otherParams) {
        commands.put(name, new TrajectoryCommandConfig(clazz, subsystems, otherParams));
    }

    public TrajectoryCommandConfig getCommandConfig(String name) {
        return this.commands.get(name);
    }

    public static TrajectoryCommandsManager getInstance() {
        return instance;
    }


    public static class TrajectoryCommandConfig {
        private final Class<? extends Command> clazz;
        private final Subsystem[] subsystems;

        private final Class<?>[] otherParams;
        public TrajectoryCommandConfig(Class<? extends Command> clazz, Subsystem[] subsystems, Class<?>... otherParams) {
            this.clazz = clazz;
            this.subsystems = subsystems;
            this.otherParams = otherParams;
        }

        // reflection go brr
        public Command createCommand(Object[] parameters) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            Class<?>[] clazzes = new Class<?>[subsystems.length + otherParams.length];
            System.arraycopy(Arrays.stream(subsystems).map(Subsystem::getClass).toArray(), 0, clazzes, 0, subsystems.length);
            System.arraycopy(otherParams, 0, clazzes, subsystems.length, otherParams.length);

            for (int i = 0; i < otherParams.length; i++) {
                Class<?> param = otherParams[i];
                if (!param.isEnum()) continue;
                for (Enum<?> obj : ((Class<Enum<?>>) param).getEnumConstants()) {
                    if (obj.name().equals(parameters[i])) parameters[i] = obj;
                }
            }

            Object[] params = new Object[subsystems.length + parameters.length];
            System.arraycopy(subsystems, 0, params, 0, subsystems.length);
            System.arraycopy(parameters, 0, params, subsystems.length,  parameters.length);
            return clazz.getConstructor(clazzes).newInstance(params);
        }
    }
}
