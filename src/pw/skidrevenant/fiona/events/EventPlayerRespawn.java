package pw.skidrevenant.fiona.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.user.User;

public class EventPlayerRespawn implements Listener
{
    @EventHandler
    public void respawn(final PlayerRespawnEvent event) {
        Fiona.getAC().getChecks().event((Event)event);
        final User user = Fiona.getUserManager().getUser(event.getPlayer().getUniqueId());
        if (user != null) {
            user.setTeleportedPhase(true);
            user.setLoginMillis(System.currentTimeMillis());
        }
    }
    
    @EventHandler
    public void death(final PlayerDeathEvent event) {
        final User user = Fiona.getUserManager().getUser(event.getEntity().getUniqueId());
        if (user != null) {
            user.setTeleportedPhase(true);
        }
    }
}
