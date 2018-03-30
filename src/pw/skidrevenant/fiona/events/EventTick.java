package pw.skidrevenant.fiona.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import pw.skidrevenant.fiona.Fiona;

public class EventTick implements Listener
{
    @EventHandler
    public void onEvent(final TickEvent event) {
        Fiona.getAC().getChecks().event(event);
    }
}
