package pw.skidrevenant.fiona.checks.other;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.packets.events.PacketEvent;
import pw.skidrevenant.fiona.packets.events.PacketTypes;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;

@ChecksListener(events = { PacketEvent.class })
public class ImpossiblePitch extends Checks
{
    public ImpossiblePitch() {
        super("ImpossiblePitch", ChecksType.OTHER, Fiona.getAC(), 6, true, true);
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PacketEvent) {
            final PacketEvent e = (PacketEvent)event;
            if (e.getType() != PacketTypes.LOOK) {
                return;
            }
            final Player player = e.getPlayer();
            if (player == null) {
                return;
            }
            if (e.getPitch() > 90.1f || e.getPitch() < -90.1f) {
                final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                user.setVL(this, user.getVL(this) + 1);
                user.setCancelled(this, CancelType.MOVEMENT);
                this.Alert(player, "*");
            }
        }
    }
}
