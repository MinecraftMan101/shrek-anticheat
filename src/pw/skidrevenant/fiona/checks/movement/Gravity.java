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
import pw.skidrevenant.fiona.utils.BlockUtils;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerQuitEvent.class })
public class Gravity extends Checks
{
    private Map<UUID, Integer> verbose;
    
    public Gravity() {
        super("Gravity", ChecksType.MOVEMENT, Fiona.getAC(), 10, true, true);
        this.verbose = new HashMap<UUID, Integer>();
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e = (PlayerMoveEvent)event;
            if (e.isCancelled() || e.getFrom().getY() == e.getTo().getY() || e.getFrom().getY() > e.getTo().getY() || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            final Player player = e.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (player.getAllowFlight() || player.getVehicle() != null || user.isCancelled() == CancelType.MOVEMENT || !MathUtils.elapsed(user.isTeleported(), 50L) || user.getBlockTicks() > 0 || user.getPosPackets() > 0 || user.getWaterTicks() > 0 || !MathUtils.elapsed(user.isVelocity(), 250L)) {
                return;
            }
            final double yVelocity = player.getVelocity().getY();
            int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
            if (yVelocity > 0.0) {
                user.setJumpTicks(user.getJumpTicks() + 1);
            }
            else {
                user.setJumpTicks(0);
            }
            final double vector = MathUtils.offset(MathUtils.getVerticalVector(e.getFrom()), MathUtils.getVerticalVector(e.getTo()));
            final double expected = this.getExpectedMotion(user.getJumpTicks(), 0.42);
            if ((Math.abs(vector - expected) > 1.0E-6 || (vector > expected && vector > 0.41)) && (!player.getLocation().getBlock().getType().isSolid() || BlockUtils.isRegularBlock(player.getLocation().getBlock())) && vector != 0.5 && vector != 0.125 && vector != 0.25 && vector != 0.1875 && BlockUtils.isRegularBlock(user.getSetbackLocation().getBlock())) {
                ++verbose;
            }
            else {
                verbose = ((verbose > 0) ? (verbose - 1) : 0);
            }
            if (verbose > 1) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.MOVEMENT, 1);
                verbose = 0;
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Velocity " + Color.Gray + "y: " + Color.Green + vector + Color.Gray + " e: " + Color.Green + expected);
            }
            user.setLastYVelocity(yVelocity);
            this.verbose.put(player.getUniqueId(), verbose);
        }
        else if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e2 = (PlayerQuitEvent)event;
            final UUID uuid = e2.getPlayer().getUniqueId();
            if (this.verbose.containsKey(uuid)) {
                this.verbose.remove(uuid);
            }
        }
    }
    
    public double getExpectedMotion(final double ticks, final double startMotion) {
        return Math.pow(0.98, ticks) * (3.92 + startMotion) - 3.92;
    }
}
