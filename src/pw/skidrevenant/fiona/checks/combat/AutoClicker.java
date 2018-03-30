package pw.skidrevenant.fiona.checks.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;

@ChecksListener(events = { PlayerInteractEvent.class, PlayerQuitEvent.class })
public class AutoClicker extends Checks
{
    private double thresholdToAlert;
    private double thresholdToAddVL;
    private Map<UUID, Integer> verbose;
    private Map<UUID, Long> time;
    
    public AutoClicker() {
        super("AutoClicker", ChecksType.COMBAT, Fiona.getAC(), 6, true, true);
        this.thresholdToAlert = 20.0;
        this.thresholdToAddVL = 30.0;
        this.thresholdToAlert = Fiona.getAC().getConfig().getDouble("checks.AutoClicker.thresholdToAlert");
        this.thresholdToAddVL = Fiona.getAC().getConfig().getDouble("checks.AutoClicker.thresholdToAddVL");
        this.verbose = new HashMap<UUID, Integer>();
        this.time = new HashMap<UUID, Long>();
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e = (PlayerQuitEvent)event;
            final UUID uuid = e.getPlayer().getUniqueId();
            if (this.verbose.containsKey(uuid)) {
                this.verbose.remove(uuid);
            }
            if (this.time.containsKey(uuid)) {
                this.time.remove(uuid);
            }
        }
        else if (event instanceof PlayerInteractEvent) {
            final PlayerInteractEvent e2 = (PlayerInteractEvent)event;
            if (e2.getAction() != Action.LEFT_CLICK_AIR) {
                return;
            }
            final Player player = e2.getPlayer();
            if (player == null) {
                return;
            }
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (Fiona.getAC().getPing().getTPS() < this.getTps() || Fiona.getAC().getPing().getPing(player) > 800L) {
                return;
            }
            int clicks = this.verbose.getOrDefault(player.getUniqueId(), 0);
            long time = this.time.getOrDefault(player.getUniqueId(), System.currentTimeMillis());
            if (MathUtils.elapsed(time, 1000L)) {
                if (clicks > this.thresholdToAddVL) {
                    user.setVL(this, user.getVL(this) + 1);
                    user.setCancelled(this, CancelType.COMBAT);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Definite " + Color.Gray + " CPS: " + Color.Green + clicks);
                }
                else if (clicks > this.thresholdToAlert) {
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Likely " + Color.Gray + " CPS: " + Color.Green + clicks);
                }
                clicks = 0;
                time = System.currentTimeMillis();
            }
            ++clicks;
            this.verbose.put(player.getUniqueId(), clicks);
            this.time.put(player.getUniqueId(), time);
        }
    }
}
