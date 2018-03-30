package pw.skidrevenant.fiona.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.checks.movement.Phase;
import pw.skidrevenant.fiona.packets.events.PacketMovementEvent;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.BlockUtils;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.CustomLocation;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.PlayerUtils;

public class EventPlayerMove implements Listener
{
    private final ImmutableSet<Material> blockedPearlTypes;
    private boolean glitch;
    
    public EventPlayerMove() {
        this.blockedPearlTypes = (ImmutableSet<Material>)Sets.immutableEnumSet((Enum)Material.THIN_GLASS, (Enum[])new Material[] { Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE, Material.FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS });
        this.glitch = true;
        this.glitch = Fiona.getAC().getConfig().getBoolean("PearlFix.stuckFix");
    }
    
    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        Fiona.getAC().getChecks().event((Event)event);
        final User user = Fiona.getUserManager().getUser(event.getPlayer().getUniqueId());
        if (user != null && event.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            user.setTeleportedPhase(true);
            user.setTeleported(System.currentTimeMillis());
        }
        if (user != null && event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && this.glitch) {
            final Location to = event.getTo();
            if (this.blockedPearlTypes.contains((Object)to.getBlock().getType()) && to.getBlock().getType() != Material.FENCE_GATE && to.getBlock().getType() != Material.TRAP_DOOR) {
                final PearlFixEvent event2 = new PearlFixEvent(event.getPlayer(), PearlFixType.STUCK);
                Bukkit.getPluginManager().callEvent((Event)event2);
                if (!event2.isCancelled()) {
                    final Player player = event.getPlayer();
                    player.sendMessage(Fiona.getAC().getPrefix() + Color.Gray + "You have been detected trying to pearl glitch, therefore your pearl was cancelled.");
                    event.setCancelled(true);
                }
                return;
            }
            to.setX(to.getBlockX() + 0.5);
            to.setZ(to.getBlockZ() + 0.5);
            if ((!Phase.allowed.contains(to.getBlock().getType()) || !Phase.allowed.contains(to.clone().add(0.0, 1.0, 0.0).getBlock().getType())) && (to.getBlock().getType().isSolid() || to.clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid()) && (to.clone().subtract(0.0, 1.0, 0.0).getBlock().getType().isSolid() & !BlockUtils.isSlab(to.getBlock()))) {
                final PearlFixEvent event2 = new PearlFixEvent(event.getPlayer(), PearlFixType.STUCK);
                Bukkit.getPluginManager().callEvent((Event)event2);
                if (!event2.isCancelled()) {
                    final Player player = event.getPlayer();
                    event.setCancelled(true);
                    player.sendMessage(Fiona.getAC().getPrefix() + Color.Gray + "Could not find a safe location, therefore your pearl was cancelled.");
                }
                return;
            }
            if (!Phase.allowed.contains(to.clone().add(0.0, 1.0, 0.0).getBlock().getType()) && to.clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid() && !to.getBlock().getType().isSolid()) {
                to.setY(to.getY() - 0.7);
            }
            event.setTo(to);
        }
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        Fiona.getAC().getChecks().event((Event)event);
        final Player p = event.getPlayer();
        Bukkit.getServer().getPluginManager().callEvent((Event)new PacketMovementEvent(p, event.getFrom(), event.getTo()));
        final User user = Fiona.getUserManager().getUser(p.getUniqueId());
        if (user == null) {
            return;
        }
        final double horizontal = Math.sqrt(Math.pow(event.getTo().getX() - event.getFrom().getX(), 2.0) + Math.pow(event.getTo().getZ() - event.getFrom().getZ(), 2.0));
        final double vertical = Math.sqrt(Math.pow(event.getTo().getY() - event.getFrom().getY(), 2.0));
        user.setDeltaXZ(horizontal);
        user.setDeltaY(vertical);
        if (user.getIceTicks() < 0) {
            user.setIceTicks(0);
        }
        final Location from = event.getFrom();
        final Location to = event.getTo();
        final CustomLocation customLocation4 = new CustomLocation(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
        user.addMovePacket(customLocation4);
        if (!PlayerUtils.isOnGround(p.getLocation()) && from.getY() > to.getY()) {
            user.setRealFallDistance(user.getRealFallDistance() + MathUtils.getVerticalDistance(event.getFrom(), event.getTo()));
            user.setSlimeDistance(user.getSlimeDistance() + MathUtils.getVerticalDistance(event.getFrom(), event.getTo()));
        }
        else if (PlayerUtils.isOnGround(p.getLocation()) && PlayerUtils.wasOnSlime(p) && MathUtils.getVerticalDistance(from, to) < 0.1) {
            user.setSlimeDistance(0.0);
        }
        else if (PlayerUtils.isOnGround(p.getLocation())) {
            user.setRealFallDistance(0.0);
        }
        if (MathUtils.elapsed(user.getFoodClick()) < 2000L && horizontal > 0.14) {
            user.setFoodSprintTicks(user.getFoodSprintTicks() + 1);
        }
        else if (user.getFoodSprintTicks() > 0) {
            user.setFoodSprintTicks(0);
        }
        if (p.isSprinting()) {
            user.setSprintTicks(user.getSprintTicks() + 1);
        }
        else {
            user.setSprintTicks(0);
        }
        final Location blockLoc = p.getLocation().subtract(0.0, 1.0, 0.0);
        final Location blockLoc2 = p.getLocation().subtract(0.0, 1.5, 0.0);
        if (blockLoc.getBlock().getType() == Material.ICE || blockLoc.getBlock().getType() == Material.PACKED_ICE || blockLoc2.getBlock().getType() == Material.ICE || blockLoc2.getBlock().getType() == Material.PACKED_ICE) {
            if (user.getIceTicks() < 40) {
                user.setIceTicks(user.getIceTicks() + 2);
            }
        }
        else {
            user.setIceTicks(user.getIceTicks() - 1);
        }
        final Location below = p.getLocation().subtract(0.0, 0.87, 0.0);
        if (PlayerUtils.isOnGroundFP(p)) {
            user.setGroundTicks(user.getGroundTicks() + 1);
            user.setAirTicks(0);
            if (below.getBlock().getType().equals((Object)Material.SOUL_SAND)) {
                user.setSoulSandTicks(user.getSoulSandTicks() + 1);
            }
            else if (user.getSoulSandTicks() > 0) {
                user.setSoulSandTicks(0);
            }
        }
        else {
            user.setGroundTicks(0);
            if (!p.isFlying()) {
                user.setAirTicks(user.getAirTicks() + 1);
            }
        }
        if (p.getLocation().getBlock().getType().equals((Object)Material.WEB) || p.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().equals(Material.WEB)) {
            user.setWebTicks(user.getWebTicks() + 1);
        }
        else if (user.getWebTicks() > 0) {
            user.setWebTicks(0);
        }
        if (PlayerUtils.isOnGround(p.getLocation().clone().add(0.0, 2.0, 0.0))) {
            if (user.getBlockTicks() < 40) {
                user.setBlockTicks(user.getBlockTicks() + 2);
            }
        }
        else {
            user.setBlockTicks((user.getBlockTicks() > 0) ? (user.getBlockTicks() - 1) : 0);
        }
        if (event.isCancelled()) {
            user.setMovementCancel(true);
        }
        else {
            user.setMovementCancel(false);
        }
        if (PlayerUtils.isInWater(p.getLocation()) || PlayerUtils.isInWater(p.getLocation().add(0.0, 1.0, 0.0)) || PlayerUtils.isInWater(p)) {
            if (user.getWaterTicks() < 30) {
                user.setWaterTicks(user.getWaterTicks() + 2);
            }
        }
        else if (user.getWaterTicks() > 0) {
            user.setWaterTicks(user.getWaterTicks() - 1);
        }
        if (PlayerUtils.isOnCactus(p) && user.getCactusTicks() < 10) {
            user.setCactusTicks(user.getCactusTicks() + 1);
        }
        else if (user.getCactusTicks() > 0) {
            user.setCactusTicks(user.getCactusTicks() - 1);
        }
        if (user.getLoginTicks() > 0) {
            user.setLoginTicks(user.getLoginTicks() - 1);
        }
        final Vector velocity = user.getLastVelocity();
        if (velocity.getX() > 0.0) {
            final double xDif = Math.abs(event.getFrom().getX() - event.getTo().getX());
            if (xDif - velocity.getX() < 0.0) {
                user.getLastVelocity().setX(0);
            }
            else {
                user.getLastVelocity().setX(xDif - velocity.getX());
            }
        }
        else if (velocity.getY() > 0.0) {
            final double yDif = Math.abs(event.getFrom().getY() - event.getTo().getY());
            if (yDif - velocity.getY() < 0.0) {
                user.getLastVelocity().setY(0);
            }
            else {
                user.getLastVelocity().setY(yDif - velocity.getY());
            }
        }
        else if (velocity.getZ() > 0.0) {
            final double zDif = Math.abs(event.getFrom().getZ() - event.getTo().getZ());
            if (zDif - velocity.getZ() < 0.0) {
                user.getLastVelocity().setZ(0);
            }
            else {
                user.getLastVelocity().setZ(zDif - velocity.getZ());
            }
        }
        if (user.isCancelled() == CancelType.MOVEMENT && user.getCancelTicks() > 0) {
            if (user.getSetbackLocation() != null) {
                event.setTo(user.getSetbackLocation());
                user.setCancelTicks(user.getCancelTicks() - 1);
            }
            else {
                event.setCancelled(true);
            }
        }
        else if (user.isCancelled() == CancelType.NONE) {
            if (PlayerUtils.isOnGround(p.getLocation())) {
                user.setSetbackLocation(p.getLocation());
            }
        }
        else if (user.isCancelled() == CancelType.MOVEMENT) {
            user.setCancelled(null, CancelType.NONE);
        }
    }
}
