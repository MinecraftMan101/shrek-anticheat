package pw.skidrevenant.fiona.checks.combat;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.checks.movement.Phase;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.packets.events.PacketEvent;
import pw.skidrevenant.fiona.packets.events.PacketKillauraEvent;
import pw.skidrevenant.fiona.packets.events.PacketTypes;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.BlockUtils;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.PlayerUtils;
import pw.skidrevenant.fiona.utils.TimerUtils;
import pw.skidrevenant.fiona.utils.TrigUtils;

@ChecksListener(events = { EntityDamageByEntityEvent.class, PacketKillauraEvent.class, PlayerMoveEvent.class, PlayerQuitEvent.class, PacketEvent.class, BlockBreakEvent.class })
public class KillAuraA extends Checks
{
    private Map<UUID, Integer> count;
    private Map<UUID, Integer> aimVerbose;
    private Map<UUID, Long> directionHit;
    private Map<UUID, Double> yawDif;
    private Map<UUID, Location> location;
    private Map<UUID, Map.Entry<Integer, Long>> multiVerbose;
    private Map<UUID, Player> lastHit2;
    private Map<UUID, Integer> posVerbose;
    private boolean invalidSwing;
    private boolean switchAura;
    private boolean direction;
    private boolean angle;
    private boolean heuristic;
    private boolean rayTrace;
    
