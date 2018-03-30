package pw.skidrevenant.fiona.checks.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.PlayerUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerQuitEvent.class })
public class Jesus extends Checks
{
    private Map<UUID, Integer> onWater;
    private Map<UUID, Integer> count;
    private Map<UUID, Integer> countB;
    
    public Jesus() {
        super("Jesus", ChecksType.MOVEMENT, Fiona.getAC(), 10, true, true);
        this.onWater = new HashMap<UUID, Integer>();
        this.count = new HashMap<UUID, Integer>();
        this.countB = new HashMap<UUID, Integer>();
        this.setTps(13.0);
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e = (PlayerMoveEvent)event;
            if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ()) {
                return;
            }
            final Player player = e.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (!player.getAllowFlight() && player.getVehicle() == null && player.getLocation().getBlock().isLiquid() && !player.getLocation().getBlock().getType().isSolid() && !PlayerUtils.isReallyOnground(player) && !PlayerUtils.isCompletelyInWater(player.getLocation()) && Fiona.getAC().getPing().getTPS() > this.getTps()) {
                final double yDistance = Math.abs(e.getFrom().getY() - e.getTo().getY());
                final double horizontalDistance = MathUtils.getHorizontalDistance(e.getFrom(), e.getTo());
                int verbose = this.countB.getOrDefault(player.getUniqueId(), 0);
                if (((yDistance == 0.0 && PlayerUtils.isInWater(player.getLocation())) || (!PlayerUtils.isFullyInWater(player.getLocation()) && PlayerUtils.isInWater(player.getLocation()) && yDistance < 0.08) || (!PlayerUtils.isInWater(player.getLocation()) && player.getLocation().clone().subtract(0.0, 0.25, 0.0).getBlock().isLiquid() && yDistance == 0.0)) && horizontalDistance > 0.1) {
                    verbose = ((yDistance == 0.0 && PlayerUtils.isInWater(player.getLocation())) ? (verbose + 2) : (verbose + 1));
                }
                else {
                    verbose = 0;
                }
                if (verbose > 14) {
                    user.setVL(this, user.getVL(this) + 1);
                    user.setCancelled(this, CancelType.MOVEMENT, 1);
                    verbose = 0;
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Hover" + Color.Gray + " Y: " + Color.Green + MathUtils.round(yDistance, 7));
                }
                this.countB.put(player.getUniqueId(), verbose);
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e = (PlayerMoveEvent)event;
            if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ()) {
                return;
            }
            final Player p = e.getPlayer();
            final User user = Fiona.getUserManager().getUser(p.getUniqueId());
            if (p.getAllowFlight() || PlayerUtils.isOnLilyPad(p) || MathUtils.elapsed(user.isTeleported()) < 500L || MathUtils.elapsed(user.isVelocity()) < 1500L || p.getLocation().clone().add(0.0, 0.4, 0.0).getBlock().getType().isSolid() || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            if (user.placedBlock()) {
                user.setPlacedBlock(false);
                return;
            }
            int Count = 0;
            if (this.count.containsKey(p.getUniqueId())) {
                Count = this.count.get(p.getUniqueId());
            }
            if ((PlayerUtils.cantStandAtWater(p.getWorld().getBlockAt(p.getLocation())) || PlayerUtils.cantStandAtLava(p.getWorld().getBlockAt(p.getLocation()))) && PlayerUtils.isHoveringOverWater(p.getLocation()) && !PlayerUtils.isFullyInWater(p.getLocation())) {
                Count += 2;
            }
            else {
                Count = ((Count > 0) ? (Count - 1) : Count);
            }
            if (Count > 20) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.MOVEMENT, 1);
                this.Alert(p, Color.Gray + "Reason: " + Color.Green + "Walking");
                Count = 0;
            }
            this.count.put(p.getUniqueId(), Count);
        }
        else if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e2 = (PlayerQuitEvent)event;
            final Player p = e2.getPlayer();
            if (this.onWater.containsKey(p.getUniqueId())) {
                this.onWater.remove(p.getUniqueId());
            }
            if (this.count.containsKey(p.getUniqueId())) {
                this.count.remove(p.getUniqueId());
            }
            if (this.countB.containsKey(p.getUniqueId())) {
                this.countB.remove(p.getUniqueId());
            }
        }
    }
}
