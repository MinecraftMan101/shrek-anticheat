package pw.skidrevenant.fiona.utils;

public class CustomLocation
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private long timeStamp;
    
    public CustomLocation(final double x, final double y, final double z, final float yaw, final float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.timeStamp = System.currentTimeMillis();
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public long getTimeStamp() {
        return this.timeStamp;
    }
}
