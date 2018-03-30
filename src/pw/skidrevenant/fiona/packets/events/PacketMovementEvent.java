package pw.skidrevenant.fiona.packets.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketMovementEvent extends Event
{
    private Player player;
    private Location from;
    private Location to;
    private static final HandlerList handlers;
    
    public PacketMovementEvent(final Player player, final Location from, final Location to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Location getFrom() {
        return this.from;
    }
    
    public Location getTo() {
        return this.to;
    }
    
    public HandlerList getHandlers() {
        return PacketMovementEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PacketMovementEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
