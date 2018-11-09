package ca.warp7.frc.commons.core;

import java.util.function.Supplier;

public class RobotLoader {

    public void registerAutoModes(IAutoMode... modes) {
    }

    public void registerComponents(IComponent... components) {
    }

    public void setControls(Supplier<IControls> supplier) {
    }

    public static XboxControlsState createXboxController(int port) {
        return Robot.getState().createXboxController(port);
    }
}
