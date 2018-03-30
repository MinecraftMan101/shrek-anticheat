package pw.skidrevenant.fiona.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;

public class EventPlayerAttack implements Listener
{
    @EventHandler
    public void onAttack(final EntityDamageByEntityEvent e) {
        Fiona.getAC().getChecks().event((Event)e);
        if (e.getEntity() instanceof Player) {
            final Player player = (Player)e.getEntity();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user != null) {
                user.setIsHit(System.currentTimeMillis());
            }
        }
        if (e.getDamager() instanceof Player) {
            final Player player = (Player)e.getDamager();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            user.setLastHitPlayer(e.getEntity());
            if (user.isCancelled() == CancelType.COMBAT && user.getCancelTicks() > 0) {
                e.setCancelled(true);
                user.setCancelTicks(user.getCancelTicks() - 1);
            }
            else if (user.isCancelled() == CancelType.COMBAT) {
                user.setCancelled(null, CancelType.NONE);
            }
        }
    }
}
