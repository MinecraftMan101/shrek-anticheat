package pw.skidrevenant.fiona.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.user.User;

public class EventJoinQuit implements Listener
{
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        Fiona.getAC();
        Fiona.getUserManager().add(new User(p));
        final User user = Fiona.getUserManager().getUser(p.getUniqueId());
        user.setLoginMillis(System.currentTimeMillis());
        user.setLoginTicks(4);
        if (user.isStaff() && !user.isHasAlerts()) {
            user.setHasAlerts(true);
            p.sendMessage(Fiona.getAC().getMessage().ALERTS_JOIN);
        }
        Fiona.getAC().getChecks().event((Event)e);
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        Fiona.getAC();
        Fiona.getUserManager().remove(Fiona.getUserManager().getUser(p.getUniqueId()));
        Fiona.getAC().getChecks().event((Event)e);
    }
}
