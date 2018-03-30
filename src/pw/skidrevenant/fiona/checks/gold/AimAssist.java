package pw.skidrevenant.fiona.checks.gold;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;

@ChecksListener(events = {})
public class AimAssist extends Checks
{
    public AimAssist() {
        super("AimAssist", ChecksType.GOLD, Fiona.getAC(), 5, true, true);
    }
    
    @EventHandler
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
    }
}
