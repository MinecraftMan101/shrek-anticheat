package pw.skidrevenant.fiona.checks.combat;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.packets.events.PacketEvent;
import pw.skidrevenant.fiona.packets.events.PacketTypes;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.TimerUtils;

@ChecksListener(events = { PacketEvent.class, PlayerQuitEvent.class })
public class ZeroPatterns extends Checks
{
    private Map<UUID, Map.Entry<Double, Double>> difference;
    
    public ZeroPatterns() {
        super("ZeroPatterns", ChecksType.COMBAT, Fiona.getAC(), 12, true, false);
        this.difference = new HashMap<UUID, Map.Entry<Double, Double>>();
        this.setTps(18.0);
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e = (PlayerQuitEvent)event;
            final Player player = e.getPlayer();
            if (this.difference.containsKey(player.getUniqueId())) {
                this.difference.remove(player.getUniqueId());
            }
        }
        if (event instanceof PacketEvent) {
            final PacketEvent e2 = (PacketEvent)event;
            if (e2.getType() != PacketTypes.POSLOOK || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            final User user = Fiona.getUserManager().getUser(e2.getPlayer().getUniqueId());
            if (!TimerUtils.elapsed(user.getLoginMIllis(), 1500L) || e2.getPlayer().getVehicle() != null || user.getLoginTicks() > 0 || System.currentTimeMillis() - user.isTeleported() < 1200L || user.getPosPackets() > 0 || user.isMovementCancel() || user.isCancelled() == CancelType.MOVEMENT || Math.abs(System.currentTimeMillis() - user.getLastPacket()) > 100L || e2.getPlayer().getLocation().getBlock().getType().isSolid() || e2.getPlayer().getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid() || !MathUtils.elapsed(user.getLastBlockPlace(), 1000L)) {
                return;
            }
            final double yaw = e2.getYaw();
            final double pitch = e2.getPitch();
            final double yawdif = this.difference.containsKey(e2.getPlayer().getUniqueId()) ? (yaw - this.difference.get(e2.getPlayer().getUniqueId()).getKey()) : 0.0;
            final double pitchdif = this.difference.containsKey(e2.getPlayer().getUniqueId()) ? (pitch - this.difference.get(e2.getPlayer().getUniqueId()).getValue()) : 0.0;
            if (user.getLastYaw() == -0.0 && yawdif == -0.0 && pitchdif == 0.0) {
                user.setVL(this, user.getVL(this) + 1);
                this.Alert(e2.getPlayer(), "Experimental");
            }
            this.difference.put(e2.getPlayer().getUniqueId(), new AbstractMap.SimpleEntry<Double, Double>(yaw, pitch));
            user.setLastYaw(yawdif);
        }
    }
}
