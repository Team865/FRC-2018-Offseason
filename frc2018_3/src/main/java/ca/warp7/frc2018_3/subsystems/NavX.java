package ca.warp7.frc2018_3.subsystems;

import ca.warp7.frc.commons.core.IComponent;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class NavX implements IComponent {
    private AHRS ahrs;

    @Override
    public void onConstruct() {
        ahrs = new AHRS(SPI.Port.kMXP);
    }

    AHRS getAhrs() {
        return ahrs;
    }
}
