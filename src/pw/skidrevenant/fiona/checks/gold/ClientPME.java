package pw.skidrevenant.fiona.checks.gold;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.Color;

public class ClientPME extends Checks implements PluginMessageListener
{
    public ClientPME() {
        super("ClientPME", ChecksType.GOLD, Fiona.getAC(), 1, true, true);
        Fiona.getAC().getServer().getMessenger().registerIncomingPluginChannel((Plugin)Fiona.getAC(), "LOLIMAHCKER", (PluginMessageListener)this);
        Fiona.getAC().getServer().getMessenger().registerIncomingPluginChannel((Plugin)Fiona.getAC(), "cock", (PluginMessageListener)this);
        Fiona.getAC().getServer().getMessenger().registerIncomingPluginChannel((Plugin)Fiona.getAC(), "customGuiOpenBspkrs", (PluginMessageListener)this);
        Fiona.getAC().getServer().getMessenger().registerIncomingPluginChannel((Plugin)Fiona.getAC(), "0SO1Lk2KASxzsd", (PluginMessageListener)this);
        Fiona.getAC().getServer().getMessenger().registerIncomingPluginChannel((Plugin)Fiona.getAC(), "lmaohax", (PluginMessageListener)this);
    }
    
    public void onPluginMessageReceived(final String pluginMessage, final Player player, final byte[] bytes) {
        if (pluginMessage == null || !this.getState()) {
            return;
        }
        String log = null;
        switch (pluginMessage) {
            case "LOLIMAHCKER": {
                log = "Client #1";
                break;
            }
            case "cock": {
                log = "Client #2";
                break;
            }
            case "customGuiOpenBspkrs": {
                log = "Client #3";
                break;
            }
            case "0SO1Lk2KASxzsd": {
                log = "Client #4";
                break;
            }
            case "lmaohax": {
                log = "Cient #5";
                break;
            }
            default: {
                return;
            }
        }
        final User user = Fiona.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            System.out.print("Null error with player: " + player.getName() + ". Would have set of check: ClientPME.");
            return;
        }
        user.setVL(this, user.getVL(this) + 2);
        this.Alert(player, Color.Gray + "Reason: " + Color.Green + log);
    }
}
