package pw.skidrevenant.fiona.checks.movement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerVelocityEvent;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.packets.events.PacketMovementEvent;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.PlayerUtils;

@ChecksListener(events = { PlayerVelocityEvent.class, PacketMovementEvent.class })
public class Velocity extends Checks
{
    private Set<UUID> playersTakingVelocity;
    private Multimap<UUID, Double> playerOffsetMultiMap;
    private Map<UUID, Integer> verbose;
    
    public Velocity() {
        super("Velocity", ChecksType.MOVEMENT, Fiona.getAC(), 4, true, false);
        this.playersTakingVelocity = new HashSet<UUID>();
        this.playerOffsetMultiMap = ArrayListMultimap.create();
        this.verbose = new HashMap<UUID, Integer>();
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerVelocityEvent) {
            final PlayerVelocityEvent e = (PlayerVelocityEvent)event;
            final Player player = e.getPlayer();
            final double velocityY = e.getVelocity().getY();
            if (velocityY < 0.02 || player.getHealth() <= 0.0 || this.playersTakingVelocity.contains(player.getUniqueId()) || PlayerUtils.isInWater(player) || PlayerUtils.isInWeb(player) || player.getLocation().clone().add(0.0, 2.0, 0.0).getBlock().getType().isSolid()) {
                return;
            }
            final int playerPing = Fiona.getAC().getPing().getPing(player);
            if (playerPing < 0) {
                return;
            }
            this.playersTakingVelocity.add(player.getUniqueId());
        }
        if (event instanceof PacketMovementEvent) {
            final PacketMovementEvent e2 = (PacketMovementEvent)event;
            if (e2.getFrom().distance(e2.getTo()) != 0.0 && e2.getFrom().getY() > e2.getTo().getY()) {
                return;
            }
            final Player player = e2.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (this.playersTakingVelocity.contains(player.getUniqueId())) {
                if (user.getPacketsFromLag() > 0.0) {
                    this.playersTakingVelocity.remove(player.getUniqueId());
                    this.playerOffsetMultiMap.removeAll((Object)player.getUniqueId());
                    return;
                }
                final int ping = Fiona.getAC().getPing().getPing(player);
                final long average = user.getMovePacketAverage();
                int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
                final int ticks = this.getFormula(ping, average) * 2;
                final double offsetY = e2.getTo().getY() - e2.getFrom().getY();
                this.playerOffsetMultiMap.put(player.getUniqueId(), offsetY);
                if (this.playerOffsetMultiMap.get(player.getUniqueId()).size() == 5 + ticks) {
                    double totalOffset = 0.0;
                    for (final double offset : this.playerOffsetMultiMap.get(player.getUniqueId())) {
                        totalOffset += offset;
                    }
                    this.playerOffsetMultiMap.removeAll((Object)player.getUniqueId());
                    this.playersTakingVelocity.remove(player.getUniqueId());
                    if (totalOffset < 0.01) {
                        if (++verbose > 3) {
                            user.setVL(this, user.getVL(this) + 1);
                            this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Ignored Server Velocity");
                        }
                    }
                    else {
                        verbose = ((verbose > 0) ? (verbose - 1) : 0);
                    }
                }
                this.verbose.put(player.getUniqueId(), verbose);
            }
        }
    }
    
    private int getFormula(final int ping, final long average) {
        int formula = (int)Math.round((ping / 2 + average / 2L) / 50.0);
        if (formula == 0) {
            formula = 1;
        }
        return formula;
    }
}
