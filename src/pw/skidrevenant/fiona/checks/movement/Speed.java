package pw.skidrevenant.fiona.checks.movement;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

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
public class Speed extends Checks
{
    private Map<UUID, Double> verbose;
    public Location location;
    private boolean normalMovements;
    private boolean limit;
    private Set<Double> normalValues;
    private boolean constant;
    private boolean single;
    
    public Speed() {
        super("Speed", ChecksType.MOVEMENT, Fiona.getAC(), 15, true, true);
        this.normalMovements = true;
        this.limit = true;
        this.constant = true;
        this.single = true;
        this.setTps(16.5);
        this.verbose = new HashMap<UUID, Double>();
        this.normalValues = new HashSet<Double>();
        this.normalMovements = Fiona.getAC().getConfig().getBoolean("checks.Speed.NormalMovements");
        this.limit = Fiona.getAC().getConfig().getBoolean("checks.Speed.Limit");
        this.constant = Fiona.getAC().getConfig().getBoolean("checks.Speed.Constant");
        this.single = Fiona.getAC().getConfig().getBoolean("checks.Speed.Others");
        this.normalValues.add(0.41999998688697815);
        this.normalValues.add(0.33319999363422426);
        this.normalValues.add(0.18672884460831);
        this.normalValues.add(0.4044491418477924);
        this.normalValues.add(0.4044449141847757);
        this.normalValues.add(0.40444491418477746);
        this.normalValues.add(0.24813599859094637);
        this.normalValues.add(0.19123230896968835);
        this.normalValues.add(0.1647732812606676);
        this.normalValues.add(0.240068658430082);
        this.normalValues.add(0.20000004768370516);
        this.normalValues.add(0.10900766491188207);
        this.normalValues.add(0.20000004768371227);
        this.normalValues.add(0.40444491418477924);
        this.normalValues.add(0.0030162615090425504);
        this.normalValues.add(0.05999999821186108);
        this.normalValues.add(0.05199999886751172);
        this.normalValues.add(0.06159999881982792);
        this.normalValues.add(0.06927999889612124);
        this.normalValues.add(0.07542399904870933);
        this.normalValues.add(0.07532994414328797);
        this.normalValues.add(0.08033919924402255);
        this.normalValues.add(0.5);
        this.normalValues.add(0.08427135945886555);
        this.normalValues.add(0.340000110268593);
        this.normalValues.add(0.30000001192092896);
        this.normalValues.add(0.3955758986732967);
        this.normalValues.add(0.019999999105930755);
        this.normalValues.add(0.210001587867816);
        this.normalValues.add(0.13283301814746876);
        this.normalValues.add(0.05193025879327907);
        this.normalValues.add(0.1875);
        this.normalValues.add(0.375);
        this.normalValues.add(0.08307781780646728);
        this.normalValues.add(0.125);
        this.normalValues.add(0.25);
        this.normalValues.add(0.01250004768371582);
        this.normalValues.add(0.1176000022888175);
        this.normalValues.add(0.0625);
        this.normalValues.add(0.20000004768371582);
        this.normalValues.add(0.4044448882341385);
        this.normalValues.add(0.40444491418477835);
        this.normalValues.add(0.019999999105934307);
        this.normalValues.add(0.4375);
        this.normalValues.add(0.36510663985490055);
        this.normalValues.add(0.4641593749554431);
        this.normalValues.add(0.3841593618424213);
        this.normalValues.add(0.2000000476837016);
        this.normalValues.add(0.011929668006757765);
        this.normalValues.add(0.4053654548823289);
        this.normalValues.add(0.07840000152587834);
        this.normalValues.add(0.40444491418477213);
        this.normalValues.add(0.019999999105920097);
        this.normalValues.add(0.365374439108057);
        this.normalValues.add(0.3955758986732931);
        this.normalValues.add(0.395575898673286);
        this.normalValues.add(0.39557589867329757);
        this.normalValues.add(0.39557589867330023);
        this.normalValues.add(0.200000047683627);
        this.normalValues.add(0.019999999105927202);
        this.normalValues.add(0.365374439108057);
        this.normalValues.add(0.36537445162271354);
        this.normalValues.add(0.3434175188903765);
        this.normalValues.add(0.3884175057773547);
        this.normalValues.add(0.38497228309773845);
        this.normalValues.add(0.44091960879393355);
        this.normalValues.add(0.3125);
        this.normalValues.add(0.2000000476835737);
        this.normalValues.add(0.0125);
        this.normalValues.add(0.020999999657277613);
        this.normalValues.add(0.24813599859093927);
        this.normalValues.add(0.23488000000000397);
        this.normalValues.add(0.1647732818260721);
        this.normalValues.add(0.08307781780646906);
        this.normalValues.add(0.2149722962107603);
        this.normalValues.add(0.3959196219069554);
        this.normalValues.add(0.08741708767762191);
        this.normalValues.add(0.09736117292808899);
        this.normalValues.add(0.09999003227396486);
        this.normalValues.add(0.09587683198483887);
        this.normalValues.add(0.0998549303276377);
        this.normalValues.add(0.0991353100824881);
        this.normalValues.add(0.09999489725122146);
        this.normalValues.add(0.021599999713899365);
        this.normalValues.add(0.24813599859095348);
        this.normalValues.add(0.4453744695041024);
        this.normalValues.add(0.4702911683122011);
        this.normalValues.add(0.38726399420357893);
        this.normalValues.add(0.4844449272978011);
        this.normalValues.add(0.019600000673562046);
        this.normalValues.add(0.4199430141446925);
        this.normalValues.add(0.005999999105924303);
        this.normalValues.add(0.4392891105552792);
        this.normalValues.add(0.36537445639108057);
        this.normalValues.add(0.5625);
        this.normalValues.add(0.20000004768370871);
        this.normalValues.add(0.3400000110268593);
        this.normalValues.add(0.015625);
        this.normalValues.add(0.015555072702198913);
        this.normalValues.add(0.07840000152589255);
        this.normalValues.add(0.15523200451659136);
        this.normalValues.add(0.03584062504455687);
        this.normalValues.add(0.04659999847412166);
        this.normalValues.add(0.3733999884128565);
        this.normalValues.add(0.2000000476836732);
        this.normalValues.add(0.2000000476836874);
        this.normalValues.add(0.07840000152587923);
        this.normalValues.add(0.0548933470320776);
        this.normalValues.add(0.01636799395751609);
        this.normalValues.add(0.03158248110962347);
        this.normalValues.add(0.015555072702206019);
        this.normalValues.add(0.15523200451660557);
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e = (PlayerMoveEvent)event;
            final Player player = e.getPlayer();
            if (e.getTo().getX() == e.getFrom().getX() && e.getTo().getY() == e.getFrom().getY() && e.getTo().getZ() == e.getFrom().getZ()) {
                return;
            }
            if (this.limit && Fiona.getAC().getPing().getTPS() > this.getTps() && !e.isCancelled() && !BlockUtils.isPiston(e.getFrom().getBlock())) {
                final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                if (player.getAllowFlight() || Math.abs(System.currentTimeMillis() - user.getLoginMIllis()) < 1250L || player.getVehicle() != null || Math.abs(System.currentTimeMillis() - user.isTeleported()) < 200L || MathUtils.elapsed(user.isVelocity()) < 1200L) {
                    return;
                }
                final double vector = MathUtils.offset(MathUtils.getHorizontalVector(e.getFrom()), MathUtils.getHorizontalVector(e.getTo()));
                double maxSpeed = 0.0;
                final double yVelocity = MathUtils.round(player.getVelocity().getY(), 2);
                final int speed = PlayerUtils.getPotionEffectLevel(player, PotionEffectType.SPEED);
                final Location below = player.getLocation().clone().subtract(0.0, 1.0, 0.0);
                if (yVelocity == -0.08) {
                    if (user.getGroundTicks() > 6) {
                        maxSpeed = 0.29;
                    }
                    else {
                        maxSpeed = 0.406;
                    }
                }
                else if (yVelocity == 0.42) {
                    maxSpeed = 0.362;
                }
                else if (yVelocity == 0.33) {
                    maxSpeed = 0.354;
                }
                else if (yVelocity == 0.25) {
                    maxSpeed = 0.348;
                }
                else if (yVelocity == 0.16 || yVelocity == 0.17) {
                    maxSpeed = 0.341;
                }
                else if (yVelocity == 0.08) {
                    maxSpeed = 0.336;
                }
                else if (yVelocity == 0.0) {
                    maxSpeed = 0.332;
                }
                else if (yVelocity == -0.16) {
                    maxSpeed = 0.326;
                }
                else if (yVelocity == -0.23) {
                    maxSpeed = 0.32;
                }
                else if (yVelocity == -0.3) {
                    maxSpeed = 0.32;
                }
                else if (yVelocity == -0.38) {
                    maxSpeed = 0.316;
                }
                else {
                    maxSpeed = 0.4;
                }
                if (user.getAirTicks() > 6) {
                    maxSpeed += 0.06;
                }
                if (user.getBlockTicks() > 0) {
                    maxSpeed += 0.3;
                }
                if (user.getIceTicks() > 0) {
                    maxSpeed += 0.2;
                }
                if (user.getIceTicks() > 0 && user.getBlockTicks() > 0) {
                    maxSpeed += 0.15;
                    if (Math.abs(user.getIceTicks() - user.getBlockTicks()) > 2) {
                        maxSpeed += 0.1;
                    }
                }
                if (speed != user.getLastSpeedLevel()) {
                    user.setSpeedTicks(40);
                    user.setLastSpeedLevel(speed);
                }
                if (user.getSpeedTicks() > 0) {
                    maxSpeed += 0.4;
                    user.setSpeedTicks(user.getSpeedTicks() - 1);
                }
                if (user.getSpeedTicks() > 0 && user.getSpeedTicks() < 20) {
                    maxSpeed += speed * 0.5;
                }
                if (PlayerUtils.wasOnSlime(player)) {
                    maxSpeed += 0.21;
                }
                final double velocity = user.getLastVelocity().getX() + user.getLastVelocity().getZ();
                if (velocity > 0.0) {
                    maxSpeed += Math.abs(velocity * 1.5);
                }
                if (MathUtils.elapsed(user.getLastBlockPlace()) < 250L) {
                    maxSpeed += 0.2;
                }
                if (speed > 0) {
                    maxSpeed += (PlayerUtils.isOnGroundVanilla(player) ? (speed * 0.062) : ((speed > 5) ? (speed * 0.024) : (speed * 0.017)));
                }
                if (user.getGroundTicks() > 0 && (BlockUtils.isStair(below.getBlock()) || BlockUtils.isSlab(below.getBlock()))) {
                    maxSpeed += 0.24;
                }
                maxSpeed += (player.getWalkSpeed() - 0.2) * 1.8;
                double verbose = this.verbose.getOrDefault(player.getUniqueId(), 0.0);
                if (vector > maxSpeed) {
                    verbose += 5.0;
                }
                else {
                    verbose = ((verbose > -10.0) ? (verbose - 1.0) : -10.0);
                }
                if (verbose > 10.0) {
                    user.setVL(this, user.getVL(this) + 3);
                    user.setCancelled(this, CancelType.MOVEMENT, 3);
                    verbose = 0.0;
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Limit " + Color.Gray + "S: " + Color.Green + vector + Color.Gray + " > " + Color.Green + maxSpeed);
                }
                this.verbose.put(player.getUniqueId(), verbose);
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e = (PlayerMoveEvent)event;
            if ((e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ() && e.getTo().getY() == e.getFrom().getY()) || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                return;
            }
            if (this.constant) {
                final Player player = e.getPlayer();
                final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                final double vector = MathUtils.offset(MathUtils.getHorizontalVector(e.getFrom()), MathUtils.getHorizontalVector(e.getTo()));
                if (vector != 0.0 && !PlayerUtils.isInWeb(player) && !PlayerUtils.isInWater(player) && !PlayerUtils.isInWater(player.getLocation())) {
                    if (user.speedValues.size() > 6) {
                        Collections.sort(user.speedValues);
                        final double one = user.speedValues.get(0);
                        final double two = user.speedValues.get(user.speedValues.size() - 1);
                        if ((Math.abs(one - two) < 1.0E-9 && user.getAirTicks() > 0) || (vector < 0.2801 && vector > 0.27 && Math.abs(one - two) < 1.0E-9 && user.getGroundTicks() >= 5)) {
                            user.setVL(this, user.getVL(this) + 1);
                            user.setCancelled(this, CancelType.MOVEMENT, 3);
                            this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Constant" + Color.Gray + " V: " + Color.Green + "0");
                        }
                        user.setLastSpeedDifference(Math.abs(one - two));
                        user.speedValues.clear();
                    }
                    else {
                        user.speedValues.add(vector);
                    }
                }
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e = (PlayerMoveEvent)event;
            if (e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ() && e.getTo().getY() == e.getFrom().getY()) {
                return;
            }
            if (this.single) {
                final Location from = ((PlayerMoveEvent)event).getFrom().clone();
                final Location to = ((PlayerMoveEvent)event).getTo().clone();
                final Player p = e.getPlayer();
                final User user2 = Fiona.getUserManager().getUser(p.getUniqueId());
                if (e.isCancelled() || user2.isCancelled() != CancelType.NONE || PlayerUtils.isGliding(p) || p.getVehicle() != null || p.getGameMode().equals((Object)GameMode.CREATIVE) || p.getAllowFlight() || Fiona.getAC().getPing().getTPS() < this.getTps() || PlayerUtils.wasOnSlime(p) || BlockUtils.isPiston(e.getFrom().getBlock())) {
                    return;
                }
                final Location l = p.getLocation();
                final int x = l.getBlockX();
                final int y = l.getBlockY();
                final int z = l.getBlockZ();
                final Location blockLoc = new Location(p.getWorld(), (double)x, (double)(y - 1), (double)z);
                final Location loc = new Location(p.getWorld(), (double)x, (double)y, (double)z);
                final Location loc2 = new Location(p.getWorld(), (double)x, (double)(y + 1), (double)z);
                final Location above = new Location(p.getWorld(), (double)x, (double)(y + 2), (double)z);
                final Location above2 = new Location(p.getWorld(), (double)(x - 1), (double)(y + 2), (double)(z - 1));
                final long loginDiff = Math.abs(System.currentTimeMillis() - user2.getLoginMIllis());
                if (loginDiff < 1250L) {
                    return;
                }
                if (user2.getIceTicks() < 0) {
                    user2.setIceTicks(0);
                }
                if (blockLoc.getBlock().getType() == Material.ICE || blockLoc.getBlock().getType() == Material.PACKED_ICE) {
                    user2.setIceTicks(user2.getIceTicks() + 1);
                }
                else {
                    user2.setIceTicks(user2.getIceTicks() - 1);
                }
                double Airmaxspeed = 0.4;
                double maxSpeed2 = 0.535;
                double newmaxspeed = 0.75;
                if (user2.getIceTicks() >= 100) {
                    newmaxspeed = 1.0;
                }
                double ig = 0.28;
                final double speed2 = PlayerUtils.offset(this.getHV(to.toVector()), this.getHV(from.toVector()));
                final double onGroundDiff = to.getY() - from.getY();
                final double velocity2 = (user2.getLastVelocity().getX() + user2.getLastVelocity().getZ() > 0.0) ? (user2.getLastVelocity().getX() + user2.getLastVelocity().getZ()) : 0.0;
                maxSpeed2 += velocity2;
                newmaxspeed += velocity2;
                ig += velocity2;
                Airmaxspeed += velocity2;
                if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                    final int level = this.getPotionEffectLevel(p, PotionEffectType.SPEED);
                    if (level > 0) {
                        newmaxspeed *= level * 20 * 0.011 + 1.0;
                        Airmaxspeed *= level * 20 * 0.011 + 1.0;
                        maxSpeed2 *= level * 20 * 0.011 + 1.0;
                        ig *= level * 20 * 0.011 + 1.0;
                    }
                }
                Airmaxspeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
                maxSpeed2 += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
                newmaxspeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
                final int vl = user2.getVL(this);
                if (PlayerUtils.isOnGround(p.getLocation(), 0.0, 0.15) && speed2 >= maxSpeed2 && user2.getGroundTicks() > 0 && blockLoc.getBlock().getType() != Material.ICE && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && user2.getIceTicks() < 1) {
                    user2.setVL(this, vl + 1);
                    user2.setGroundTicks(0);
                    user2.setCancelled(this, CancelType.MOVEMENT, 1);
                    this.Alert(p, Color.Gray + "Reason: " + Color.Green + "onGround " + Color.Gray + "Speed: " + Color.Green + speed2 + Color.Gray + " > " + Color.Green + maxSpeed2);
                }
                if (Fiona.getAC().getPing().getTPS() > 17.5 && !PlayerUtils.isReallyOnground(p) && speed2 >= Airmaxspeed && user2.getIceTicks() < 10 && blockLoc.getBlock().getType() != Material.ICE && !blockLoc.getBlock().isLiquid() && !loc.getBlock().isLiquid() && blockLoc.getBlock().getType() != Material.PACKED_ICE && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && blockLoc.getBlock().getType() != Material.AIR) {
                    user2.setVL(this, vl + 1);
                    user2.setCancelled(this, CancelType.MOVEMENT, 1);
                    user2.setIceTicks(0);
                    this.Alert(p, Color.Gray + "Reason: " + Color.Green + "midAir " + Color.Gray + "Speed: " + Color.Green + speed2 + Color.Gray + " > " + Color.Green + Airmaxspeed);
                }
                if (speed2 >= newmaxspeed && user2.getIceTicks() < 10 && p.getFallDistance() < 0.6 && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && loc2.getBlock().getType() == Material.AIR) {
                    user2.setVL(this, vl + 1);
                    user2.setCancelled(this, CancelType.MOVEMENT, 1);
                    user2.setIceTicks(0);
                    this.Alert(p, Color.Gray + "Reason: " + Color.Green + "Limited " + Color.Gray + "Speed: " + Color.Green + speed2 + Color.Gray + " > " + Color.Green + newmaxspeed);
                }
                if (speed2 > ig && !PlayerUtils.isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4 && !PlayerUtils.flaggyStuffNear(p.getLocation()) && blockLoc.getBlock().getType() != Material.ICE && e.getTo().getY() != e.getFrom().getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && user2.getIceTicks() == 0) {
                    user2.setVL(this, vl + 1);
                    user2.setCancelled(this, CancelType.MOVEMENT, 1);
                    this.Alert(p, Color.Gray + "Reason: " + Color.Green + "Vanilla " + Color.Gray + "Speed: " + Color.Green + speed2 + Color.Gray + " > " + Color.Green + ig);
                }
            }
        }
        else if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e2 = (PlayerQuitEvent)event;
            if (this.verbose.containsKey(e2.getPlayer().getUniqueId())) {
                this.verbose.remove(e2.getPlayer().getUniqueId());
            }
        }
    }
    
    private int getPotionEffectLevel(final Player p, final PotionEffectType pet) {
        for (final PotionEffect pe : p.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }
    
    private Vector getHV(final Vector V) {
        V.setY(0);
        return V;
    }
}
