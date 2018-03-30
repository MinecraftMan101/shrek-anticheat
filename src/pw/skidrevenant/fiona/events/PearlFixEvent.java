package pw.skidrevenant.fiona.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PearlFixEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private PearlFixType type;
    private Player player;
    private boolean cancelled;
    
    public PearlFixEvent(final Player player, final PearlFixType type) {
        this.cancelled = false;
        this.player = player;
        this.type = type;
    }
    
    public PearlFixType getType() {
        return this.type;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public static HandlerList getHandlerList() {
        return PearlFixEvent.handlers;
    }
    
    public HandlerList getHandlers() {
        return PearlFixEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
