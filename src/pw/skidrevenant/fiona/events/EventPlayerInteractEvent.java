package pw.skidrevenant.fiona.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.Vector;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.BlockUtils;
import pw.skidrevenant.fiona.utils.CancelType;

public class EventPlayerInteractEvent implements Listener
{
    private final ImmutableSet<Material> blockedPearlTypes;
    private boolean glitch;
    
    public EventPlayerInteractEvent() {
        this.blockedPearlTypes = (ImmutableSet<Material>)Sets.immutableEnumSet((Enum)Material.THIN_GLASS, (Enum[])new Material[] { Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE, Material.FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS });
        this.glitch = true;
        this.glitch = Fiona.getAC().getConfig().getBoolean("PearlFix.glitchFix");
    }
    
    @EventHandler
    public void onMove(final PlayerInteractEvent event) {
        Fiona.getAC().getChecks().event((Event)event);
        final Player p = event.getPlayer();
        final User user = Fiona.getUserManager().getUser(p.getUniqueId());
        if (Fiona.getAC().getPing().getTPS() < 16.0) {
            return;
        }
        if (user != null) {
            if (event.getItem() != null && event.getItem().getType().equals((Object)Material.BOW)) {
                user.setLastBow(System.currentTimeMillis());
            }
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                user.setLeftClicks(user.getLeftClicks() + 1);
            }
            else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                user.setRightClicks(user.getRightClicks() + 1);
            }
            if (user.isCancelled() == CancelType.INTERACT) {
                event.setCancelled(true);
                user.setCancelled(null, CancelType.NONE);
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem() && event.getItem().getType() == Material.ENDER_PEARL && this.glitch) {
                final Block block = event.getClickedBlock();
                if (block.getType().isSolid() && this.blockedPearlTypes.contains((Object)block.getType()) && !(block.getState() instanceof InventoryHolder)) {
                    final PearlFixEvent event2 = new PearlFixEvent(event.getPlayer(), PearlFixType.GLITCH);
                    Bukkit.getPluginManager().callEvent((Event)event2);
                    if (!event2.isCancelled()) {
                        event.setCancelled(true);
                        final Player player = event.getPlayer();
                        player.setItemInHand(event.getItem());
                    }
                }
            }
            if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && (BlockUtils.isEdible(event.getItem().getType()) || event.getItem().getType().equals((Object)Material.BOW))) {
                user.setFoodClick(System.currentTimeMillis());
            }
        }
    }
    
    @EventHandler
    public void potionSplash(final EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getRegainReason() != EntityRegainHealthEvent.RegainReason.MAGIC) {
            return;
        }
        final Player player = (Player)event.getEntity();
        final User user = Fiona.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            user.setLastPotionSplash(System.currentTimeMillis());
        }
    }
    
    @EventHandler
    public void inventoryInteract(final InventoryInteractEvent event) {
        Fiona.getAC().getChecks().event((Event)event);
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e) {
        Fiona.getAC().getChecks().event((Event)e);
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        Fiona.getAC().getChecks().event((Event)event);
        final User user = Fiona.getUserManager().getUser(event.getPlayer().getUniqueId());
        if (user != null) {
            if (user.isCancelled() == CancelType.BLOCK) {
                event.setCancelled(true);
                user.setCancelled(null, CancelType.NONE);
                return;
            }
            user.setLastBlockPlaced(user.getBlockPlaced());
            user.setBlockPlaced(event.getBlockPlaced());
            user.setLastBlockPlace(System.currentTimeMillis());
        }
        if (event.isCancelled()) {
            event.getPlayer().setVelocity(new Vector(0, -1, 0));
        }
        if (event.getBlockReplacedState().getBlock().getType() == Material.WATER && user != null) {
            user.setPlacedBlock(true);
        }
    }
}
