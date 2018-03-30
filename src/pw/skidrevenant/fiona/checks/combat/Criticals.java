package pw.skidrevenant.fiona.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.PlayerUtils;

@ChecksListener(events = { EntityDamageByEntityEvent.class, PlayerQuitEvent.class })
public class Criticals extends Checks
{
    private Map<UUID, Integer> verbose;
    
    public Criticals() {
        super("Criticals", ChecksType.COMBAT, Fiona.getAC(), 4, true, true);
        this.setTps(14.0);
        this.verbose = new HashMap<UUID, Integer>();
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
            if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player) || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            final Player player = (Player)e.getDamager();
            if (player == null) {
                return;
            }
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
            if (System.currentTimeMillis() - user.isVelocity() < 1200L || PlayerUtils.hasSlabsNear(player.getLocation()) || player.getAllowFlight() || PlayerUtils.isOnClimbable(player, 0) || PlayerUtils.isOnClimbable(player, 1) || user.isMovementCancel() || user.isCancelled() == CancelType.MOVEMENT || MathUtils.elapsed(user.isTeleported()) < 500L) {
                return;
            }
            final Location l = player.getLocation().clone();
            l.add(0.0, player.getEyeHeight() + 1.0, 0.0);
            if (PlayerUtils.hasBlocksNear(l)) {
                return;
            }
            if (player.getFallDistance() > 0.0 && user.getRealFallDistance() == 0.0) {
                ++verbose;
            }
            else {
                verbose = ((verbose > 0) ? (verbose - 1) : 0);
            }
            if (verbose > 2) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.COMBAT);
                verbose = 0;
                this.Alert(player, "*");
            }
            this.verbose.put(player.getUniqueId(), verbose);
        }
        else if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e2 = (PlayerQuitEvent)event;
            if (this.verbose.containsKey(e2.getPlayer().getUniqueId())) {
                this.verbose.remove(e2.getPlayer().getUniqueId());
            }
        }
    }
}
