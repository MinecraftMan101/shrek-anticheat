package pw.skidrevenant.fiona.packets.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketEvent extends Event
{
    private Player player;
    private float yaw;
    private float pitch;
    private PacketTypes type;
    private static final HandlerList handlers;
    
    public PacketEvent(final Player player, final float yaw, final float pitch, final PacketTypes type) {
        this.player = player;
        this.yaw = yaw;
        this.pitch = pitch;
        this.type = type;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public PacketTypes getType() {
        return this.type;
    }
    
    public HandlerList getHandlers() {
        return PacketEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PacketEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
