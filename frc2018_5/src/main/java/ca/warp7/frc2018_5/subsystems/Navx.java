package ca.warp7.frc2018_5.subsystems;

import ca.warp7.frc.core.IComponent;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Navx implements IComponent {
    private AHRS ahrs;

    @Override
    public void onConstruct() {
        ahrs = new AHRS(SPI.Port.kMXP);
    }

    AHRS getAhrs() {
        return ahrs;
    }
}
