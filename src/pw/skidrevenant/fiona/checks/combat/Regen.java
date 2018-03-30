package pw.skidrevenant.fiona.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;

@ChecksListener(events = { PlayerQuitEvent.class, EntityRegainHealthEvent.class })
public class Regen extends Checks
{
    public Map<UUID, Integer> verbose;
    private boolean cancelled;
    
    public Regen() {
        super("Regen", ChecksType.COMBAT, Fiona.getAC(), 9, true, true);
        this.cancelled = false;
        this.verbose = new HashMap<UUID, Integer>();
        this.cancelled = Fiona.getAC().getConfig().getBoolean("checks.Regen.cancelled");
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e = (PlayerQuitEvent)event;
            final Player player = e.getPlayer();
            if (this.verbose.containsKey(player.getUniqueId())) {
                this.verbose.remove(player.getUniqueId());
            }
        }
        if (event instanceof EntityRegainHealthEvent) {
            final EntityRegainHealthEvent e2 = (EntityRegainHealthEvent)event;
            if (!e2.getRegainReason().equals((Object)EntityRegainHealthEvent.RegainReason.SATIATED) || !(e2.getEntity() instanceof Player) || Fiona.getAC().getPing().getTPS() < this.getTps() || e2.isCancelled()) {
                return;
            }
            final Player player = (Player)e2.getEntity();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
            if (MathUtils.elapsed(user.getLastRegen()) < 3000L) {
                ++verbose;
            }
            else {
                verbose = 0;
            }
            if (verbose > 2) {
                user.setVL(this, user.getVL(this) + 1);
                verbose = 2;
                if (this.cancelled) {
                    e2.setCancelled(true);
                }
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "FastHeal " + Color.Gray + "T: " + Color.Green + MathUtils.elapsed(user.getLastRegen()));
            }
            user.setLastRegen(System.currentTimeMillis());
            this.verbose.put(player.getUniqueId(), verbose);
        }
    }
}
