package pw.skidrevenant.fiona.packets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketKeepAliveEvent extends Event
{
    public Player player;
    private static final HandlerList handlers;
    private PacketKeepAliveType type;
    
    public PacketKeepAliveEvent(final Player player, final PacketKeepAliveType type) {
        this.player = player;
        this.type = type;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public PacketKeepAliveType getType() {
        return this.type;
    }
    
    public HandlerList getHandlers() {
        return PacketKeepAliveEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PacketKeepAliveEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
    
    public enum PacketKeepAliveType
    {
        SERVER, 
        CLIENT;
    }
}
