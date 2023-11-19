package dev.elijuh.minerpvp.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtil {
    public double round(double d, int decimals) {
        double devisor = Math.pow(10, decimals);
        return Math.floor(d * devisor) / devisor;
    }

    public static float clamp(float angle) {
        while (angle < -180.0F) {
            angle += 360.0F;
        }
        while (angle >= 180.0F) {
            angle -= 360.0F;
        }
        return angle;
    }
}
