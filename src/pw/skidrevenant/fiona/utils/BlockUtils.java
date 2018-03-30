package pw.skidrevenant.fiona.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Door;

public class BlockUtils
{
    public static boolean isLiquid(final Block block) {
        return block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA;
    }
    
    public static boolean isClimbableBlock(final Block block) {
        return block.getType() == Material.LADDER || block.getType() == Material.VINE;
    }
    
    public static boolean isFence(final Block block) {
        return block.getType().getId() == 85 || block.getType().getId() == 139 || block.getType().getId() == 113 || block.getTypeId() == 188 || block.getTypeId() == 189 || block.getTypeId() == 190 || block.getTypeId() == 191 || block.getTypeId() == 192;
    }
    
    public static boolean isDoor(final Block block) {
        return block.getType().equals((Object)Material.IRON_DOOR) || block.getType().equals((Object)Material.IRON_DOOR_BLOCK) || block.getType().equals((Object)Material.WOOD_DOOR) || block.getType().equals((Object)Material.WOODEN_DOOR) || block.getTypeId() == 193 || block.getTypeId() == 194 || block.getTypeId() == 195 || block.getTypeId() == 196 || block.getTypeId() == 197 || block.getTypeId() == 324 || block.getTypeId() == 428 || block.getTypeId() == 429 || block.getTypeId() == 430 || block.getTypeId() == 431;
    }
    
    public static boolean pointTwo(final Block block) {
        return block.getType().equals((Object)Material.ENDER_PORTAL_FRAME) || block.getType().equals((Object)Material.CHEST) || block.getType().equals((Object)Material.ENDER_CHEST) || block.getType().equals((Object)Material.TRAPPED_CHEST) || block.getType().equals((Object)Material.SOUL_SAND);
    }
    
    public static boolean pointThreeSeven(final Block block) {
        return block.getType().equals((Object)Material.FLOWER_POT) || block.getType().equals((Object)Material.DAYLIGHT_DETECTOR) || block.getTypeId() == 178;
    }
    
    public static boolean threeQuarters(final Block block) {
        return block.getType().equals((Object)Material.SKULL) || block.getType().equals((Object)Material.COCOA) || block.getType().equals((Object)Material.ENCHANTMENT_TABLE);
    }
    
    public static boolean isBed(final Block block) {
        return block.getType().equals((Object)Material.BED_BLOCK) || block.getType().equals((Object)Material.BED);
    }
    
    public static boolean isTrapDoor(final Block block) {
        final String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        return block.getType().equals((Object)Material.TRAP_DOOR) || block.getTypeId() == 167;
    }
    
    public static boolean isChest(final Block block) {
        return block.getType().equals((Object)Material.TRAPPED_CHEST) || block.getType().equals((Object)Material.CHEST) || block.getType().equals((Object)Material.ENDER_CHEST);
    }
    
