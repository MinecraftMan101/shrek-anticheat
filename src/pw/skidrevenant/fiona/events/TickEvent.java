package pw.skidrevenant.fiona.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TickEvent extends Event
{
    private static final HandlerList handlers;
    private TickType type;
    
    public static HandlerList getHandlerList() {
        return TickEvent.handlers;
    }
    
    public TickEvent(final TickType type) {
        this.type = type;
    }
    
    public TickType getType() {
        return this.type;
    }
    
    public HandlerList getHandlers() {
        return TickEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
