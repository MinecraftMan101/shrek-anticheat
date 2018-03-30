package pw.skidrevenant.fiona.checks.combat;

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
import pw.skidrevenant.fiona.packets.events.PacketEvent;
import pw.skidrevenant.fiona.packets.events.PacketTypes;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PacketEvent.class, PlayerQuitEvent.class })
public class AimPattern extends Checks {
    private Map<UUID, Integer> aimCVerbose;
    private Map<UUID, Integer> memeVerbose;
    private Map<UUID, Integer> differenceVerbose;
    private boolean meme;
    private boolean impossiblePitch;
    private boolean difference;
    
    public AimPattern() {
        super("AimPattern", ChecksType.COMBAT, Fiona.getAC(), 10, true, false);
        this.setTps(15.0);
        this.aimCVerbose = new HashMap<UUID, Integer>();
        this.memeVerbose = new HashMap<UUID, Integer>();
        this.differenceVerbose = new HashMap<UUID, Integer>();
        this.meme = Fiona.getAC().getConfig().getBoolean("checks.AimPattern.Meme");
        this.impossiblePitch = Fiona.getAC().getConfig().getBoolean("checks.AimPattern.ImpossiblePitch");
        this.difference = Fiona.getAC().getConfig().getBoolean("checks.AimPattern.Difference");
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e = (PlayerQuitEvent)event;
            final Player player = e.getPlayer();
            final UUID uuid = player.getUniqueId();
            if (this.aimCVerbose.containsKey(uuid)) {
                this.aimCVerbose.remove(uuid);
            }
            if (this.differenceVerbose.containsKey(uuid)) {
                this.differenceVerbose.remove(uuid);
            }
            if (this.memeVerbose.containsKey(uuid)) {
                this.memeVerbose.remove(uuid);
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e2 = (PlayerMoveEvent)event;
            if (e2.getPlayer() == null) {
                return;
            }
            if (this.meme) {
                final User user = Fiona.getUserManager().getUser(e2.getPlayer().getUniqueId());
                if (e2.getFrom().getYaw() == e2.getTo().getYaw() || Fiona.getAC().getPing().getTPS() < this.getTps() || user.isCancelled() == CancelType.MOVEMENT || MathUtils.elapsed(user.isTeleported()) < 200L || user.isMovementCancel()) {
                    return;
                }
                final double yawDelta = Math.abs(e2.getFrom().getYaw() - e2.getTo().getYaw());
                int verbose = this.memeVerbose.getOrDefault(e2.getPlayer().getUniqueId(), 0);
                if (yawDelta > 0.0 && yawDelta < 360.0) {
                    if (yawDelta % 1.0 == 0.0 && yawDelta >= 5.0) {
                        ++verbose;
                    } else {
                        --verbose;
                    }
                }
                if (verbose > 3) {
                    user.setVL(this, user.getVL(this) + 1);
                    verbose = 0;
                    user.setCancelled(this, CancelType.MOVEMENT);
                    this.Alert(e2.getPlayer(), Color.Gray + "Reason: " + Color.Red + "Experimental Meme");
                }
                this.memeVerbose.put(e2.getPlayer().getUniqueId(), verbose);
            }
        }
        if (event instanceof PacketEvent) {
            final PacketEvent e3 = (PacketEvent)event;
            if (this.difference || this.impossiblePitch) {
                if ((e3.getType() != PacketTypes.POSLOOK && e3.getType() != PacketTypes.LOOK) || e3.getPitch() < -80.0f || e3.getPitch() > 80.0f || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                    return;
                }
                final Player player = e3.getPlayer();
                if (player == null) {
                    return;
                }
                final User user2 = Fiona.getUserManager().getUser(player.getUniqueId());
                if (System.currentTimeMillis() - user2.isTeleported() < 300L || user2.getPosPackets() > 0 || user2.isCancelled() == CancelType.MOVEMENT || user2.isMovementCancel() || user2.getPacketsFromLag() > 0.0 || player.getAllowFlight() || player.getVehicle() != null) {
                    return;
                }
                int verbose2 = this.aimCVerbose.getOrDefault(player.getUniqueId(), 0);
                int difVerbose = this.differenceVerbose.getOrDefault(player.getUniqueId(), 0);
                final double pitchDifference = Math.abs(e3.getPitch() - user2.getLastPitchAimC());
                final double yaw = Math.abs(MathUtils.yawTo180D(e3.getYaw()));
                final double lastYaw = Math.abs(MathUtils.yawTo180D(user2.getLastYawAim()));
                final double yawDifference = Math.abs(yaw - lastYaw);
                final double differenceOfDifference = yawDifference % user2.getLastYawDifference();
                if (this.impossiblePitch) {
                    if (Math.abs(pitchDifference - user2.getLastPitchDifferenceAimC()) < 9.0E-6 && Math.abs(e3.getYaw() - user2.getLastYawAim()) > 4.5 && !MathUtils.elapsed(user2.getLastPacket(), 100L)) {
                        ++verbose2;
                    } else {
                        verbose2 = 0;
                    }
                    if (verbose2 > 32) {
                        user2.setVL(this, user2.getVL(this) + 1);
                        user2.setCancelled(this, CancelType.MOVEMENT);
                        verbose2 = 0;
                        this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Impossible Pitch Movement");
                    }
                }
                if (this.difference) {
                    if (differenceOfDifference < 0.01) {
                        ++difVerbose;
                    } else {
                        difVerbose = ((difVerbose > 0) ? (difVerbose - 1) : 0);
                    }
                    if (difVerbose > 20) {
                        user2.setVL(this, user2.getVL(this) + 1);
                        difVerbose = 0;
                        this.Alert(player, Color.Gray + "Reason: " + Color.Red + "Difference");
                    }
                }
                this.aimCVerbose.put(player.getUniqueId(), verbose2);
                this.differenceVerbose.put(player.getUniqueId(), difVerbose);
                user2.setLastPitchDifferenceAimC(pitchDifference);
                user2.setLastPitchAimC(e3.getPitch());
                user2.setLastYawDifference(yawDifference);
                user2.setLastYawAim(yaw);
            }
        }
    }
}
