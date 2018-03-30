package pw.skidrevenant.fiona.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Base64;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class MathUtils
{
    public static boolean elapsed(final long from, final long required) {
        return System.currentTimeMillis() - from > required;
    }
    
    public static long elapsed(final long starttime) {
        return System.currentTimeMillis() - starttime;
    }
    
    public static double trim(final int degree, final double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format = String.valueOf(format) + "#";
        }
        final DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d).replaceAll(",", "."));
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static String decrypt(final String strEncrypted) {
        String strData = "";
        try {
            final byte[] decoded = Base64.getDecoder().decode(strEncrypted);
            strData = new String(decoded, "UTF-8") + "\n";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }
    
    public static double offset(final Vector one, final Vector two) {
        return one.subtract(two).length();
    }
    
    public static double getHorizontalDistance(final Location to, final Location from) {
        final double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
        final double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));
        return Math.sqrt(x * x + z * z);
    }
    
    public static float[] getRotations(final Location one, final Location two) {
        final double diffX = two.getX() - one.getX();
        final double diffZ = two.getZ() - one.getZ();
        final double diffY = two.getY() + 2.0 - 0.4 - (one.getY() + 2.0);
        final double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static float getYawChangeToEntity(final Player player, final Entity entity, final Location from, final Location to) {
        final double deltaX = entity.getLocation().getX() - player.getLocation().getX();
        final double deltaZ = entity.getLocation().getZ() - player.getLocation().getZ();
        double yawToEntity;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return Float.valueOf((float)(-getYawDifference(from, to) - yawToEntity));
    }
    
    public static float getPitchChangeToEntity(final Player player, final Entity entity, final Location from, final Location to) {
        final double deltaX = entity.getLocation().getX() - player.getLocation().getX();
        final double deltaZ = entity.getLocation().getZ() - player.getLocation().getZ();
        final double deltaY = player.getLocation().getY() - 1.6 + 2.0 - 0.4 - entity.getLocation().getY();
        final double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return yawTo180F((float)(-(getYawDifference(from, to) - (float)pitchToEntity)));
    }
    
    public static double fixedXAxis(final double x) {
        double touchedX = x;
        final double rem = touchedX - Math.round(touchedX) + 0.01;
        if (rem < 0.3) {
            touchedX = NumberConversions.floor(x) - 1;
        }
        return touchedX;
    }
    
    public static Vector calculateAngle(final Vector player, final Vector otherPlayer) {
        final Vector delta = new Vector(player.getX() - otherPlayer.getX(), player.getY() - otherPlayer.getY(), player.getZ() - otherPlayer.getZ());
        final double hyp = Math.sqrt(delta.getX() * delta.getX() + delta.getY() * delta.getY());
        final Vector angle = new Vector(Math.atan((delta.getZ() + 64.06) / hyp) * 57.295779513082, Math.atan(delta.getY() / delta.getX()) * 57.295779513082, 0.0);
        if (delta.getX() >= 0.0) {
            angle.setY(angle.getY() + 180.0);
        }
        return angle;
    }
    
    public static Vector getRotation(final Location one, final Location two) {
        final double dx = two.getX() - one.getX();
        final double dy = two.getY() - one.getY();
        final double dz = two.getZ() - one.getZ();
        final double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        final float yaw = (float)(Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-Math.atan2(dy, distanceXZ) * 180.0 / 3.141592653589793);
        return new Vector(yaw, pitch, 0.0f);
    }
    
    public static float[] getAngles(final Player player, final Entity e, final Location from, final Location to) {
        return new float[] { (float)(getYawChangeToEntity(player, e, from, to) + getYawDifference(from, to)), (float)(getPitchChangeToEntity(player, e, from, to) + getYawDifference(from, to)) };
    }
    
    public static double getYawDifference(final Location one, final Location two) {
        return Math.abs(one.getYaw() - two.getYaw());
    }
    
    public static double getVerticalDistance(final Location to, final Location from) {
        final double y = Math.abs(Math.abs(to.getY()) - Math.abs(from.getY()));
        return Math.sqrt(y * y);
    }
    
    public static double getYDifference(final Location to, final Location from) {
        return Math.abs(to.getY() - from.getY());
    }
    
    public static Vector getHorizontalVector(final Location loc) {
        final Vector vec = loc.toVector();
        vec.setY(0);
        return vec;
    }
    
    public static Vector getVerticalVector(final Location loc) {
        final Vector vec = loc.toVector();
        vec.setX(0);
        vec.setZ(0);
        return vec;
    }
    
    public static float yawTo180F(float flub) {
        flub %= 360.0f;
        if (flub >= 180.0f) {
            flub -= 360.0f;
        }
        if (flub < -180.0f) {
            flub += 360.0f;
        }
        return flub;
    }
    
    public static double yawTo180D(double dub) {
        dub %= 360.0;
        if (dub >= 180.0) {
            dub -= 360.0;
        }
        if (dub < -180.0) {
            dub += 360.0;
        }
        return dub;
    }
}
