package pw.skidrevenant.fiona.checks.movement;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;

@ChecksListener(events = { PlayerMoveEvent.class })
public class Vclip extends Checks
{
    private List<Material> allowed;
    
    public Vclip() {
        super("Vclip", ChecksType.MOVEMENT, Fiona.getAC(), 5, true, false);
        (this.allowed = new ArrayList<Material>()).add(Material.PISTON_EXTENSION);
        this.allowed.add(Material.PISTON_STICKY_BASE);
        this.allowed.add(Material.PISTON_BASE);
        this.allowed.add(Material.SIGN_POST);
        this.allowed.add(Material.WALL_SIGN);
        this.allowed.add(Material.STRING);
        this.allowed.add(Material.AIR);
        this.allowed.add(Material.FENCE_GATE);
        this.allowed.add(Material.THIN_GLASS);
        this.allowed.add(Material.STAINED_GLASS_PANE);
        this.allowed.add(Material.CHEST);
        this.allowed.add(Material.FENCE);
        this.allowed.add(Material.NETHER_FENCE);
        this.setTps(0.0);
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e = (PlayerMoveEvent)event;
            final Player p = e.getPlayer();
            final Location to = e.getTo().clone();
            final Location from = e.getFrom().clone();
            if (from.getY() == to.getY() || p.getAllowFlight() || p.getVehicle() != null || e.getTo().getY() <= 0.0 || e.getTo().getY() >= p.getWorld().getMaxHeight() || p.getLocation().getY() < 0.0 || p.getLocation().getY() > p.getWorld().getMaxHeight()) {
                return;
            }
            final User user = Fiona.getUserManager().getUser(p.getUniqueId());
            final double yDist = from.getBlockY() - to.getBlockY();
            if (yDist < 1.0) {
                return;
            }
            for (double y = 0.0; y < Math.abs(yDist); ++y) {
                final Location l = (yDist < -0.2) ? from.getBlock().getLocation().add(0.0, y, 0.0) : to.getBlock().getLocation().clone().add(0.0, y, 0.0);
                if ((yDist > 20.0 || yDist < -20.0) && l.getBlock().getType() != Material.AIR && l.getBlock().getType().isSolid() && !this.allowed.contains(l.getBlock().getType())) {
                    p.kickPlayer("No");
                    user.setVL(this, user.getVL(this) + 1);
                    this.Alert(p, "*");
                    p.teleport(from);
                    return;
                }
                if (l.getBlock().getType() != Material.AIR && Math.abs(yDist) > 1.0 && l.getBlock().getType().isSolid() && !this.allowed.contains(l.getBlock().getType())) {
                    user.setVL(this, user.getVL(this) + 1);
                    this.Alert(p, "*");
                    p.teleport(from);
                }
            }
        }
    }
}
