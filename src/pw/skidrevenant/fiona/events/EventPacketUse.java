package pw.skidrevenant.fiona.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.packets.events.PacketKillauraEvent;
import pw.skidrevenant.fiona.packets.events.PacketTypes;
import pw.skidrevenant.fiona.user.User;

public class EventPacketUse implements Listener
{
    @EventHandler
    public void onUse(final PacketKillauraEvent e) {
        Fiona.getAC().getChecks().event(e);
        final User user = Fiona.getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user != null && e.getType() == PacketTypes.USE) {
            user.setLastAttack(System.currentTimeMillis());
        }
    }
}
