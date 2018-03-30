package pw.skidrevenant.fiona.checks.other;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;

@ChecksListener(events = { PlayerMoveEvent.class, PlayerToggleSneakEvent.class })
public class InvalidPacket extends Checks
{
    public InvalidPacket() {
        super("InvalidPacket", ChecksType.OTHER, Fiona.getAC(), 4, true, true);
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PlayerMoveEvent) {
            final PlayerMoveEvent e = (PlayerMoveEvent)event;
            final Player player = e.getPlayer();
            if (player == null) {
                return;
            }
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user == null || player.getGameMode().equals((Object)GameMode.CREATIVE) || player.getAllowFlight()) {
                return;
            }
            if (user.getCactusTicks() > 4 && MathUtils.elapsed(user.getLastDamage()) > 2000L) {
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.MOVEMENT, 1);
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Cactus " + Color.Gray + "cT: " + Color.Green + user.getCactusTicks() + " ");
            }
        }
        if (event instanceof PlayerToggleSneakEvent) {
            final PlayerToggleSneakEvent e2 = (PlayerToggleSneakEvent)event;
            final Player player = e2.getPlayer();
            if (player.isDead()) {
                return;
            }
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (user.getSneakVL() > 30) {
                user.setSneakVL(0);
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.MOVEMENT, 2);
                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Sneak");
            }
            if (player.isSneaking()) {
                if (user.getSneak1() != 0L) {
                    user.setSneak2(System.currentTimeMillis());
                }
                if (user.getSneak1() == 0L) {
                    user.setSneak1(System.currentTimeMillis());
                }
                if (user.getSneak1() == 0L || user.getSneak2() == 0L) {
                    return;
                }
                final long diff = user.getSneak2() - user.getSneak1();
                if (diff < 150L) {
                    user.setSneakVL(user.getSneakVL() + 1);
                }
                else {
                    user.setSneakVL(0);
                }
                user.setSneak1(0L);
                user.setSneak2(0L);
            }
        }
    }
}
