package pw.skidrevenant.fiona.checks.movement;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.PlayerUtils;

@ChecksListener(events = { PlayerInteractEvent.class, PlayerItemConsumeEvent.class, ProjectileLaunchEvent.class, PlayerMoveEvent.class, PlayerQuitEvent.class })
public class NoSlowDown extends Checks
{
    private Map<UUID, Map.Entry<Integer, Long>> speedTicks;
    private Map<UUID, Integer> soulVerbose;
    
    public NoSlowDown() {
        super("NoSlowDown", ChecksType.MOVEMENT, Fiona.getAC(), 100, true, true);
        this.speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.soulVerbose = new HashMap<UUID, Integer>();
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e = (PlayerQuitEvent)event;
            final UUID uuid = e.getPlayer().getUniqueId();
            if (this.speedTicks.containsKey(uuid)) {
                this.speedTicks.remove(uuid);
            }
            if (this.soulVerbose.containsKey(uuid)) {
                this.soulVerbose.remove(uuid);
            }
        }
        if (event instanceof PlayerInteractEvent) {
            final PlayerInteractEvent e2 = (PlayerInteractEvent)event;
            if ((e2.getAction() == Action.RIGHT_CLICK_AIR || e2.getAction() == Action.RIGHT_CLICK_BLOCK) && e2.getItem() != null) {
                if (e2.getItem().getType().equals((Object)Material.EXP_BOTTLE) || e2.getItem().getType().equals((Object)Material.GLASS_BOTTLE) || e2.getItem().getType().equals((Object)Material.POTION) || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                    return;
                }
                final Player player = e2.getPlayer();
                final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                if (System.currentTimeMillis() - user.getLastBlockPlace() < 500L) {
                    return;
                }
                long Time = System.currentTimeMillis();
                int level = 0;
                if (this.speedTicks.containsKey(player.getUniqueId())) {
                    level = this.speedTicks.get(player.getUniqueId()).getKey();
                    Time = this.speedTicks.get(player.getUniqueId()).getValue();
                }
                final double diff = System.currentTimeMillis() - Time;
                int n;
                if (diff >= 2.0) {
                    if (diff <= 51.0) {
                        level += 2;
                        n = level;
                    }
                    else if (diff <= 100.0) {
                        level += 0;
                        n = level;
                    }
                    else if (diff <= 500.0) {
                        level -= 6;
                        n = level;
                    }
                    else {
                        level -= 12;
                        n = level;
                    }
                }
                else {
                    n = ++level;
                }
                level = n;
                final int max = 50;
                if (level > max * 0.9 && diff <= 100.0) {
                    user.setVL(this, user.getVL(this) + 1);
                    user.setCancelled(this, CancelType.MOVEMENT);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "BlockPatterns");
                    if (level > max) {
                        level = max / 4;
                    }
                }
                else if (level < 0) {
                    level = 0;
                }
                this.speedTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(level, System.currentTimeMillis()));
            }
        }
        if (event instanceof PlayerItemConsumeEvent) {
            final PlayerItemConsumeEvent e3 = (PlayerItemConsumeEvent)event;
            final Player player = e3.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user == null) {
                return;
            }
            if (user.getFoodSprintTicks() > 22) {
                user.setVL(this, user.getVL(this) + 2);
                user.setCancelled(this, CancelType.MOVEMENT, 2);
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Edible " + Color.Gray + "t: " + Color.Green + user.getFoodSprintTicks());
            }
        }
        if (event instanceof ProjectileLaunchEvent) {
            final ProjectileLaunchEvent e4 = (ProjectileLaunchEvent)event;
            if (e4.getEntity() instanceof Arrow) {
                final Arrow arrow = (Arrow)e4.getEntity();
                if (!(arrow.getShooter() instanceof Player) || arrow.getShooter() == null || arrow.getVelocity().length() < 0.12 || Fiona.getAC().getPing().getTPS() < this.getTps()) {
                    return;
                }
                final Player player2 = (Player)arrow.getShooter();
                final User user2 = Fiona.getUserManager().getUser(player2.getUniqueId());
                if (user2.getFoodSprintTicks() > 7) {
                    user2.setVL(this, user2.getVL(this) + 2);
                    user2.setCancelled(this, CancelType.MOVEMENT, 2);
                    this.Alert(player2, Color.Gray + "Reason: " + Color.Green + "Bow " + Color.Gray + "t: " + Color.Green + user2.getFoodSprintTicks());
                }
            }
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e5 = (PlayerMoveEvent)event;
            final Player player = e5.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user == null || (e5.getFrom().getX() == e5.getTo().getX() && e5.getFrom().getY() == e5.getTo().getY() && e5.getFrom().getZ() == e5.getTo().getZ())) {
                return;
            }
            final int soulTicks = user.getSoulSandTicks();
            final int webTicks = user.getWebTicks();
            int verbose = this.soulVerbose.getOrDefault(player.getUniqueId(), 0);
            if (soulTicks > 8 && PlayerUtils.isOnGroundFP(player)) {
                final double horizontal = Math.sqrt(Math.pow(e5.getTo().getX() - e5.getFrom().getX(), 2.0) + Math.pow(e5.getTo().getZ() - e5.getFrom().getZ(), 2.0));
                if (horizontal > 0.141) {
                    ++verbose;
                }
                else {
                    verbose = 0;
                }
                if (verbose > 7) {
                    user.setVL(this, user.getVL(this) + 1);
                    verbose = 4;
                    user.setCancelled(this, CancelType.MOVEMENT, 3);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Soulsand " + Color.Gray + "v: " + Color.Green + MathUtils.round(horizontal, 4));
                }
            }
            else if (webTicks > 10) {
                final double horizontal = Math.sqrt(Math.pow(e5.getTo().getX() - e5.getFrom().getX(), 2.0) + Math.pow(e5.getTo().getZ() - e5.getFrom().getZ(), 2.0));
                if (horizontal > 0.141) {
                    ++verbose;
                }
                else {
                    verbose = 0;
                }
                if (verbose > 10) {
                    user.setVL(this, user.getVL(this) + 1);
                    verbose = 6;
                    user.setCancelled(this, CancelType.MOVEMENT, 1);
                    this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Web " + Color.Gray + "v: " + Color.Green + MathUtils.round(horizontal, 4));
                }
            }
            else if (verbose > 0 && soulTicks <= 8) {
                verbose = 0;
            }
            this.soulVerbose.put(player.getUniqueId(), verbose);
        }
    }
}
