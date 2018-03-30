package pw.skidrevenant.fiona.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.packets.events.PacketEvent;
import pw.skidrevenant.fiona.packets.events.PacketKeepAliveEvent;
import pw.skidrevenant.fiona.packets.events.PacketMovementEvent;
import pw.skidrevenant.fiona.packets.events.PacketTypes;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.MathUtils;

public class EventPacket implements Listener
{
    @EventHandler
    public void packet(final PacketEvent e) {
        Fiona.getAC().getChecks().event(e);
        if (e.getType() == PacketTypes.FLYING) {
            Bukkit.getServer().getPluginManager().callEvent((Event)new PacketMovementEvent(e.getPlayer(), e.getPlayer().getLocation(), e.getPlayer().getLocation()));
        }
        final User user = Fiona.getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null) {
            if (e.getType() == PacketTypes.POSLOOK) {
                user.setLastPosPacket(System.currentTimeMillis());
            }
            final long time = MathUtils.elapsed(user.getLastPacket());
            if (time >= 100L) {
                final long diff = time - 50L;
                user.setPacketsFromLag(user.getPacketsFromLag() + (int)Math.ceil(diff / 50.0));
            }
            else {
                user.setPacketsFromLag(0.0);
            }
            user.setLastPacket(System.currentTimeMillis());
        }
    }
    
    @EventHandler
    public void onKeepAlive(final PacketKeepAliveEvent e) {
        Fiona.getAC().getChecks().event(e);
    }
    
    @EventHandler
    public void regainHealth(final EntityRegainHealthEvent e) {
        Fiona.getAC().getChecks().event((Event)e);
        if (e.getEntity() instanceof Player) {
            final Player player = (Player)e.getEntity();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user != null) {
                user.setLastHeal(System.currentTimeMillis());
            }
        }
    }
    
    @EventHandler
    public void itemConsume(final PlayerItemConsumeEvent event) {
        Fiona.getAC().getChecks().event((Event)event);
    }
    
    @EventHandler
    public void lastDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player)e.getEntity();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user != null) {
                user.setLastDamage(System.currentTimeMillis());
            }
        }
    }
    
    @EventHandler
    public void sneak(final PlayerToggleSneakEvent e) {
        Fiona.getAC().getChecks().event((Event)e);
    }
}
