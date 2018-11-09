package ca.warp7.frc.commons;

public interface Unit {
    interface Hertz {
        static double toSeconds(int hertz) {
            return 1 / (double) hertz;
        }
    }

    interface Degrees {
        static double toRadians(double degrees) {
            return degrees / 180.0 * Math.PI;
        }
    }
}
