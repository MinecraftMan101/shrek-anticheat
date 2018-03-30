package pw.skidrevenant.fiona.checks.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.BlockUtils;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.PlayerUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerQuitEvent.class })
public class Fly extends Checks
{
    public Map<UUID, Double> verbose;
    private Map<UUID, Double> glideVerbose;
    private Map<UUID, Double> velocity;
    private Map<UUID, Integer> ladderVerbose;
    private boolean invalid;
    private boolean jumpHeight;
    private boolean glide;
    private boolean speed;
    
    public Fly() {
        super("Fly", ChecksType.MOVEMENT, Fiona.getAC(), 15, true, true);
        this.invalid = true;
        this.jumpHeight = true;
        this.glide = true;
        this.speed = true;
        this.verbose = new HashMap<UUID, Double>();
        this.velocity = new HashMap<UUID, Double>();
        this.glideVerbose = new HashMap<UUID, Double>();
        this.ladderVerbose = new HashMap<UUID, Integer>();
        this.invalid = Fiona.getAC().getConfig().getBoolean("checks.Fly.Invalid");
        this.glide = Fiona.getAC().getConfig().getBoolean("checks.Fly.FallSpeed");
        this.jumpHeight = Fiona.getAC().getConfig().getBoolean("checks.Fly.JumpHeight");
        this.speed = Fiona.getAC().getConfig().getBoolean("checks.Fly.Speed");
        this.setTps(14.0);
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e = (PlayerQuitEvent)event;
            final Player p = e.getPlayer();
            final UUID uuid = p.getUniqueId();
            if (this.verbose.containsKey(uuid)) {
                this.verbose.remove(uuid);
            }
            if (this.velocity.containsKey(uuid)) {
                this.velocity.remove(uuid);
            }
            if (this.glideVerbose.containsKey(uuid)) {
                this.glideVerbose.remove(uuid);
            }
            if (this.ladderVerbose.containsKey(uuid)) {
                this.ladderVerbose.remove(uuid);
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e2 = (PlayerMoveEvent)event;
            final Player player = e2.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (e2.isCancelled() || user.isCancelled() == CancelType.MOVEMENT || player.getAllowFlight() || PlayerUtils.isGliding(player) || player.getVehicle() != null || MathUtils.elapsed(user.getLastBlockPlace()) < 1000L || Fiona.getAC().getPing().getTPS() < this.getTps() || !MathUtils.elapsed(user.isHit(), 1400L) || PlayerUtils.hasPotionEffect(player, PotionEffectType.JUMP)) {
                return;
            }
            if (e2.getFrom().getY() < e2.getTo().getY() && this.speed && MathUtils.elapsed(user.isTeleported(), 100L) && MathUtils.elapsed(user.isVelocity(), 1400L)) {
                final double speed = MathUtils.offset(MathUtils.getVerticalVector(e2.getFrom()), MathUtils.getVerticalVector(e2.getTo()));
                if (speed > 0.5 && !player.getLocation().getBlock().getType().isSolid()) {
                    user.setVL(this, user.getVL(this) + 1);
                    user.setCancelled(this, CancelType.MOVEMENT, 1);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Speed " + Color.Gray + "Y: " + MathUtils.round(speed, 6));
                }
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e2 = (PlayerMoveEvent)event;
            final Player player = e2.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (e2.isCancelled() || user.isCancelled() == CancelType.MOVEMENT || player.getAllowFlight() || PlayerUtils.isGliding(player) || player.getVehicle() != null || MathUtils.elapsed(user.getLastBlockPlace()) < 1000L || Fiona.getAC().getPing().getTPS() < this.getTps() || !MathUtils.elapsed(user.isHit(), 1000L)) {
                return;
            }
            final double Speed = MathUtils.offset(MathUtils.getVerticalVector(e2.getFrom()), MathUtils.getVerticalVector(e2.getTo()));
            if (this.glide) {
                double glideVerbose = this.glideVerbose.getOrDefault(player.getUniqueId(), 0.0);
                if (!PlayerUtils.isInWeb(player) && PlayerUtils.isAir(player) && ((!PlayerUtils.isOnGround(player.getLocation()) || (BlockUtils.isFence(player.getLocation().clone().subtract(0.0, 1.2, 0.0).getBlock()) && PlayerUtils.isOnGround(player.getLocation().clone().subtract(0.0, 0.5, 0.0).getBlock().getLocation()))) & !PlayerUtils.isOnGround(player.getLocation(), -1.2)) && user.getAirTicks() > 30 && e2.getFrom().getY() - e2.getTo().getY() > 0.0 && Speed < 1.0 && !PlayerUtils.hasBlocksNear(player)) {
                    ++glideVerbose;
                }
                else {
                    glideVerbose = ((glideVerbose > -10.0) ? (glideVerbose - 1.0) : -10.0);
                }
                if (glideVerbose > 17.0) {
                    user.setVL(this, user.getVL(this) + 1);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Fall Speed");
                    user.setCancelled(this, CancelType.MOVEMENT);
                    glideVerbose = 0.0;
                }
                this.glideVerbose.put(player.getUniqueId(), glideVerbose);
            }
            if (this.invalid && !PlayerUtils.isOnGroundFP(player) && !PlayerUtils.isOnGround(player.getLocation(), 0.4, -0.6) && !PlayerUtils.isOnGround(player.getLocation(), 0.4, -1.5) && !PlayerUtils.isOnGround(player.getLocation().subtract(0.0, 1.0, 0.0)) && !PlayerUtils.isOnGroundVanilla(player) && user.getAirTicks() > 35 && Math.abs(e2.getFrom().getY() - e2.getTo().getY()) < 0.069 && !player.hasPotionEffect(PotionEffectType.JUMP) && user.getWaterTicks() == 0 && !PlayerUtils.isOnLilyPad(player)) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.MOVEMENT, 1);
                if (user.getVL(this) > 2) {
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Invalid " + Color.Gray + " Type: " + Color.Green + "High" + Color.Gray + " T: " + Color.Green + user.getAirTicks());
                }
            }
            else if (this.invalid && !PlayerUtils.isOnGroundFP(player) && !PlayerUtils.isOnGround(player.getLocation()) && !PlayerUtils.isOnGround(player.getLocation(), 0.4, -0.6) && !PlayerUtils.isOnGroundVanilla(player) && Math.abs(e2.getFrom().getY() - e2.getTo().getY()) < 0.05 && user.getAirTicks() > 60 && user.getWaterTicks() == 0 && !PlayerUtils.isOnLilyPad(player)) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.MOVEMENT);
                if (user.getVL(this) > 2) {
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Invalid " + Color.Gray + " Type: " + Color.Green + "Low" + Color.Gray + " T: " + Color.Green + user.getAirTicks());
                }
            }
            if (this.jumpHeight) {
                if (e2.getFrom().getY() > e2.getTo().getY()) {
                    return;
                }
                double TotalBlocks = this.verbose.getOrDefault(player.getUniqueId(), 0.0);
                final double OffsetY = MathUtils.offset(MathUtils.getVerticalVector(e2.getFrom()), MathUtils.getVerticalVector(e2.getTo()));
                if (OffsetY > 0.0) {
                    TotalBlocks += OffsetY;
                }
                double Limit = 2.45;
                if (user.getLastVelocity().getY() > 0.0) {
                    TotalBlocks = ((TotalBlocks > 0.0) ? (TotalBlocks - user.getLastVelocity().getY()) : 0.0);
                }
                if (PlayerUtils.isOnGroundFP(player) || PlayerUtils.isOnGroundVanilla(player) || PlayerUtils.isReallyOnground(player) || PlayerUtils.isOnGroundFP2(player) || MathUtils.elapsed(user.getLastBlockPlace()) <= 1500L || user.getGroundTicks() > 0 || user.getWaterTicks() > 2) {
                    TotalBlocks = 0.0;
                }
                if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                    for (final PotionEffect effect : player.getActivePotionEffects()) {
                        if (effect.getType().equals((Object)PotionEffectType.JUMP)) {
                            final int level = effect.getAmplifier() + 1;
                            Limit += Math.pow(level + 4.2, 2.0) / 16.0 + 0.4;
                            break;
                        }
                    }
                }
                if (PlayerUtils.wasOnSlime(player)) {
                    Limit += user.getRealFallDistance() / 1.5;
                }
                if (TotalBlocks > Limit) {
                    user.setVL(this, user.getVL(this) + 1);
                    user.setCancelled(this, CancelType.MOVEMENT, 3);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Ascension " + Color.Gray + "Blocks: " + Color.Green + MathUtils.trim(2, TotalBlocks));
                }
                this.verbose.put(player.getUniqueId(), TotalBlocks);
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e2 = (PlayerMoveEvent)event;
            final Player player = e2.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user == null || e2.getFrom().getY() == e2.getTo().getY() || !PlayerUtils.isOnClimbable(player, 0) || !PlayerUtils.isOnClimbable(player, 1)) {
                return;
            }
            int verbose = this.ladderVerbose.getOrDefault(player.getUniqueId(), 0);
            final double max = 0.17;
            final double speed2 = MathUtils.offset(MathUtils.getVerticalVector(e2.getFrom()), MathUtils.getVerticalVector(e2.getTo()));
            if (speed2 > max) {
                verbose = ((Math.abs(speed2 - max) > 0.28) ? (verbose + 2) : (verbose + 1));
            }
            else {
                verbose = 0;
            }
            if (verbose > 10) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.MOVEMENT, 2);
                verbose = 6;
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Ladder " + Color.Gray + "S: " + Color.Green + MathUtils.round(speed2, 6));
            }
            this.ladderVerbose.put(player.getUniqueId(), verbose);
        }
    }
    
    public double getNextYMove(final double yMove) {
        return (yMove - 0.08) * 0.98;
    }
}
