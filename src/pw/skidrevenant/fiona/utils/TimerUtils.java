package pw.skidrevenant.fiona.utils;

public class TimerUtils
{
    private long lastMS;
    
    public TimerUtils() {
        this.lastMS = 0L;
    }
    
    public static boolean elapsed(final long from, final long required) {
        return System.currentTimeMillis() - from > required;
    }
    
    public static long nowlong() {
        return System.currentTimeMillis();
    }
    
    public boolean isDelayComplete(final long delay) {
        return System.currentTimeMillis() - this.lastMS >= delay;
    }
    
    public boolean hasReached(final long milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }
    
    public void setLastMS() {
        this.lastMS = System.currentTimeMillis();
    }
    
    public int convertToMS(final int perSecond) {
        return 1000 / perSecond;
    }
    
    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public long getLastMS() {
        return this.lastMS;
    }
    
    public void setLastMS(final long lastMS) {
        this.lastMS = lastMS;
    }
    
    public void reset() {
        this.lastMS = this.getCurrentMS();
    }
}
