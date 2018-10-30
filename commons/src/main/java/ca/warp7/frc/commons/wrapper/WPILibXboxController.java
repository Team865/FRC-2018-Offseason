package ca.warp7.frc.commons.wrapper;

import edu.wpi.first.wpilibj.XboxController;

@Deprecated
public class WPILibXboxController extends XboxController {
    public WPILibXboxController(int port) {
        super(port);
    }
}
