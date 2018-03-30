package pw.skidrevenant.fiona.checks.movement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.Directional;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.material.TrapDoor;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.BlockUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerJoinEvent.class })
public class Phase extends Checks
{
    public static List<Material> allowed;
    private List<Material> semi;
    private boolean silent;
    private Map<UUID, Location> lastLocation;
    
    public Phase() {
        super("Phase", ChecksType.MOVEMENT, Fiona.getAC(), 100, true, false);
        Phase.allowed = new ArrayList<Material>();
        this.semi = new ArrayList<Material>();
        this.lastLocation = new HashMap<UUID, Location>();
        Phase.allowed.add(Material.SIGN);
        Phase.allowed.add(Material.SIGN_POST);
        Phase.allowed.add(Material.WALL_SIGN);
        Phase.allowed.add(Material.SUGAR_CANE_BLOCK);
        Phase.allowed.add(Material.WHEAT);
        Phase.allowed.add(Material.POTATO);
        Phase.allowed.add(Material.CARROT);
        Phase.allowed.add(Material.STEP);
        Phase.allowed.add(Material.WOOD_STEP);
        Phase.allowed.add(Material.SOUL_SAND);
        Phase.allowed.add(Material.CARPET);
        Phase.allowed.add(Material.STONE_PLATE);
        Phase.allowed.add(Material.WOOD_PLATE);
        Phase.allowed.add(Material.LADDER);
        Phase.allowed.add(Material.CHEST);
        Phase.allowed.add(Material.WATER);
        Phase.allowed.add(Material.STATIONARY_WATER);
        Phase.allowed.add(Material.LAVA);
        Phase.allowed.add(Material.STATIONARY_LAVA);
        Phase.allowed.add(Material.REDSTONE_COMPARATOR);
        Phase.allowed.add(Material.REDSTONE_COMPARATOR_OFF);
        Phase.allowed.add(Material.REDSTONE_COMPARATOR_ON);
        Phase.allowed.add(Material.IRON_PLATE);
        Phase.allowed.add(Material.GOLD_PLATE);
        Phase.allowed.add(Material.DAYLIGHT_DETECTOR);
        Phase.allowed.add(Material.STONE_BUTTON);
        Phase.allowed.add(Material.WOOD_BUTTON);
        Phase.allowed.add(Material.HOPPER);
        Phase.allowed.add(Material.RAILS);
        Phase.allowed.add(Material.ACTIVATOR_RAIL);
        Phase.allowed.add(Material.DETECTOR_RAIL);
        Phase.allowed.add(Material.POWERED_RAIL);
        Phase.allowed.add(Material.TRIPWIRE_HOOK);
        Phase.allowed.add(Material.TRIPWIRE);
        Phase.allowed.add(Material.SNOW_BLOCK);
        Phase.allowed.add(Material.REDSTONE_TORCH_OFF);
        Phase.allowed.add(Material.REDSTONE_TORCH_ON);
        Phase.allowed.add(Material.DIODE_BLOCK_OFF);
        Phase.allowed.add(Material.DIODE_BLOCK_ON);
        Phase.allowed.add(Material.DIODE);
        Phase.allowed.add(Material.SEEDS);
        Phase.allowed.add(Material.MELON_SEEDS);
        Phase.allowed.add(Material.PUMPKIN_SEEDS);
        Phase.allowed.add(Material.DOUBLE_PLANT);
        Phase.allowed.add(Material.LONG_GRASS);
        Phase.allowed.add(Material.WEB);
        Phase.allowed.add(Material.CAKE_BLOCK);
        Phase.allowed.add(Material.SNOW);
        Phase.allowed.add(Material.FLOWER_POT);
        Phase.allowed.add(Material.BREWING_STAND);
        Phase.allowed.add(Material.CAULDRON);
        Phase.allowed.add(Material.CACTUS);
        Phase.allowed.add(Material.WATER_LILY);
        Phase.allowed.add(Material.RED_ROSE);
        Phase.allowed.add(Material.ENCHANTMENT_TABLE);
        Phase.allowed.add(Material.ENDER_PORTAL_FRAME);
        Phase.allowed.add(Material.PORTAL);
        Phase.allowed.add(Material.ENDER_PORTAL);
        Phase.allowed.add(Material.ENDER_CHEST);
        Phase.allowed.add(Material.NETHER_FENCE);
        Phase.allowed.add(Material.NETHER_WARTS);
        Phase.allowed.add(Material.REDSTONE_WIRE);
        Phase.allowed.add(Material.LEVER);
        Phase.allowed.add(Material.YELLOW_FLOWER);
        Phase.allowed.add(Material.CROPS);
        Phase.allowed.add(Material.WATER);
        Phase.allowed.add(Material.LAVA);
        Phase.allowed.add(Material.SKULL);
        Phase.allowed.add(Material.TRAPPED_CHEST);
        Phase.allowed.add(Material.FIRE);
        Phase.allowed.add(Material.BROWN_MUSHROOM);
        Phase.allowed.add(Material.RED_MUSHROOM);
        Phase.allowed.add(Material.DEAD_BUSH);
        Phase.allowed.add(Material.SAPLING);
        Phase.allowed.add(Material.TORCH);
        Phase.allowed.add(Material.MELON_STEM);
        Phase.allowed.add(Material.PUMPKIN_STEM);
        Phase.allowed.add(Material.COCOA);
        Phase.allowed.add(Material.BED);
        Phase.allowed.add(Material.BED_BLOCK);
        Phase.allowed.add(Material.PISTON_EXTENSION);
        Phase.allowed.add(Material.PISTON_MOVING_PIECE);
        this.semi.add(Material.IRON_FENCE);
        this.semi.add(Material.THIN_GLASS);
        this.semi.add(Material.STAINED_GLASS_PANE);
        this.semi.add(Material.COBBLE_WALL);
        this.semi.add(Material.FENCE);
        this.semi.add(Material.NETHER_FENCE);
        this.semi.add(Material.STAINED_GLASS_PANE);
        this.silent = Fiona.getAC().getConfig().getBoolean("checks.Phase.silent");
        this.setTps(0.0);
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e = (PlayerMoveEvent)event;
            final Player player = e.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (player.isDead()) {
                return;
            }
            final UUID playerId = player.getUniqueId();
            final Location loc1 = this.lastLocation.containsKey(playerId) ? this.lastLocation.get(playerId) : player.getLocation();
            final Location loc2 = player.getLocation();
            if (player.getAllowFlight()) {
                user.setTeleportedPhase(true);
            }
            if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
                user.setTeleportedPhase(true);
            }
            if (loc1.getWorld() == loc2.getWorld() && !user.isTeleportedPhase() && loc1.distance(loc2) > 10.0) {
                player.teleport((Location)this.lastLocation.get(playerId), PlayerTeleportEvent.TeleportCause.PLUGIN);
                if (player.getLocation().getBlock().getType().isSolid() || (player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid() && player.getVehicle() == null)) {
                    user.setTeleported(System.currentTimeMillis());
                    player.teleport((Location)this.lastLocation.get(playerId), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    return;
                }
                user.setVL(this, user.getVL(this) + 1);
                user.setTeleported(System.currentTimeMillis());
                this.Alert(player, "*");
            }
            else if (this.isLegit(playerId, loc1, loc2)) {
                this.lastLocation.put(playerId, loc2);
            }
            else if (player.hasPermission("fiona.admin") || this.lastLocation.containsKey(playerId)) {
                player.teleport((Location)this.lastLocation.get(playerId), PlayerTeleportEvent.TeleportCause.PLUGIN);
                if (player.getLocation().getBlock().getType().isSolid() || (player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid() && player.getVehicle() == null)) {
                    user.setTeleported(System.currentTimeMillis());
                    player.teleport((Location)this.lastLocation.get(playerId), PlayerTeleportEvent.TeleportCause.PLUGIN);
                    return;
                }
                user.setVL(this, user.getVL(this) + 1);
                user.setTeleported(System.currentTimeMillis());
                this.Alert(player, "*");
            }
        }
    }
    
    public boolean isLegit(final UUID playerId, final Location loc1, final Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            return true;
        }
        final User user = Fiona.getUserManager().getUser(playerId);
        if (user == null) {
            return true;
        }
        if (user.isTeleportedPhase()) {
            user.setTeleportedPhase(false);
            return true;
        }
        int moveMaxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        final int moveMinX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        final int moveMaxY = Math.max(loc1.getBlockY(), loc2.getBlockY()) + 1;
        int moveMinY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        final int moveMaxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        final int moveMinZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        if (moveMaxY > 256) {
            moveMaxX = 256;
        }
        if (moveMinY > 256) {
            moveMinY = 256;
        }
        for (int x = moveMinX; x <= moveMaxX; ++x) {
            for (int z = moveMinZ; z <= moveMaxZ; ++z) {
                for (int y = moveMinY; y <= moveMaxY; ++y) {
                    final Block block = loc1.getWorld().getBlockAt(x, y, z);
                    if ((y != moveMinY || loc1.getBlockY() == loc2.getBlockY()) && this.hasPhased(block, loc1, loc2, Bukkit.getPlayer(playerId))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private boolean hasPhased(final Block block, final Location loc1, final Location loc2, final Player p) {
        if (Phase.allowed.contains(block.getType()) || BlockUtils.isStair(block) || BlockUtils.isSlab(block) || BlockUtils.isClimbableBlock(block) || block.isLiquid() || block.getType().equals((Object)Material.AIR)) {
            return false;
        }
        final double moveMaxX = Math.max(loc1.getX(), loc2.getX());
        final double moveMinX = Math.min(loc1.getX(), loc2.getX());
        final double moveMaxY = Math.max(loc1.getY(), loc2.getY()) + 1.8;
        final double moveMinY = Math.min(loc1.getY(), loc2.getY());
        final double moveMaxZ = Math.max(loc1.getZ(), loc2.getZ());
        final double moveMinZ = Math.min(loc1.getZ(), loc2.getZ());
        double blockMaxX = block.getLocation().getBlockX() + 1;
        double blockMinX = block.getLocation().getBlockX();
        double blockMaxY = block.getLocation().getBlockY() + 2;
        double blockMinY = block.getLocation().getBlockY();
        double blockMaxZ = block.getLocation().getBlockZ() + 1;
        double blockMinZ = block.getLocation().getBlockZ();
        if (blockMinY > moveMinY) {
            --blockMaxY;
        }
        if (block.getType().equals((Object)Material.IRON_DOOR_BLOCK) || block.getType().equals((Object)Material.WOODEN_DOOR)) {
            final Door door = (Door)block.getType().getNewData(block.getData());
            if (door.isTopHalf()) {
                return false;
            }
            BlockFace facing = door.getFacing();
            if (door.isOpen()) {
                final Block up = block.getRelative(BlockFace.UP);
                if (!up.getType().equals((Object)Material.IRON_DOOR_BLOCK) && !up.getType().equals((Object)Material.WOODEN_DOOR)) {
                    return false;
                }
                final boolean hinge = (up.getData() & 0x1) == 0x1;
                if (facing == BlockFace.NORTH) {
                    facing = (hinge ? BlockFace.WEST : BlockFace.EAST);
                }
                else if (facing == BlockFace.EAST) {
                    facing = (hinge ? BlockFace.NORTH : BlockFace.SOUTH);
                }
                else if (facing == BlockFace.SOUTH) {
                    facing = (hinge ? BlockFace.EAST : BlockFace.WEST);
                }
                else {
                    facing = (hinge ? BlockFace.SOUTH : BlockFace.NORTH);
                }
            }
            if (facing == BlockFace.WEST) {
                blockMaxX -= 0.8;
            }
            if (facing == BlockFace.EAST) {
                blockMinX += 0.8;
            }
            if (facing == BlockFace.NORTH) {
                blockMaxZ -= 0.8;
            }
            if (facing == BlockFace.SOUTH) {
                blockMinZ += 0.8;
            }
        }
        else if (block.getType().equals((Object)Material.FENCE_GATE)) {
            if (((Gate)block.getType().getNewData(block.getData())).isOpen()) {
                return false;
            }
            final BlockFace face = ((Directional)block.getType().getNewData(block.getData())).getFacing();
            if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
                blockMaxX -= 0.2;
                blockMinX += 0.2;
            }
            else {
                blockMaxZ -= 0.2;
                blockMinZ += 0.2;
            }
        }
        else if (block.getType().equals((Object)Material.TRAP_DOOR)) {
            final TrapDoor door2 = (TrapDoor)block.getType().getNewData(block.getData());
            if (door2.isOpen()) {
                return false;
            }
            if (door2.isInverted()) {
                blockMinY += 0.85;
            }
            else {
                blockMaxY -= ((blockMinY > moveMinY) ? 0.85 : 1.85);
            }
        }
        else if (block.getType().equals((Object)Material.FENCE) || this.semi.contains(block.getType())) {
            blockMaxX -= 0.2;
            blockMinX += 0.2;
            blockMaxZ -= 0.2;
            blockMinZ += 0.2;
            if ((moveMaxX > blockMaxX && moveMinX > blockMaxX && moveMaxZ > blockMaxZ && moveMinZ > blockMaxZ) || (moveMaxX < blockMinX && moveMinX < blockMinX && moveMaxZ > blockMaxZ && moveMinZ > blockMaxZ) || (moveMaxX > blockMaxX && moveMinX > blockMaxX && moveMaxZ < blockMinZ && moveMinZ < blockMinZ) || (moveMaxX < blockMinX && moveMinX < blockMinX && moveMaxZ < blockMinZ && moveMinZ < blockMinZ)) {
                return false;
            }
            if (block.getRelative(BlockFace.EAST).getType() == block.getType()) {
                blockMaxX += 0.2;
            }
            if (block.getRelative(BlockFace.WEST).getType() == block.getType()) {
                blockMinX -= 0.2;
            }
            if (block.getRelative(BlockFace.SOUTH).getType() == block.getType()) {
                blockMaxZ += 0.2;
            }
            if (block.getRelative(BlockFace.NORTH).getType() == block.getType()) {
                blockMinZ -= 0.2;
            }
        }
        final boolean x = loc1.getX() < loc2.getX();
        final boolean y = loc1.getY() < loc2.getY();
        final boolean z = loc1.getZ() < loc2.getZ();
        final double distance = loc1.distance(loc2) - Math.abs(loc1.getY() - loc2.getY());
        return (distance > 0.5 && block.getType().isSolid()) || (moveMinX != moveMaxX && moveMinY <= blockMaxY && moveMaxY >= blockMinY && moveMinZ <= blockMaxZ && moveMaxZ >= blockMinZ && ((x && moveMinX <= blockMinX && moveMaxX >= blockMinX) || (!x && moveMinX <= blockMaxX && moveMaxX >= blockMaxX))) || (moveMinY != moveMaxY && moveMinX <= blockMaxX && moveMaxX >= blockMinX && moveMinZ <= blockMaxZ && moveMaxZ >= blockMinZ && ((y && moveMinY <= blockMinY && moveMaxY >= blockMinY) || (!y && moveMinY <= blockMaxY && moveMaxY >= blockMaxY))) || (moveMinZ != moveMaxZ && moveMinX <= blockMaxX && moveMaxX >= blockMinX && moveMinY <= blockMaxY && moveMaxY >= blockMinY && ((z && moveMinZ <= blockMinZ && moveMaxZ >= blockMinZ) || (!z && moveMinZ <= blockMaxZ && moveMaxZ >= blockMaxZ)));
    }
}
