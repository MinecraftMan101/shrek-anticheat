package pw.skidrevenant.fiona.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pw.skidrevenant.fiona.detections.Checks;

public class FionaFlagEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private Player player;
    private boolean cancelled;
    private Checks check;
    private String info;
    
    public FionaFlagEvent(final Player player, final Checks check, final String info) {
        this.cancelled = false;
        this.player = player;
        this.check = check;
        this.info = info;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Checks getCheck() {
        return this.check;
    }
    
    public String getInfo() {
        return this.info;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public static HandlerList getHandlerList() {
        return FionaFlagEvent.handlers;
    }
    
    public HandlerList getHandlers() {
        return FionaFlagEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