    public static boolean isInBlockDimensions(final Location to, final Location from, final Block block) {
        if (isDoor(block)) {
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
            if (facing == BlockFace.EAST && block.getX() >= 0) {
                if (from.getX() > to.getX()) {
                    if (to.getX() < block.getX() + 0.8) {
                        return true;
                    }
                }
                else if (to.getX() > block.getX() + 0.8) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static double getBlockHeight(final Block block) {
        if (block.getTypeId() == 44) {
            return 0.5;
        }
        if (block.getTypeId() == 53) {
            return 0.5;
        }
        if (block.getTypeId() == 85) {
            return 0.2;
        }
        if (block.getTypeId() == 54 || block.getTypeId() == 130) {
            return 0.125;
        }
        return 0.0;
    }
    
    public static boolean isPiston(final Block block) {
        return block.getType().equals((Object)Material.PISTON_MOVING_PIECE) || block.getType().equals((Object)Material.PISTON_EXTENSION) || block.getType().equals((Object)Material.PISTON_BASE) || block.getType().equals((Object)Material.PISTON_STICKY_BASE);
    }
    
    public static boolean isFenceGate(final Block block) {
        return block.getType().equals((Object)Material.FENCE_GATE) || block.getTypeId() == 183 || block.getTypeId() == 184 || block.getTypeId() == 185 || block.getTypeId() == 186 || block.getTypeId() == 187;
    }
    
    public static boolean isOneTwoFive(final Block block) {
        return block.getType().equals((Object)Material.CHEST) || block.getType().equals((Object)Material.TRAPPED_CHEST) || block.getType().equals((Object)Material.SOUL_SAND);
    }
    
    public static boolean isStair(final Block block) {
        return block.getType().equals((Object)Material.ACACIA_STAIRS) || block.getType().equals((Object)Material.BIRCH_WOOD_STAIRS) || block.getType().equals((Object)Material.BRICK_STAIRS) || block.getType().equals((Object)Material.COBBLESTONE_STAIRS) || block.getType().equals((Object)Material.DARK_OAK_STAIRS) || block.getType().equals((Object)Material.NETHER_BRICK_STAIRS) || block.getType().equals((Object)Material.JUNGLE_WOOD_STAIRS) || block.getType().equals((Object)Material.QUARTZ_STAIRS) || block.getType().equals((Object)Material.SMOOTH_STAIRS) || block.getType().equals((Object)Material.WOOD_STAIRS) || block.getType().equals((Object)Material.SANDSTONE_STAIRS) || block.getType().equals((Object)Material.SPRUCE_WOOD_STAIRS) || block.getTypeId() == 203 || block.getTypeId() == 180;
    }
    
    public static boolean isSlab(final Block block) {
        return block.getTypeId() == 44 || block.getTypeId() == 126 || block.getTypeId() == 205 || block.getTypeId() == 182;
    }
    
    public static ArrayList<Block> getSurroundingB(final Block block) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        for (double x = -0.3; x <= 0.3; x += 0.3) {
            for (double y = -0.3; y <= 0.3; y += 0.3) {
                for (double z = -0.3; z <= 0.3; z += 0.3) {
                    if (x != 0.0 || y != 0.0 || z != 0.0) {
                        blocks.add(block.getLocation().add(x, y, z).getBlock());
                    }
                }
            }
        }
        return blocks;
    }
    
    public static ArrayList<Block> getSurroundingC(final Location location) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        for (double x = -0.3; x <= 0.3; x += 0.3) {
            for (double y = -0.3; y <= 0.3; y += 0.3) {
                for (double z = -0.3; z <= 0.3; z += 0.3) {
                    blocks.add(location.add(x, y, z).getBlock());
                }
            }
        }
        return blocks;
    }
    
    public static ArrayList<Block> getSurroundingXZ(final Block block) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(block.getRelative(BlockFace.NORTH));
        blocks.add(block.getRelative(BlockFace.NORTH_EAST));
        blocks.add(block.getRelative(BlockFace.NORTH_WEST));
        blocks.add(block.getRelative(BlockFace.SOUTH));
        blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
        blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
        blocks.add(block.getRelative(BlockFace.EAST));
        blocks.add(block.getRelative(BlockFace.WEST));
        return blocks;
    }
    
    public static boolean isRegularBlock(final Block block) {
        return !isTrapDoor(block) && !isDoor(block) && !isSlab(block) && !isStair(block) && !isFence(block) && !isFenceGate(block) && !isOneTwoFive(block) && !isPiston(block);
    }
    
    public static boolean isEdible(final Material material) {
        return material.equals((Object)Material.COOKED_BEEF) || material.equals((Object)Material.COOKED_CHICKEN) || material.equals((Object)Material.COOKED_FISH) || material.equals((Object)Material.getMaterial("COOKED_MUTTON")) || material.equals((Object)Material.getMaterial("COOKED_RABBIT")) || material.equals((Object)Material.ROTTEN_FLESH) || material.equals((Object)Material.CARROT_ITEM) || material.equals((Object)Material.CARROT) || material.equals((Object)Material.GOLDEN_APPLE) || material.equals((Object)Material.GOLDEN_CARROT) || material.equals((Object)Material.GRILLED_PORK) || material.equals((Object)Material.RAW_BEEF) || material.equals((Object)Material.RAW_CHICKEN) || material.equals((Object)Material.RAW_FISH) || material.equals((Object)Material.SPIDER_EYE) || material.equals((Object)Material.getMaterial("BEETROOT_SOUP")) || material.equals((Object)Material.MUSHROOM_SOUP) || material.equals((Object)Material.POTATO) || material.equals((Object)Material.POTATO_ITEM) || material.equals((Object)Material.BAKED_POTATO) || material.equals((Object)Material.POISONOUS_POTATO) || material.equals((Object)Material.PUMPKIN_PIE) || material.equals((Object)Material.APPLE) || material.equals((Object)Material.getMaterial("MUTTON")) || material.equals((Object)Material.getMaterial("RABBIT")) || material.equals((Object)Material.MELON) || material.equals((Object)Material.getMaterial("CHORUS_FRUIT")) || material.equals((Object)Material.COOKIE) || material.equals((Object)Material.POTION);
    }
    
    public static ArrayList<Block> getSurroundingXZ(final Block block, final boolean diagonals) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.NORTH_EAST));
            blocks.add(block.getRelative(BlockFace.NORTH_WEST));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
            blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        else {
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }
}
