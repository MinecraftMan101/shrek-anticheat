package pw.skidrevenant.fiona.checks.gold;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

import org.bukkit.event.player.PlayerQuitEvent;
import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.packets.events.PacketEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ChecksListener(events = {})
public class AimAssist extends Checks
{

    private Map n = new HashMap();
    private Map o = new HashMap();
    private Map p = new HashMap();
    private Map q = new HashMap();
    private Map d = new HashMap();


    public AimAssist() {
        super("AimAssist", ChecksType.GOLD, Fiona.getAC(), 5, true, true);
    }
    
    @EventHandler
    @Override
    protected void onEvent(final Event event) {
        if (this.getState()) {
            if (event instanceof PlayerQuitEvent) {
                PlayerQuitEvent var2 = (PlayerQuitEvent)event;
                UUID var3 = var2.getPlayer().getUniqueId();
                if (this.n.containsKey(var3)) {
                    this.n.remove(var3);
                }

                if (this.o.containsKey(var3)) {
                    this.o.remove(var3);
                }

                if (this.p.containsKey(var3)) {
                    this.p.remove(var3);
                }

                if (this.q.containsKey(var3)) {
                    this.q.remove(var3);
                }

                if (this.d.containsKey(var3)) {
                    this.d.remove(var3);
                }
            }

            if (event instanceof PacketEvent) {
                final PacketEvent e3 = (PacketEvent) event;


                int var6 = ((Integer)this.o.getOrDefault(((PacketEvent) event).getPlayer().getUniqueId(), Integer.valueOf(0))).intValue();

            }
        }
    }
}
