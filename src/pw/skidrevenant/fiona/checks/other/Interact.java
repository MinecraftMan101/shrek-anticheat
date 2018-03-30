package pw.skidrevenant.fiona.checks.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;

@ChecksListener(events = { BlockPlaceEvent.class, PlayerInteractEvent.class })
public class Interact extends Checks
{
    private Map<UUID, Integer> verbose;
    
    public Interact() {
        super("Interact", ChecksType.OTHER, Fiona.getAC(), 100, true, false);
        this.verbose = new HashMap<UUID, Integer>();
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerInteractEvent) {
            final PlayerInteractEvent e = (PlayerInteractEvent)event;
            if (e.isCancelled() || (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_BLOCK)) {
                return;
            }
            boolean isValid = false;
            final Player player = e.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
            final Location scanLocation = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();
            final double x = scanLocation.getX();
            final double y = scanLocation.getY();
            final double z = scanLocation.getZ();
            for (double sX = x; sX < x + 2.0; ++sX) {
                for (double sY = y; sY < y + 2.0; ++sY) {
                    for (double sZ = z; sZ < z + 2.0; ++sZ) {
                        final Location relative = new Location(scanLocation.getWorld(), sX, sY, sZ);
                        final List<Location> blocks = this.rayTrace(player.getLocation(), relative);
                        boolean valid = true;
                        for (final Location l : blocks) {
                            if (!this.checkPhase(l.getBlock().getType())) {
                                valid = false;
                            }
                        }
                        if (valid) {
                            isValid = true;
                        }
                    }
                }
            }
            if (!isValid && !e.getItem().getType().equals((Object)Material.ENDER_PEARL)) {
                ++verbose;
                user.setCancelled(this, CancelType.INTERACT);
            }
            else {
                verbose = ((verbose > 0) ? (verbose - 1) : 0);
            }
            if (verbose > 2) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.INTERACT);
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Interact");
            }
            this.verbose.put(player.getUniqueId(), verbose);
        }
        if (event instanceof BlockPlaceEvent) {
            final BlockPlaceEvent e2 = (BlockPlaceEvent)event;
            if (e2.isCancelled()) {
                return;
            }
            final Player player2 = e2.getPlayer();
            final User user2 = Fiona.getUserManager().getUser(player2.getUniqueId());
            int verbose2 = this.verbose.getOrDefault(player2.getUniqueId(), 0);
            if (user2.getLastBlockPlaced() == null) {
                return;
            }
            if (e2.getBlockPlaced().getLocation().distance(user2.getLastBlockPlaced().getLocation()) < 1.2 && e2.getPlayer().getLocation().getBlockY() - e2.getBlock().getLocation().getY() == 1.0 && e2.getPlayer().getLocation().clone().subtract(0.0, 1.0, 0.0).distance(e2.getBlock().getLocation()) > 2.0) {
                if (++verbose2 > 2) {
                    user2.setCancelled(this, CancelType.BLOCK);
                }
            }
            else {
                verbose2 = ((verbose2 > 0) ? (verbose2 - 1) : verbose2);
            }
            if (verbose2 > 5) {
                user2.setVL(this, user2.getVL(this) + 1);
                user2.setCancelled(this, CancelType.BLOCK);
                this.Alert(player2, Color.Gray + "Reason: " + Color.Green + "PLACE_REACH");
            }
            this.verbose.put(player2.getUniqueId(), verbose2);
        }
    }
    
    private List<Location> rayTrace(final Location from, final Location to) {
        final List<Location> a = new ArrayList<Location>();
        if (from == null || to == null) {
            return a;
        }
        if (!from.getWorld().equals(to.getWorld())) {
            return a;
        }
        if (from.distance(to) > 10.0) {
            return a;
        }
        double x1 = from.getX();
        double y1 = from.getY() + 1.62;
        double z1 = from.getZ();
        final double x2 = to.getX();
        final double y2 = to.getY();
        final double z2 = to.getZ();
        for (boolean scanning = true; scanning; scanning = false) {
            a.add(new Location(from.getWorld(), x1, y1, z1));
            x1 += (x2 - x1) / 10.0;
            y1 += (y2 - y1) / 10.0;
            z1 += (z2 - z1) / 10.0;
            if (Math.abs(x1 - x2) < 0.01 && Math.abs(y1 - y2) < 0.01 && Math.abs(z1 - z2) < 0.01) {}
        }
        return a;
    }
    
    public boolean checkPhase(final Material m) {
        final int[] array;
        final int[] whitelist = array = new int[] { 355, 196, 194, 197, 195, 193, 64, 96, 187, 184, 186, 107, 185, 183, 192, 189, 139, 191, 85, 101, 190, 113, 188, 160, 102, 163, 157, 0, 145, 49, 77, 135, 108, 67, 164, 136, 114, 156, 180, 128, 143, 109, 134, 53, 126, 44, 416, 8, 425, 138, 26, 397, 372, 13, 135, 117, 108, 39, 81, 92, 71, 171, 141, 118, 144, 54, 139, 67, 127, 59, 115, 330, 164, 151, 178, 32, 28, 93, 94, 175, 122, 116, 130, 119, 120, 51, 140, 147, 154, 148, 136, 65, 10, 69, 31, 105, 114, 372, 33, 34, 36, 29, 90, 142, 27, 104, 156, 66, 40, 330, 38, 180, 149, 150, 75, 76, 55, 128, 6, 295, 323, 63, 109, 78, 88, 134, 176, 11, 9, 44, 70, 182, 83, 50, 146, 132, 131, 106, 177, 68, 8, 111, 30, 72, 53, 126, 37 };
        for (final int ids : array) {
            if (m.getId() == ids) {
                return true;
            }
        }
        return false;
    }
}
