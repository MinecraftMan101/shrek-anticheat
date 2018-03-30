package pw.skidrevenant.fiona.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.user.User;

public class PlayerUtils
{
    public static final double PLAYER_WIDTH = 0.6;
    private static ImmutableSet<Material> ground;
    public static List<Player> noChats;
    
    public static boolean isGround(final Material material) {
        return PlayerUtils.ground.contains((Object)material);
    }
    
    public static boolean isOnGround(final Location loc) {
        final double diff = 0.29999999999;
        return !isGround(loc.clone().add(0.0, -0.1, 0.0).getBlock().getType()) || !isGround(loc.clone().add(diff, -0.1, 0.0).getBlock().getType()) || !isGround(loc.clone().add(-diff, -0.1, 0.0).getBlock().getType()) || !isGround(loc.clone().add(0.0, -0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(0.0, -0.1, -diff).getBlock().getType()) || !isGround(loc.clone().add(diff, -0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(diff, -0.1, -diff).getBlock().getType()) || !isGround(loc.clone().add(-diff, -0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(-diff, -0.1, -diff).getBlock().getType()) || (BlockUtils.getBlockHeight(loc.clone().subtract(0.0, 0.5, 0.0).getBlock()) != 0.0 && (!isGround(loc.clone().add(diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, 0.0).getBlock().getType()) || !isGround(loc.clone().add(-diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, 0.0).getBlock().getType()) || !isGround(loc.clone().add(0.0, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(0.0, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType()) || !isGround(loc.clone().add(diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType()) || !isGround(loc.clone().add(-diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(-diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType())));
    }
    
    public static boolean isOnGround(final Location loc, final double xDif, final double ydiff) {
        return !isGround(loc.clone().add(0.0, ydiff, 0.0).getBlock().getType()) || !isGround(loc.clone().add(xDif, ydiff, 0.0).getBlock().getType()) || !isGround(loc.clone().add(-xDif, ydiff, 0.0).getBlock().getType()) || !isGround(loc.clone().add(0.0, ydiff, xDif).getBlock().getType()) || !isGround(loc.clone().add(0.0, ydiff, -xDif).getBlock().getType()) || !isGround(loc.clone().add(xDif, ydiff, xDif).getBlock().getType()) || !isGround(loc.clone().add(xDif, ydiff, -xDif).getBlock().getType()) || !isGround(loc.clone().add(-xDif, ydiff, xDif).getBlock().getType()) || !isGround(loc.clone().add(-xDif, ydiff, -xDif).getBlock().getType());
    }
    
    public static void kick(final Player p, final String reason) {
        if (p.isOnline()) {
            p.kickPlayer("�8[�c�lFiona�8]:\n�c" + reason);
            Bukkit.broadcastMessage("�8[�c�lFiona�8] �7kicked �0" + p.getDisplayName() + " �7for �a" + reason);
        }
    }
    
    public static Entity[] getNearbyEntities(final Location l, final int radius) {
        final int chunkRadius = (radius < 16) ? 1 : ((radius - radius % 16) / 16);
        final HashSet<Entity> radiusEntities = new HashSet<Entity>();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; ++chX) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; ++chZ) {
                final int x = (int)l.getX();
                final int y = (int)l.getY();
                final int z = (int)l.getZ();
                for (final Entity e : new Location(l.getWorld(), (double)(x + chX * 16), (double)y, (double)(z + chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) {
                        radiusEntities.add(e);
                    }
                }
            }
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
    
    public static boolean cantStandAtWater(final Block block) {
        final Block otherBlock = block.getRelative(BlockFace.DOWN);
        final boolean isHover = block.getType() == Material.AIR;
        final boolean n = otherBlock.getRelative(BlockFace.NORTH).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        final boolean s = otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.WATER || otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_WATER;
        final boolean e = otherBlock.getRelative(BlockFace.EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_WATER;
        final boolean w = otherBlock.getRelative(BlockFace.WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_WATER;
        final boolean ne = otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_WATER;
        final boolean nw = otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_WATER;
        final boolean se = otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        final boolean sw = otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.WATER || otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_WATER;
        return n && s && e && w && ne && nw && se && sw && isHover;
    }
    
    public static boolean cantStandAtLava(final Block block) {
        final Block otherBlock = block.getRelative(BlockFace.DOWN);
        final boolean isHover = block.getType() == Material.AIR;
        final boolean n = otherBlock.getRelative(BlockFace.NORTH).getType() == Material.LAVA || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_LAVA;
        final boolean s = otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.LAVA || otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_LAVA;
        final boolean e = otherBlock.getRelative(BlockFace.EAST).getType() == Material.LAVA || otherBlock.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_LAVA;
        final boolean w = otherBlock.getRelative(BlockFace.WEST).getType() == Material.LAVA || otherBlock.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_LAVA;
        final boolean ne = otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.LAVA || otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_LAVA;
        final boolean nw = otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.LAVA || otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_LAVA;
        final boolean se = otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.LAVA || otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_LAVA;
        final boolean sw = otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.LAVA || otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_LAVA;
        return n && s && e && w && ne && nw && se && sw && isHover;
    }
    
    public static boolean isInWater(final Location loc) {
        final double diff = 0.3;
        return BlockUtils.isLiquid(loc.clone().add(0.0, 0.0, 0.0).getBlock()) || BlockUtils.isLiquid(loc.clone().add(diff, 0.0, 0.0).getBlock()) || BlockUtils.isLiquid(loc.clone().add(-diff, 0.0, 0.0).getBlock()) || BlockUtils.isLiquid(loc.clone().add(0.0, 0.0, diff).getBlock()) || BlockUtils.isLiquid(loc.clone().add(0.0, 0.0, -diff).getBlock()) || BlockUtils.isLiquid(loc.clone().add(diff, 0.0, diff).getBlock()) || BlockUtils.isLiquid(loc.clone().add(diff, 0.0, -diff).getBlock()) || BlockUtils.isLiquid(loc.clone().add(-diff, 0.0, diff).getBlock()) || BlockUtils.isLiquid(loc.clone().add(-diff, 0.0, -diff).getBlock()) || (BlockUtils.getBlockHeight(loc.clone().subtract(0.0, 0.5, 0.0).getBlock()) != 0.0 && (BlockUtils.isLiquid(loc.clone().add(diff, 0.0, 0.0).getBlock()) || BlockUtils.isLiquid(loc.clone().add(-diff, 0.0, 0.0).getBlock()) || BlockUtils.isLiquid(loc.clone().add(0.0, 0.0, diff).getBlock()) || BlockUtils.isLiquid(loc.clone().add(0.0, 0.0, -diff).getBlock()) || BlockUtils.isLiquid(loc.clone().add(diff, 0.0, diff).getBlock()) || BlockUtils.isLiquid(loc.clone().add(diff, 0.0, -diff).getBlock()) || BlockUtils.isLiquid(loc.clone().add(-diff, 0.0, diff).getBlock()) || BlockUtils.isLiquid(loc.clone().add(-diff, 0.0, -diff).getBlock())));
    }
    
    public static boolean isOnSlab(final Location loc) {
        final double diff = 0.3;
        return BlockUtils.isSlab(loc.clone().add(0.0, 0.0, 0.0).getBlock()) || BlockUtils.isSlab(loc.clone().add(diff, 0.0, 0.0).getBlock()) || BlockUtils.isSlab(loc.clone().add(-diff, 0.0, 0.0).getBlock()) || BlockUtils.isSlab(loc.clone().add(0.0, 0.0, diff).getBlock()) || BlockUtils.isSlab(loc.clone().add(0.0, 0.0, -diff).getBlock()) || BlockUtils.isSlab(loc.clone().add(diff, 0.0, diff).getBlock()) || BlockUtils.isSlab(loc.clone().add(diff, 0.0, -diff).getBlock()) || BlockUtils.isSlab(loc.clone().add(-diff, 0.0, diff).getBlock()) || BlockUtils.isSlab(loc.clone().add(-diff, 0.0, -diff).getBlock());
    }
    
    public static boolean isOnStair(final Location loc) {
        final double diff = 0.3;
        return BlockUtils.isStair(loc.clone().add(0.0, 0.0, 0.0).getBlock()) || BlockUtils.isStair(loc.clone().add(diff, 0.0, 0.0).getBlock()) || BlockUtils.isStair(loc.clone().add(-diff, 0.0, 0.0).getBlock()) || BlockUtils.isStair(loc.clone().add(0.0, 0.0, diff).getBlock()) || BlockUtils.isStair(loc.clone().add(0.0, 0.0, -diff).getBlock()) || BlockUtils.isStair(loc.clone().add(diff, 0.0, diff).getBlock()) || BlockUtils.isStair(loc.clone().add(diff, 0.0, -diff).getBlock()) || BlockUtils.isStair(loc.clone().add(-diff, 0.0, diff).getBlock()) || BlockUtils.isStair(loc.clone().add(-diff, 0.0, -diff).getBlock());
    }
    
    public static boolean isInsideCauldron(final Player player) {
        return player.getLocation().getBlock().getType().equals((Object)Material.CAULDRON);
    }
    
    public static boolean isOnGroundFP(final Player player) {
        final Location half = player.getLocation().subtract(0.0, 0.5, 0.0);
        final Location full = player.getLocation().subtract(0.0, 1.0, 0.0);
        final Location threeSeven = player.getLocation().subtract(0.0, 0.37, 0.0);
        return (isOnGround(player.getLocation()) && isOnGround(player.getLocation(), 0.13, -0.1) && !BlockUtils.isBed(half.getBlock()) && !isOnSlab(player.getLocation()) && !isOnStair(player.getLocation()) && !BlockUtils.pointTwo(half.getBlock()) && !isOnLilyPad(player) && !BlockUtils.pointThreeSeven(threeSeven.getBlock()) && !half.getBlock().getType().equals((Object)Material.BREWING_STAND) && !isInsideCauldron(player) && !BlockUtils.isFence(full.getBlock()) && !BlockUtils.isOneTwoFive(half.getBlock())) || (BlockUtils.isFence(full.getBlock()) && isOnGround(player.getLocation().subtract(0.0, 0.5, 0.0), 0.55, -1.0)) || ((BlockUtils.isBed(half.getBlock()) || isOnStair(player.getLocation()) || isOnSlab(player.getLocation())) && isOnGround(player.getLocation().clone().add(0.0, 0.5, 0.0))) || (BlockUtils.threeQuarters(threeSeven.getBlock()) && isOnGround(player.getLocation().clone().add(0.0, 0.25, 0.0))) || (((half.getBlock().getType().equals((Object)Material.BREWING_STAND) && Math.abs(half.getBlock().getY() - player.getLocation().getY()) > 0.8) || BlockUtils.pointTwo(half.getBlock())) && isOnGround(player.getLocation().clone().subtract(0.0, 0.2, 0.0))) || (BlockUtils.pointThreeSeven(threeSeven.getBlock()) && isOnGround(player.getLocation().add(0.0, 0.625, 0.0))) || (isOnLilyPad(player) && isOnGround(player.getLocation().add(0.0, 0.984375, 0.0))) || (isInsideCauldron(player) && isOnGround(player.getLocation().add(0.0, 0.6875, 0.0))) || (BlockUtils.isOneTwoFive(half.getBlock()) && isOnGround(player.getLocation().add(0.0, 0.125, 0.0)));
    }
    
    public static boolean isOnLilyPad(final Player player) {
        final Block block = player.getLocation().getBlock();
        final Material lily = Material.WATER_LILY;
        return block.getType() == lily || block.getRelative(BlockFace.NORTH).getType() == lily || block.getRelative(BlockFace.SOUTH).getType() == lily || block.getRelative(BlockFace.EAST).getType() == lily || block.getRelative(BlockFace.WEST).getType() == lily;
    }
    
    public static boolean isHoveringOverWater(final Location player, final int blocks) {
        for (int i = player.getBlockY(); i > player.getBlockY() - blocks; --i) {
            final Block newloc = new Location(player.getWorld(), (double)player.getBlockX(), (double)i, (double)player.getBlockZ()).getBlock();
            if (newloc.getType() != Material.AIR) {
                return newloc.isLiquid();
            }
        }
        return false;
    }
    
    public static boolean isHoveringOverWater(final Location player) {
        return isHoveringOverWater(player, 25);
    }
    
    public static boolean isFullyInWater(final Location player) {
        return player.getBlock().isLiquid() && player.clone().add(0.0, 1.0, 0.0).getBlock().isLiquid();
    }
    
    public static boolean isCompletelyInWater(final Location player) {
        return player.getBlock().isLiquid() && player.clone().add(0.0, 1.35, 0.0).getBlock().isLiquid();
    }
    
    public static boolean hasSlabsNear(final Location location) {
        for (final Block block : BlockUtils.getSurroundingXZ(location.getBlock(), true)) {
            if (BlockUtils.isSlab(block)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasBlocksNear(final Player player) {
        for (final Block block : BlockUtils.getSurroundingXZ(player.getLocation().getBlock())) {
            if (block.getType().isSolid()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isOnCactus(final Player player) {
        return player.getLocation().clone().subtract(0.0, 0.8, 0.0).getBlock().getType().equals((Object)Material.CACTUS);
    }
    
    public static boolean hasBlocksNear(final Location location) {
        for (final Block block : BlockUtils.getSurroundingXZ(location.getBlock())) {
            if (block.getType().isSolid()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean wasOnSlime(final Player player) {
        final User user = Fiona.getUserManager().getUser(player.getUniqueId());
        if (user.getSetbackLocation() != null) {
            final Location location = user.getSetbackLocation().clone().subtract(0.0, 1.0, 0.0);
            if (location.getBlock().getTypeId() == 165) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isOnClimbable(final Player player, final int blocks) {
        if (blocks == 0) {
            for (final Block block : getSurrounding(player.getLocation().getBlock(), false)) {
                if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                    return true;
                }
            }
        }
        else {
            for (final Block block : getSurrounding(player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock(), false)) {
                if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                    return true;
                }
            }
        }
        return player.getLocation().getBlock().getType() == Material.LADDER || player.getLocation().getBlock().getType() == Material.VINE;
    }
    
    public static boolean isInWeb(final Player player) {
        return player.getLocation().getBlock().getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WEB || player.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.WEB;
    }
    
    public static boolean isOnGroundFP2(final Player player) {
        return isOnGround(player.getLocation()) || isOnGround(player.getLocation(), 0.2999999999, -0.6);
    }
    
    public static ArrayList<Block> getSurrounding(final Block block, final boolean diagonals) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    for (int z = -1; z <= 1; ++z) {
                        if (x != 0 || y != 0 || z != 0) {
                            blocks.add(block.getRelative(x, y, z));
                        }
                    }
                }
            }
        }
        else {
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }
    
    public static ArrayList<Block> getSurroundingC(final Block block, final boolean diagonals) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            for (int x = -2; x <= 2; ++x) {
                for (int y = -2; y <= 2; ++y) {
                    for (int z = -2; z <= 2; ++z) {
                        if (x != 0 || y != 0 || z != 0) {
                            blocks.add(block.getRelative(x, y, z));
                        }
                    }
                }
            }
        }
        else {
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }
    
    public static boolean hasPotionEffect(final Player player, final PotionEffectType type) {
        if (player.getActivePotionEffects().size() > 0) {
            for (final PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType() == type) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isInWater(final Player player) {
        final Material m = player.getLocation().getBlock().getType();
        return m == Material.STATIONARY_WATER || m == Material.WATER || m == Material.LAVA || m == Material.STATIONARY_LAVA;
    }
    
    public static boolean hasWaterAround(final Player player) {
        final Iterator<Block> iterator = BlockUtils.getSurroundingXZ(player.getLocation().getBlock()).iterator();
        if (iterator.hasNext()) {
            final Block block = iterator.next();
            final Material m = block.getType();
            return m == Material.STATIONARY_WATER || m == Material.WATER || m == Material.LAVA || m == Material.STATIONARY_LAVA;
        }
        return false;
    }
    
    public static boolean hasChestNear(final Player player) {
        for (final Block block : BlockUtils.getSurroundingXZ(player.getLocation().clone().subtract(0.0, 0.8, 0.0).getBlock())) {
            if (block.getType().equals((Object)Material.CHEST) || block.getType().equals((Object)Material.TRAPPED_CHEST)) {
                return true;
            }
        }
        for (final Block block : BlockUtils.getSurroundingXZ(player.getLocation().clone().subtract(0.0, 2.0, 0.0).getBlock())) {
            if (block.getType().equals((Object)Material.CHEST) || block.getType().equals((Object)Material.TRAPPED_CHEST)) {
                return true;
            }
        }
        return false;
    }
    
    public static Location getEyeLocation(final Player player) {
        final Location eye = player.getLocation();
        eye.setY(eye.getY() + player.getEyeHeight());
        return eye;
    }
    
    public static boolean isOnGround(final Location loc, final Double ydiff) {
        final double diff = 0.3;
        return !isGround(loc.clone().add(0.0, (double)ydiff, 0.0).getBlock().getType()) || !isGround(loc.clone().add(diff, (double)ydiff, 0.0).getBlock().getType()) || !isGround(loc.clone().add(-diff, (double)ydiff, 0.0).getBlock().getType()) || !isGround(loc.clone().add(0.0, (double)ydiff, diff).getBlock().getType()) || !isGround(loc.clone().add(0.0, (double)ydiff, -diff).getBlock().getType()) || !isGround(loc.clone().add(diff, (double)ydiff, diff).getBlock().getType()) || !isGround(loc.clone().add(diff, (double)ydiff, -diff).getBlock().getType()) || !isGround(loc.clone().add(-diff, (double)ydiff, diff).getBlock().getType()) || !isGround(loc.clone().add(-diff, (double)ydiff, -diff).getBlock().getType());
    }
    
    public static double offset(final Vector a, final Vector b) {
        return a.subtract(b).length();
    }
    
    public static boolean isReallyOnground(final Player p) {
        final Location l = p.getLocation();
        final int x = l.getBlockX();
        final int y = l.getBlockY();
        final int z = l.getBlockZ();
        final Location b = new Location(p.getWorld(), (double)x, (double)(y - 1), (double)z);
        return p.isOnGround() && b.getBlock().getType() != Material.AIR && b.getBlock().getType() != Material.WEB && !b.getBlock().isLiquid();
    }
    
    public static boolean hasDepthStrider(final Player player) {
        return player.getInventory().getItem(100) != null && player.getInventory().getItem(100).getEnchantmentLevel(Enchantment.getByName("DEPTH_STRIDER")) > 0;
    }
    
    public static boolean isOnGroundVanilla(final Player p) {
        final Location l = p.getLocation();
        final int x = l.getBlockX();
        final int y = l.getBlockY();
        final int z = l.getBlockZ();
        final Location b = new Location(p.getWorld(), (double)x, y - 0.5, (double)z);
        return p.isOnGround() && b.getBlock().getType() != Material.AIR && b.getBlock().getType() != Material.WEB && !b.getBlock().isLiquid();
    }
    
    public static int getPotionEffectLevel(final Player p, final PotionEffectType pet) {
        if (p.getActivePotionEffects().size() > 0) {
            for (final PotionEffect pe : p.getActivePotionEffects()) {
                if (pe.getType().getName().equals(pet.getName())) {
                    return pe.getAmplifier() + 1;
                }
            }
        }
        return 0;
    }
    
    public static boolean flaggyStuffNear(final Location loc) {
        boolean nearBlocks = false;
        for (final Block bl : getSurrounding(loc.getBlock(), true)) {
            if (bl.getType().equals((Object)Material.STEP) || bl.getType().equals((Object)Material.DOUBLE_STEP) || bl.getType().equals((Object)Material.BED) || bl.getType().equals((Object)Material.WOOD_DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }
        for (final Block bl : getSurrounding(loc.getBlock(), false)) {
            if (bl.getType().equals((Object)Material.STEP) || bl.getType().equals((Object)Material.DOUBLE_STEP) || bl.getType().equals((Object)Material.BED) || bl.getType().equals((Object)Material.WOOD_DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.STEP, Material.BED, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP })) {
            nearBlocks = true;
        }
        return nearBlocks;
    }
    
    public static boolean isBlock(final Block block, final Material[] materials) {
        final Material type = block.getType();
        for (final Material m : materials) {
            if (m == type) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isAir(final Player player) {
        final Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        return b.getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.WEST).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.NORTH).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.EAST).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.SOUTH).getType().equals((Object)Material.AIR);
    }
    
    public static Vector getRotation(final Location one, final Location two) {
        final double dx = two.getX() - one.getX();
        final double dy = two.getY() - one.getY();
        final double dz = two.getZ() - one.getZ();
        final double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        final float yaw = (float)(Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(dy, distanceXZ) * 180.0 / 3.141592653589793));
        return new Vector(yaw, pitch, 0.0f);
    }
    
    public static double clamp180(double dub) {
        dub %= 360.0;
        if (dub >= 180.0) {
            dub -= 360.0;
        }
        if (dub < -180.0) {
            dub += 360.0;
        }
        return dub;
    }
    
    public static double getDistance3D(final Location one, final Location two) {
        double toReturn = 0.0;
        final double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        final double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        final double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        final double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }
    
    public static double[] getOffsetsOffCursor(final Player player, final LivingEntity entity) {
        final Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        final Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        final Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        final Vector expectedRotation = getRotation(playerLoc, entityLoc);
        final double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        final double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());
        final double horizontalDistance = MathUtils.getHorizontalDistance(playerLoc, entityLoc);
        final double distance = getDistance3D(playerLoc, entityLoc);
        final double offsetX = deltaYaw * horizontalDistance * distance;
        final double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return new double[] { Math.abs(offsetX), Math.abs(offsetY) };
    }
    
    public static double getOffsetOffCursor(final Player player, final LivingEntity entity) {
        double offset = 0.0;
        final double[] offsets = getOffsetsOffCursor(player, entity);
        offset += offsets[0];
        offset += offsets[1];
        return offset;
    }
    
    public static boolean isGliding(final Player p) {
        if (!ServerUtils.isBukkitVerison("1_9") && !ServerUtils.isBukkitVerison("1_1")) {
            return false;
        }
        boolean isGliding = false;
        try {
            isGliding = (boolean)p.getClass().getMethod("isGliding", (Class<?>[])new Class[0]).invoke(p, new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return isGliding;
    }
    
    static {
        PlayerUtils.ground = (ImmutableSet<Material>)Sets.immutableEnumSet((Enum)Material.SUGAR_CANE, (Enum[])new Material[] { Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.ACTIVATOR_RAIL, Material.AIR, Material.CARROT, Material.CROPS, Material.DEAD_BUSH, Material.DETECTOR_RAIL, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT, Material.FIRE, Material.GOLD_PLATE, Material.IRON_PLATE, Material.LAVA, Material.LEVER, Material.LONG_GRASS, Material.MELON_STEM, Material.NETHER_WARTS, Material.PORTAL, Material.POTATO, Material.POWERED_RAIL, Material.PUMPKIN_STEM, Material.RAILS, Material.RED_ROSE, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.REDSTONE_WIRE, Material.SAPLING, Material.SEEDS, Material.SIGN, Material.SIGN_POST, Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.STONE_BUTTON, Material.STONE_PLATE, Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN, Material.WATER, Material.WEB, Material.WOOD_BUTTON, Material.WOOD_PLATE, Material.YELLOW_FLOWER });
        PlayerUtils.noChats = new ArrayList<Player>();
        final String bukkit = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        if (bukkit.contains("1_8") || (bukkit.contains("1_9") | bukkit.contains("1_1"))) {
            PlayerUtils.ground = (ImmutableSet<Material>)Sets.immutableEnumSet((Enum)Material.SUGAR_CANE, (Enum[])new Material[] { Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.ACTIVATOR_RAIL, Material.AIR, Material.CARROT, Material.CROPS, Material.DEAD_BUSH, Material.DETECTOR_RAIL, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT, Material.FIRE, Material.GOLD_PLATE, Material.IRON_PLATE, Material.LAVA, Material.LEVER, Material.LONG_GRASS, Material.MELON_STEM, Material.NETHER_WARTS, Material.PORTAL, Material.POTATO, Material.POWERED_RAIL, Material.PUMPKIN_STEM, Material.RAILS, Material.RED_ROSE, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.REDSTONE_WIRE, Material.SAPLING, Material.SEEDS, Material.SIGN, Material.SIGN_POST, Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.STONE_BUTTON, Material.STONE_PLATE, Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN, Material.WATER, Material.WEB, Material.WOOD_BUTTON, Material.WOOD_PLATE, Material.YELLOW_FLOWER, Material.getMaterial("ARMOR_STAND"), Material.getMaterial("BANNER"), Material.getMaterial("STANDING_BANNER"), Material.getMaterial("WALL_BANNER") });
        }
    }
}
