package pw.skidrevenant.fiona.checks.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.PlayerUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerQuitEvent.class })
public class GroundSpoof extends Checks
{
    private Map<UUID, Integer> verbose;
    
    public GroundSpoof() {
        super("GroundSpoof", ChecksType.MOVEMENT, Fiona.getAC(), 9, true, true);
        this.verbose = new HashMap<UUID, Integer>();
        this.setTps(15.0);
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e = (PlayerQuitEvent)event;
            final UUID uuid = e.getPlayer().getUniqueId();
            if (this.verbose.containsKey(uuid)) {
                this.verbose.remove(uuid);
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e2 = (PlayerMoveEvent)event;
            final Player p = e2.getPlayer();
            final Location from = e2.getFrom().clone();
            final Location to = e2.getTo().clone();
            final double diff = to.toVector().distance(from.toVector());
            int verbose = this.verbose.getOrDefault(p.getUniqueId(), 0);
            if (PlayerUtils.isGliding(p) || p.getGameMode().equals((Object)GameMode.CREATIVE) || p.getVehicle() != null || p.getAllowFlight() || p.getHealth() <= 0.0 || Fiona.getAC().getPing().getTPS() < this.getTps() || PlayerUtils.wasOnSlime(p)) {
                return;
            }
            final User user = Fiona.getUserManager().getUser(p.getUniqueId());
            if (MathUtils.elapsed(user.getLastBlockPlace()) < 1500L || MathUtils.elapsed(user.isHit()) < 1000L || MathUtils.elapsed(user.isTeleported()) < 150L || user.isCancelled() == CancelType.MOVEMENT) {
                return;
            }
            if (p.isOnGround() && diff > 0.8 && !PlayerUtils.isReallyOnground(p)) {
                user.setVL(this, user.getVL(this) + 1);
                this.Alert(p, "Spoofed onGround");
                user.setCancelled(this, CancelType.MOVEMENT);
            }
            if (p.getFallDistance() == 0.0 && !e2.isCancelled() && user.getRealFallDistance() > 3.0 && PlayerUtils.isAir(p) && !PlayerUtils.isReallyOnground(p) && from.getY() > to.getY() && p.getWalkSpeed() < 0.4) {
                ++verbose;
            }
            else {
                verbose = ((verbose > 0) ? (verbose - 1) : 0);
            }
            if (verbose > 2) {
                user.setVL(this, user.getVL(this) + 1);
                verbose = 0;
                this.Alert(p, "Spoofed FallDistance");
                user.setCancelled(this, CancelType.MOVEMENT);
            }
            this.verbose.put(p.getUniqueId(), verbose);
        }
    }
}
