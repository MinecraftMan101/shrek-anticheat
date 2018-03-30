package pw.skidrevenant.fiona.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pw.skidrevenant.fiona.detections.Checks;

public class FionaPunishEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private Player player;
    private boolean cancelled;
    private Checks check;
    
    public FionaPunishEvent(final Player player, final Checks check) {
        this.cancelled = false;
        this.player = player;
        this.check = check;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Checks getCheck() {
        return this.check;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public static HandlerList getHandlerList() {
        return FionaPunishEvent.handlers;
    }
    
    public HandlerList getHandlers() {
        return FionaPunishEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
