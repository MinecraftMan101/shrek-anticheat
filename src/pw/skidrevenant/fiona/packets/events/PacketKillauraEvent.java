package pw.skidrevenant.fiona.packets.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketKillauraEvent extends Event
{
    private Player player;
    private Entity entity;
    private static final HandlerList handlers;
    private PacketTypes type;
    
    public PacketKillauraEvent(final Player player, final Entity entity, final PacketTypes type) {
        this.player = player;
        this.type = type;
        this.entity = entity;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public PacketTypes getType() {
        return this.type;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public HandlerList getHandlers() {
        return PacketKillauraEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PacketKillauraEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
