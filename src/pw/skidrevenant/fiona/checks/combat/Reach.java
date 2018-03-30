package pw.skidrevenant.fiona.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.packets.events.PacketKillauraEvent;
import pw.skidrevenant.fiona.packets.events.PacketTypes;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.PlayerUtils;

@ChecksListener(events = { PlayerMoveEvent.class, EntityDamageByEntityEvent.class, PlayerQuitEvent.class, PacketKillauraEvent.class })
public class Reach extends Checks
{
    private Map<UUID, Integer> count;
    private Map<UUID, Integer> reachBVerbose;
    private boolean counted;
    private boolean useEntity;
    
    public Reach() {
        super("Reach", ChecksType.COMBAT, Fiona.getAC(), 5, true, true);
        this.count = new HashMap<UUID, Integer>();
        this.reachBVerbose = new HashMap<UUID, Integer>();
        this.counted = Fiona.getAC().getConfig().getBoolean("checks.Reach.Counted");
        this.useEntity = Fiona.getAC().getConfig().getBoolean("checks.Reach.UseEntity");
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
            if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK || !(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player) || !this.counted) {
                return;
            }
            final Player damager = (Player)e.getDamager();
            final Player player = (Player)e.getEntity();
            if (damager == null) {
                return;
            }
            final double yDif = Math.abs(PlayerUtils.getEyeLocation(damager).getY() - PlayerUtils.getEyeLocation(player).getY());
            final double Reach = MathUtils.trim(2, PlayerUtils.getEyeLocation(damager).distance(player.getEyeLocation()) - yDif);
            if (Fiona.getAC().getPing().getTPS() < this.getTps() + 0.5 || damager.getAllowFlight() || player.getAllowFlight() || Fiona.getAC().getPing().getPing(player) > 550 || Fiona.getAC().getPing().getPing(damager) > 550) {
                return;
            }
            int Count = this.count.getOrDefault(damager.getUniqueId(), 0);
            double MaxReach = 3.0;
            final double YawDifference = Math.abs(180.0f - Math.abs(damager.getLocation().getYaw() - player.getLocation().getYaw()));
            final User user = Fiona.getUserManager().getUser(damager.getUniqueId());
            MaxReach += ((YawDifference > 100.0 && player.getVelocity().getY() < 0.2) ? (YawDifference * 0.01) : (YawDifference * 0.001));
            MaxReach += 0.05 / user.getDeltaXZ();
            MaxReach += Math.abs(player.getVelocity().length()) * 2.5;
            MaxReach += ((damager.getWalkSpeed() <= 0.2) ? 0.0 : (damager.getWalkSpeed() - 0.2));
            final int PingD = Fiona.getAC().getPing().getPing(damager);
            final int PingP = Fiona.getAC().getPing().getPing(player);
            MaxReach += (PingD + PingP) / 2 * 0.0024;
            if (Reach > MaxReach) {
                Count += 2;
            }
            else {
                Count = ((Count > 0) ? (Count - 1) : Count);
            }
            if (Count > 12 && Reach > MaxReach && PingD >= 0) {
                user.setVL(this, user.getVL(this) + 1);
                this.Alert(damager, Color.Gray + "Reason: " + Color.Green + "Counted " + Color.Gray + "Ping: " + Color.Green + Fiona.getAC().getPing().getPing(damager) + Color.Gray + " Reach: " + Color.Green + Reach + Color.Gray + " > " + Color.Green + MaxReach);
            }
            this.count.put(damager.getUniqueId(), Count);
        }
        if (event instanceof PacketKillauraEvent) {
            final PacketKillauraEvent e2 = (PacketKillauraEvent)event;
            if (e2.getType() != PacketTypes.USE || !(e2.getEntity() instanceof Player) || !this.useEntity || Fiona.getAC().getPing().getTPS() < 17.0) {
                return;
            }
            final Player player2 = e2.getPlayer();
            if (player2 == null) {
                return;
            }
            final User user2 = Fiona.getUserManager().getUser(player2.getUniqueId());
            final Player entity = (Player)e2.getEntity();
            if (player2.getAllowFlight() || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            final double reach = MathUtils.getHorizontalDistance(player2.getLocation(), entity.getLocation()) - MathUtils.getYDifference(player2.getLocation(), entity.getLocation());
            double maxReach = 3.2;
            final double playerPing = Fiona.getAC().getPing().getPing(player2);
            final double entityPing = Fiona.getAC().getPing().getPing(entity);
            final double deltaXZ = user2.getDeltaXZ();
            final double entityDeltaXZ = (entity.getVelocity().length() > 0.0) ? entity.getVelocity().length() : 0.0;
            int verbose = this.reachBVerbose.getOrDefault(player2.getUniqueId(), 0);
            maxReach += (playerPing + entityPing) * 0.004;
            maxReach += (deltaXZ + entityDeltaXZ) * 0.9;
            maxReach += Math.abs(player2.getEyeLocation().getYaw() - entity.getLocation().getYaw()) * 0.004;
            if (reach > maxReach) {
                ++verbose;
            }
            else {
                verbose = ((verbose > -2) ? (verbose - 1) : verbose);
            }
            if (verbose > 7 && playerPing >= 0.0) {
                user2.setVL(this, user2.getVL(this) + 1);
                verbose = 0;
                this.Alert(player2, Color.Gray + "Reason: " + Color.Green + "UseEntity " + Color.Gray + "Ping: " + Color.Green + Fiona.getAC().getPing().getPing(player2) + Color.Gray + " Reach: " + Color.Green + reach + Color.Gray + " > " + Color.Green + maxReach);
            }
            this.reachBVerbose.put(player2.getUniqueId(), verbose);
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e3 = (PlayerQuitEvent)event;
            if (this.count.containsKey(e3.getPlayer().getUniqueId())) {
                this.count.remove(e3.getPlayer().getUniqueId());
            }
            if (this.reachBVerbose.containsKey(e3.getPlayer().getUniqueId())) {
                this.reachBVerbose.remove(e3.getPlayer().getUniqueId());
            }
        }
    }
}
