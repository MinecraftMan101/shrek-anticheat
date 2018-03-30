package pw.skidrevenant.fiona.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import pw.skidrevenant.fiona.Fiona;

public class EventProjectileLaunch implements Listener
{
    @EventHandler
    public void onLaunch(final ProjectileLaunchEvent event) {
        Fiona.getAC().getChecks().event((Event)event);
    }
}
