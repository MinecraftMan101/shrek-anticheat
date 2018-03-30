package pw.skidrevenant.fiona.checks.other;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;

@ChecksListener(events = { BlockPlaceEvent.class, PlayerQuitEvent.class })
public class Scaffold extends Checks
{
    private Map<UUID, Integer> verbose;
    private Map<UUID, Integer> verboseB;
    private Map<UUID, Integer> verboseC;
    private Map<UUID, Integer> verboseD;
    
    public Scaffold() {
        super("Scaffold", ChecksType.OTHER, Fiona.getAC(), 2, true, true);
        this.verbose = new HashMap<UUID, Integer>();
        this.verboseB = new HashMap<UUID, Integer>();
        this.verboseC = new HashMap<UUID, Integer>();
        this.verboseD = new HashMap<UUID, Integer>();
        this.setTps(10.0);
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
            if (this.verboseB.containsKey(uuid)) {
                this.verboseB.remove(uuid);
            }
            if (this.verboseC.containsKey(uuid)) {
                this.verboseC.remove(uuid);
            }
            if (this.verboseD.containsKey(uuid)) {
                this.verboseD.remove(uuid);
            }
        }
        if (event instanceof BlockPlaceEvent) {
            final BlockPlaceEvent e2 = (BlockPlaceEvent)event;
            if (Fiona.getAC().getPing().getTPS() < this.getTps() || e2.isCancelled() || e2.getPlayer().getAllowFlight()) {
                return;
            }
            final Player player = e2.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            final double yaw = player.getLocation().getYaw();
            final double pitch = player.getLocation().getPitch();
            int verbose = this.verboseB.getOrDefault(player.getUniqueId(), 0);
            int verboseC = this.verboseC.getOrDefault(player.getUniqueId(), 0);
            final int verboseD = this.verboseD.getOrDefault(player.getUniqueId(), 0);
            final double yawDif = Math.abs(yaw - user.getLastScaffoldYaw());
            final double pitchDif = Math.abs(pitch - user.getLastScaffoldPitch());
            if (user.getLastBlockPlaced() == null || user.getLastBlockPlaced().getWorld() != e2.getBlockPlaced().getWorld()) {
                return;
            }
            if (yawDif > 14.0 && pitchDif < 3.2 && player.getLocation().getY() - e2.getBlockPlaced().getY() >= 1.0) {
                ++verbose;
            }
            else {
                verbose = ((verbose > 0) ? (verbose - 1) : 0);
            }
            if (verbose > 3) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.BLOCK);
                verbose = 0;
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Pattern " + Color.Gray + "P: " + Color.Green + MathUtils.round(Math.abs(pitch - user.getLastScaffoldPitch()), 3) + Color.Gray + " Y: " + Color.Green + MathUtils.round(Math.abs(yaw - user.getLastScaffoldYaw()), 3));
            }
            if (player.getLocation().getPitch() < 69.0 && e2.getBlockAgainst().getType().isSolid() && MathUtils.elapsed(user.getLastBlockPlace()) < 500L && player.getLocation().getY() - e2.getBlockPlaced().getY() >= 1.0 && !player.getLocation().clone().subtract(0.0, 2.0, 0.0).getBlock().getType().isSolid()) {
                ++verboseC;
            }
            else {
                verboseC = ((verbose > 0) ? (verbose - 1) : 0);
            }
            if (verboseC > 3) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.BLOCK);
                verboseC = 0;
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Impossible Pitch " + Color.Gray + "P: " + Color.Green + MathUtils.round(player.getLocation().getPitch(), 4) + Color.Gray + " Y: " + Color.Green + MathUtils.round(yawDif, 4));
            }
            user.setVeryLastScaffoldYaw(Math.abs(yaw - user.getLastScaffoldYaw()));
            user.setVeryLastScaffoldPitch(Math.abs(pitch - user.getLastScaffoldPitch()));
            user.setLastScaffoldYaw(yaw);
            user.setLastScaffoldPitch(pitch);
            this.verboseB.put(player.getUniqueId(), verbose);
            this.verboseC.put(player.getUniqueId(), verboseC);
            this.verboseD.put(player.getUniqueId(), verboseD);
        }
        if (event instanceof BlockPlaceEvent) {
            final BlockPlaceEvent e2 = (BlockPlaceEvent)event;
            final Player player = e2.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            final long scaffoldDifference = Math.abs(user.getLastScaffoldPlace() - System.currentTimeMillis());
            int verbose2 = this.verbose.getOrDefault(player.getUniqueId(), 0);
            if (!e2.getBlockPlaced().getLocation().clone().subtract(0.0, 1.0, 0.0).getBlock().getType().isSolid()) {
                if (user.scaffoldDifferences.size() >= 3) {
                    Collections.sort(user.scaffoldDifferences);
                    final long one = user.scaffoldDifferences.get(0);
                    final long two = user.scaffoldDifferences.get(user.scaffoldDifferences.size() - 1);
                    if (Math.abs(Math.abs(one - two) - user.getLastScaffoldDifference()) < 3L && player.getLocation().getY() - e2.getBlockPlaced().getLocation().getY() >= 1.0) {
                        verbose2 += 3;
                    }
                    else {
                        verbose2 = ((verbose2 > 0) ? (verbose2 - 1) : verbose2);
                    }
                    user.setLastScaffoldDifference(Math.abs(one - two));
                    user.scaffoldDifferences.clear();
                }
                if (verbose2 > 6) {
                    user.setVL(this, user.getVL(this) + 1);
                    verbose2 = 0;
                    user.setCancelled(this, CancelType.BLOCK);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Time " + Color.Gray + "V: " + Color.Green + Math.abs(scaffoldDifference - user.getLastScaffoldDifference()));
                }
                if (scaffoldDifference > 5L && scaffoldDifference < 300L) {
                    user.scaffoldDifferences.add(scaffoldDifference);
                }
                user.setLastScaffoldDifference(scaffoldDifference);
                user.setLastScaffoldPlace(System.currentTimeMillis());
                this.verbose.put(player.getUniqueId(), verbose2);
            }
        }
    }
}
