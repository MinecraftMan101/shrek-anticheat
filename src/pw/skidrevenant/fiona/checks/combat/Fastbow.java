package pw.skidrevenant.fiona.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.MathUtils;

@ChecksListener(events = { ProjectileLaunchEvent.class, PlayerInteractEvent.class, PlayerQuitEvent.class })
public class Fastbow extends Checks
{
    public Map<UUID, Integer> verbose;
    
    public Fastbow() {
        super("Fastbow", ChecksType.COMBAT, Fiona.getAC(), 5, true, true);
        this.verbose = new HashMap<UUID, Integer>();
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof ProjectileLaunchEvent) {
            final ProjectileLaunchEvent e = (ProjectileLaunchEvent)event;
            if (e.getEntity() instanceof Arrow) {
                final Arrow arrow = (Arrow)e.getEntity();
                if (!(arrow.getShooter() instanceof Player) || arrow.getShooter() == null || arrow.getVelocity().length() < 0.12 || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                    return;
                }
                final Player player = (Player)arrow.getShooter();
                final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                final long threshold = Math.round((arrow.getVelocity().length() > 1.2) ? (280.0 * arrow.getVelocity().length()) : (140.0 * arrow.getVelocity().length()));
                int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
                if (MathUtils.elapsed(user.getLastBow()) < threshold) {
                    ++verbose;
                    if (Fiona.getAC().getConfig().getBoolean("checks." + this.getName() + ".cancelled")) {
                        e.setCancelled(true);
                    }
                }
                else {
                    verbose = ((verbose > -2) ? (verbose - 2) : verbose);
                }
                if (verbose > 4) {
                    user.setVL(this, user.getVL(this) + 1);
                    verbose = 0;
                    this.Alert(player, "*");
                }
                this.verbose.put(player.getUniqueId(), verbose);
            }
        }
        else if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e2 = (PlayerQuitEvent)event;
            if (this.verbose.containsKey(e2.getPlayer().getUniqueId())) {
                this.verbose.remove(e2.getPlayer().getUniqueId());
            }
        }
    }
}