    public KillAuraA() {
        super("KillAura", ChecksType.COMBAT, Fiona.getAC(), 14, true, false);
        this.count = new HashMap<UUID, Integer>();
        this.aimVerbose = new HashMap<UUID, Integer>();
        this.yawDif = new HashMap<UUID, Double>();
        this.directionHit = new HashMap<UUID, Long>();
        this.location = new HashMap<UUID, Location>();
        this.multiVerbose = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.lastHit2 = new HashMap<UUID, Player>();
        this.posVerbose = new HashMap<UUID, Integer>();
        this.setTps(15.6);
        this.invalidSwing = Fiona.getAC().getConfig().getBoolean("checks.KillAura.InvalidSwing");
        this.direction = Fiona.getAC().getConfig().getBoolean("checks.KillAura.Direction");
        this.angle = Fiona.getAC().getConfig().getBoolean("checks.KillAura.Angle");
        this.heuristic = Fiona.getAC().getConfig().getBoolean("checks.KillAura.Heuristic");
        this.switchAura = Fiona.getAC().getConfig().getBoolean("checks.KillAura.Switch");
        this.rayTrace = Fiona.getAC().getConfig().getBoolean("checks.KillAura.RayTrace");
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PacketKillauraEvent) {
            final PacketKillauraEvent e = (PacketKillauraEvent)event;
            if (this.invalidSwing) {
                if (Fiona.getAC().getPing().getTPS() < this.getTps()) {
                    return;
                }
                final Player player = e.getPlayer();
                if (player == null) {
                    return;
                }
                final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                if (e.getType() == PacketTypes.SWING) {
                    user.addSwingPackets();
                }
                else if (e.getType() == PacketTypes.USE) {
                    user.addUsePackets();
                }
                if ((user.getUsePackets() > user.getSwingPackets() && user.getSwingPackets() == 2) || (user.getUsePackets() - user.getSwingPackets() > 2 && user.getUsePackets() > user.getSwingPackets())) {
                    user.setVL(this, user.getVL(this) + 1);
                    user.setCancelled(this, CancelType.COMBAT);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Invalid Swing " + Color.Gray + "Ping: " + Fiona.getAC().getPing().getPing(player));
                }
                if (user.getSwingPackets() > 3 && user.getUsePackets() > 3) {
                    user.resetSwingPackets();
                    user.resetUsePackets();
                }
            }
        }
        if (event instanceof PacketKillauraEvent) {
            final PacketKillauraEvent e = (PacketKillauraEvent)event;
            if (e.getType() != PacketTypes.USE || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            if (this.switchAura && e.getEntity() instanceof Player) {
                final Player player = e.getPlayer();
                if (player == null) {
                    return;
                }
                Player lastHit;
                final Player damaged = lastHit = (Player)e.getEntity();
                int verbose = 0;
                long Time = 0L;
                if (this.multiVerbose.containsKey(player.getUniqueId())) {
                    verbose = this.multiVerbose.get(player.getUniqueId()).getKey();
                    Time = this.multiVerbose.get(player.getUniqueId()).getValue();
                }
                if (this.lastHit2.containsKey(player.getUniqueId())) {
                    lastHit = this.lastHit2.get(player.getUniqueId());
                }
                if (damaged != lastHit) {
                    if (System.currentTimeMillis() - Time < 50L) {
                        verbose += 2;
                    }
                    else {
                        verbose = ((verbose > 0) ? (verbose - 1) : 0);
                    }
                    Time = System.currentTimeMillis();
                }
                if (verbose > 10) {
                    final User user2 = Fiona.getUserManager().getUser(player.getUniqueId());
                    verbose = 0;
                    user2.setVL(this, user2.getVL(this) + 1);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Switch " + Color.Gray + "Ping: " + Fiona.getAC().getPing().getPing(player));
                }
                this.multiVerbose.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(verbose, Time));
                this.lastHit2.put(player.getUniqueId(), damaged);
            }
        }
        if (event instanceof PacketEvent) {
            final PacketEvent e2 = (PacketEvent)event;
            if (this.direction) {
                final Player player = e2.getPlayer();
                if (player == null) {
                    return;
                }
                final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                final double difference = Math.abs(System.currentTimeMillis() - user.getLastDirPosPacket());
                user.setLastDirPosPacket(System.currentTimeMillis());
                if (difference > 65.0 && difference < 5000.0) {
                    user.setDirectionViolations(user.getDirectionViolations() - 5.0);
                }
                if (user.getDirectionViolations() < -10.0) {
                    user.setDirectionViolations(-10.0);
                }
            }
        }
        if (event instanceof PacketKillauraEvent) {
            final PacketKillauraEvent e = (PacketKillauraEvent)event;
            if (e.getType() != PacketTypes.USE || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            if (this.direction && e.getEntity() instanceof Player) {
                final Player player = e.getPlayer();
                if (player == null) {
                    return;
                }
                final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                final double aimValue = Math.abs(TrigUtils.getDirection(player.getLocation(), e.getEntity().getLocation()));
                final double yawValue = Math.abs(TrigUtils.wrapAngleTo180_float(player.getLocation().getYaw()));
                final double difference2 = Math.abs(aimValue - yawValue);
                final double deltaCombined = user.getDeltaXZ();
                if (difference2 > 30.0 && MathUtils.getHorizontalDistance(player.getLocation(), e.getEntity().getLocation()) > 1.0 && deltaCombined <= 0.6) {
                    user.setDirectionViolations(user.getDirectionViolations() + 2.0);
                    if (user.getDirectionViolations() > 25.0) {
                        user.setVL(this, user.getVL(this) + 1);
                        this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Direction " + Color.Gray + "Ping: " + Fiona.getAC().getPing().getPing(player) + Color.Gray + " Data: " + Color.Green + difference2 + ", " + deltaCombined + ", " + MathUtils.trim(6, MathUtils.getHorizontalDistance(player.getLocation(), e.getEntity().getLocation())));
                        user.setDirectionViolations(0.0);
                    }
                }
                else {
                    user.setDirectionViolations(user.getDirectionViolations() - 1.0);
                }
            }
        }
        if (event instanceof PacketKillauraEvent) {
            final PacketKillauraEvent e = (PacketKillauraEvent)event;
            final Player player = e.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user == null || player.getAllowFlight()) {
                return;
            }
            int verbose2 = this.posVerbose.getOrDefault(player.getUniqueId(), 0);
            final long difference3 = Math.abs(user.getLastPosPacket() - System.currentTimeMillis());
            if (difference3 < 10L) {
                verbose2 += 10;
            }
            else {
                verbose2 = ((verbose2 > -4) ? (verbose2 - 1) : 0);
            }
            this.debug(MathUtils.elapsed(user.getLastPosPacket()) + ", " + player.getLocation().getPitch());
            if (verbose2 > 30) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.COMBAT);
                verbose2 = 0;
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Position " + Color.Gray + "D: " + Color.Green + difference3);
            }
            this.posVerbose.put(player.getUniqueId(), verbose2);
        }
        if (event instanceof PacketKillauraEvent) {
            final PacketKillauraEvent e = (PacketKillauraEvent)event;
            final Player player = e.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user == null || player.getAllowFlight()) {
                return;
            }
            int verbose2 = user.getFluxAACVerbose();
            final long difference3 = Math.abs(user.getLastPosPacket() - System.currentTimeMillis());
            if (Math.abs(user.getPacketPosDifference() - difference3 - 17L) <= 1L || Math.abs(user.getPacketPosDifference() - difference3 + 38L) <= 1L || Math.abs(user.getPacketPosDifference() - difference3 - 21L) <= 1L || Math.abs(user.getPacketPosDifference() - difference3 + 26L) <= 1L) {
                verbose2 += 4;
            }
            else {
                verbose2 = ((verbose2 > 0) ? (verbose2 - 1) : 0);
            }
            if (verbose2 > 14) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.COMBAT);
                verbose2 = 0;
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Specific " + Color.Gray + "t: " + Color.Green + "Flux");
            }
            user.setPacketPosDifference(difference3);
            user.setFluxAACVerbose(verbose2);
        }
        if (event instanceof PacketKillauraEvent) {
            final PacketKillauraEvent e = (PacketKillauraEvent)event;
            if (e.getType() != PacketTypes.USE || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            if (this.angle) {
                final Player player = e.getPlayer();
                if (player == null) {
                    return;
                }
                final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                if (!TimerUtils.elapsed(this.directionHit.getOrDefault(player.getUniqueId(), 0L), 51L) && e.getEntity().getLocation().toVector().subtract(player.getLocation().toVector()).normalize().dot(player.getLocation().getDirection()) > 0.97) {
                    user.setVL(this, user.getVL(this) + 1);
                    user.setCancelled(this, CancelType.COMBAT);
                    user.setCancelled(this, CancelType.MOVEMENT);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Angle " + Color.Gray + "Ping: " + Fiona.getAC().getPing().getPing(player));
                }
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e3 = (PlayerMoveEvent)event;
            if (e3.getPlayer() == null) {
                return;
            }
            if (this.direction && e3.getFrom().getYaw() != e3.getTo().getYaw() && getYawDifference(e3.getFrom(), e3.getTo()) >= 77.5 && Fiona.getAC().getPing().getTPS() > this.getTps()) {
                this.directionHit.put(e3.getPlayer().getUniqueId(), System.currentTimeMillis());
            }
        }
        if (event instanceof PacketKillauraEvent) {
            final PacketKillauraEvent e = (PacketKillauraEvent)event;
            if (e.getType() != PacketTypes.USE || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            if (this.heuristic) {
                final Player player = e.getPlayer();
                if (player == null) {
                    return;
                }
                final Entity entity = e.getEntity();
                final User user3 = Fiona.getUserManager().getUser(player.getUniqueId());
                final Location location = this.location.getOrDefault(player.getUniqueId(), player.getLocation());
                int verbose3 = this.aimVerbose.getOrDefault(player.getUniqueId(), 0);
                if ((Math.abs(Math.abs(MathUtils.yawTo180D(MathUtils.getRotations(location, entity.getLocation())[0])) - Math.abs(MathUtils.yawTo180D(location.getYaw()))) < 2.0 && MathUtils.getYawDifference(player.getLocation(), location) > 6.0) || (Math.abs(Math.abs(MathUtils.yawTo180D(MathUtils.getRotations(location, entity.getLocation())[0])) - Math.abs(MathUtils.yawTo180D(location.getYaw()))) < 4.0 && MathUtils.getYawDifference(player.getLocation(), location) > 30.0) || (Math.abs(entity.getVelocity().length()) > 0.1 && user3.getDeltaXZ() > 0.1 && Math.abs(Math.abs(MathUtils.yawTo180D(MathUtils.getRotations(location, entity.getLocation())[0])) - Math.abs(MathUtils.yawTo180D(location.getYaw()))) < 7.0 && MathUtils.getYawDifference(player.getLocation(), location) > Math.abs(12.0 - 1.5 * player.getLocation().distance(entity.getLocation()))) || (Math.abs(Math.abs(MathUtils.yawTo180D(MathUtils.getRotations(location, entity.getLocation())[0]))) - Math.abs(MathUtils.yawTo180D(location.getYaw())) < 8.0 && Math.abs(MathUtils.yawTo180D(Math.round(user3.getLastKillauraYaw())) - MathUtils.yawTo180D(Math.round(player.getLocation().getYaw()))) == 12.0)) {
                    verbose3 = ((Math.abs(MathUtils.getRotations(location, entity.getLocation())[0] - location.getYaw()) < 1.0 || (Math.abs(entity.getVelocity().length()) > 0.1 && user3.getDeltaXZ() > 0.2)) ? (verbose3 + 5) : (verbose3 + 4));
                }
                else if (verbose3 > 0) {
                    verbose3 = ((MathUtils.getYawDifference(player.getLocation(), location) > 5.0) ? (verbose3 - 8) : (verbose3 - 1));
                }
                if (verbose3 > 50) {
                    user3.setVL(this, user3.getVL(this) + 1);
                    verbose3 = 0;
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Heuristic " + Color.Gray + "Y: " + Color.Green + MathUtils.round(Math.abs(MathUtils.yawTo180D(location.getYaw())), 3) + Color.Gray + " E: " + Color.Green + MathUtils.round(Math.abs(MathUtils.yawTo180D(MathUtils.getRotations(location, entity.getLocation())[0])), 3));
                }
                this.location.put(player.getUniqueId(), player.getLocation());
                this.aimVerbose.put(player.getUniqueId(), verbose3);
                user3.setLastKillauraYaw(player.getLocation().getYaw());
            }
        }
        if (event instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e4 = (EntityDamageByEntityEvent)event;
            if (!this.rayTrace || !(e4.getDamager() instanceof Player) || !(e4.getEntity() instanceof Player) || e4.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            final Player player = (Player)e4.getDamager();
            if (player == null) {
                return;
            }
            final Player damaged = (Player)e4.getEntity();
            if (player.getAllowFlight() || player.getLocation().distance(damaged.getLocation()) > 5.0 || player.getLocation().distance(damaged.getLocation()) < 1.2 || player.getLocation().getBlockY() != damaged.getLocation().getBlockY()) {
                return;
            }
            final User user3 = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user3.getBlockCancelled() > 0) {
                user3.setBlockCancelled(user3.getBlockCancelled() - 1);
                return;
            }
            int i = 0;
            for (int x = Math.min(player.getLocation().getBlockX(), damaged.getLocation().getBlockX()); x <= Math.max(player.getLocation().getBlockX(), damaged.getLocation().getBlockX()); ++x) {
                for (int z = Math.min(player.getLocation().getBlockZ(), damaged.getLocation().getBlockZ()); z <= Math.max(player.getLocation().getBlockZ(), damaged.getLocation().getBlockZ()); ++z) {
                    for (int y = Math.min(player.getLocation().getBlockY(), damaged.getLocation().getBlockY()); y <= Math.max(player.getLocation().getBlockY(), damaged.getLocation().getBlockY()) + 1; ++y) {
                        final Block block = new Location(player.getWorld(), (double)x, (double)y, (double)z).getBlock();
                        if (block.getType().isSolid() && !BlockUtils.isSlab(block) && !BlockUtils.isFence(block) && !BlockUtils.isPiston(block) && !Phase.allowed.contains(block.getType()) && !BlockUtils.isChest(block) && !BlockUtils.isStair(block)) {
                            ++i;
                        }
                    }
                }
            }
            if (i >= 2 && !player.hasLineOfSight((Entity)damaged) && PlayerUtils.getOffsetOffCursor(player, (LivingEntity)damaged) < 10.0) {
                user3.setVL(this, user3.getVL(this) + 1);
                user3.setCancelled(this, CancelType.COMBAT, 1);
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "RayTrace");
            }
        }
        if (event instanceof BlockBreakEvent) {
            final BlockBreakEvent e5 = (BlockBreakEvent)event;
            if (e5.getPlayer() == null) {
                return;
            }
            if (e5.isCancelled() && this.rayTrace) {
                final User user4 = Fiona.getUserManager().getUser(e5.getPlayer().getUniqueId());
                if (user4 != null) {
                    user4.setBlockCancelled(10);
                }
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e3 = (PlayerMoveEvent)event;
            if (e3.getPlayer() == null) {
                return;
            }
            final double yawDif = Math.abs(e3.getFrom().getYaw() - e3.getTo().getYaw());
            this.yawDif.put(e3.getPlayer().getUniqueId(), yawDif);
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e6 = (PlayerQuitEvent)event;
            final Player p = e6.getPlayer();
            final UUID uuid = p.getUniqueId();
            if (this.count.containsKey(uuid)) {
                this.count.remove(uuid);
            }
            else if (this.directionHit.containsKey(uuid)) {
                this.directionHit.remove(uuid);
            }
            else if (this.yawDif.containsKey(uuid)) {
                this.yawDif.remove(uuid);
            }
            else if (this.lastHit2.containsKey(uuid)) {
                this.lastHit2.remove(uuid);
            }
            else if (this.multiVerbose.containsKey(uuid)) {
                this.multiVerbose.remove(uuid);
            }
        }
    }
    
    private static float getYawDifference(final Location location, final Location location2) {
        final float f = getYaw(location);
        final float f2 = getYaw(location2);
        float f3 = Math.abs(f - f2);
        if ((f < 90.0f && f2 > 270.0f) || (f2 < 90.0f && f > 270.0f)) {
            f3 -= 360.0f;
        }
        return Math.abs(f3);
    }
    
    public static float getYaw(final Location location) {
        float f = (location.getYaw() - 90.0f) % 360.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        return f;
    }
}
