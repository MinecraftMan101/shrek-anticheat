package pw.skidrevenant.fiona.utils;

import org.bukkit.Location;

public class TrigUtils
{
    public static double getDirection(final Location from, final Location to) {
        if (from == null || to == null) {
            return 0.0;
        }
        final double difX = to.getX() - from.getX();
        final double difZ = to.getZ() - from.getZ();
        return wrapAngleTo180_float((float)(Math.atan2(difZ, difX) * 180.0 / 3.141592653589793) - 90.0f);
    }
    
    public static double getDistance(final double p1, final double p2, final double p3, final double p4) {
        final double delta1 = p3 - p1;
        final double delta2 = p4 - p2;
        return Math.sqrt(delta1 * delta1 + delta2 * delta2);
    }
    
    public static float wrapAngleTo180_float(float value) {
        value %= 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }
    
    public static float fixRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float var4 = wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }
}
