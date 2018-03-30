package pw.skidrevenant.fiona.checks.other;

import java.util.Collections;
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
import pw.skidrevenant.fiona.packets.events.PacketKeepAliveEvent;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;

@ChecksListener(events = { PacketKeepAliveEvent.class, PlayerQuitEvent.class })
public class PingSpoof extends Checks
{
    public Map<UUID, Integer> violations;
    
    public PingSpoof() {
        super("PingSpoof", ChecksType.OTHER, Fiona.getAC(), 4, true, false);
        this.violations = new HashMap<UUID, Integer>();
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e = (PlayerQuitEvent)event;
            final Player player = e.getPlayer();
            player.getUniqueId();
        }
        if (event instanceof PacketKeepAliveEvent) {
            final PacketKeepAliveEvent e2 = (PacketKeepAliveEvent)event;
            final Player player = e2.getPlayer();
            final int ping = Fiona.getAC().getPing().getPing(player);
            int verbose = this.violations.getOrDefault(player.getUniqueId(), 0);
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (Fiona.getAC().getPing().getTPS() < 17.2) {
                return;
            }
            if (e2.getType() == PacketKeepAliveEvent.PacketKeepAliveType.CLIENT) {
                if (user.packets.size() >= 4) {
                    Collections.sort(user.packets);
                    final long one = user.packets.get(0);
                    final long two = user.packets.get(user.packets.size() - 1);
                    this.debug("Dif: " + Math.abs(one - two));
                    if (Math.abs(Math.abs(one - two) - user.getLastPingSpoofDifference()) <= 2L) {
                        ++verbose;
                        this.debug("Verbose (+1): " + verbose);
                    }
                    else {
                        verbose = ((verbose > 0) ? (verbose - 1) : verbose);
                    }
                    user.setLastPingSpoofDifference(Math.abs(one - two));
                    user.packets.clear();
                }
                if (verbose > 2) {
                    user.setVL(this, user.getVL(this) + 1);
                    user.setCancelled(this, CancelType.MOVEMENT);
                    this.Alert(player, "Reason: " + Color.Green + "Packet Modification " + Color.Gray + "Spoofed Ping: " + Color.Green + ping);
                }
                user.packets.add(MathUtils.elapsed(user.getLastClientKeepAlive()));
                user.setLastClientKeepAlive(System.currentTimeMillis());
            }
            if (e2.getType() == PacketKeepAliveEvent.PacketKeepAliveType.SERVER) {
                user.setLastServerKeepAlive(System.currentTimeMillis());
                if (MathUtils.elapsed(user.getLoginMIllis()) < 4000L) {
                    return;
                }
                if (MathUtils.elapsed(user.getLastClientKeepAlive()) > 10000L) {
                    user.setVL(this, user.getVL(this) + 1);
                    user.setCancelled(this, CancelType.MOVEMENT);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "NoClient " + Color.Gray + "Spoofed Ping: " + Color.Green + ping);
                }
            }
            this.violations.put(player.getUniqueId(), verbose);
        }
    }
}
