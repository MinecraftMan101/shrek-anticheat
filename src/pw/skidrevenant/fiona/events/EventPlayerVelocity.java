package pw.skidrevenant.fiona.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.PlayerUtils;

public class EventPlayerVelocity implements Listener
{
    @EventHandler
    public void onMove(final PlayerVelocityEvent event) {
        Fiona.getAC().getChecks().event((Event)event);
        final User user = Fiona.getUserManager().getUser(event.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }
        if (PlayerUtils.hasPotionEffect(event.getPlayer(), PotionEffectType.POISON) || MathUtils.elapsed(user.isHit()) < 100L) {
            final Vector vector = new Vector(Math.abs(event.getVelocity().getX()) * 10.0, Math.abs(event.getVelocity().getY()) * 5.0, Math.abs(event.getVelocity().getZ()) * 10.0);
            user.setLastVelocity(vector);
            user.setTookVelocity(System.currentTimeMillis());
        }
    }
}
