package pw.skidrevenant.fiona.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import pw.skidrevenant.fiona.Fiona;

public class EventInventory implements Listener
{
    @EventHandler
    public void onclose(final InventoryCloseEvent e) {
        Fiona.getAC().getChecks().event((Event)e);
    }
    
    @EventHandler
    public void onopen(final InventoryOpenEvent e) {
        Fiona.getAC().getChecks().event((Event)e);
    }
}
