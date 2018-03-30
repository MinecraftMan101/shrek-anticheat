package pw.skidrevenant.fiona;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.entity.Player;

import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksManager;
import pw.skidrevenant.fiona.user.User;

public class FionaAPI {
    private static FionaAPI api;
    
    public FionaAPI() {
        FionaAPI.api = new FionaAPI();
    }
    
    public static FionaAPI getAPI() {
        return FionaAPI.api;
    }
    
    public Map<Checks, Integer> getViolations(Player player) {
        return Fiona.getUserManager().getUser(player.getUniqueId()).getVLs();
    }
    
    public void clearViolations(Player player) {
        Fiona.getUserManager().getUser(player.getUniqueId()).getVLs().clear();
    }
    
    public void clearAllViolations() {
        Fiona.getAC().clearVLS();
    }
    
    public Checks getLastViolation(Player player) {
        User user = Fiona.getUserManager().getUser(player.getUniqueId());
        if (user.getVLs().size() > 0) {
            return (Checks)user.getVLs().keySet().toArray()[user.getVLs().keySet().size() - 1];
        }
        return null;
    }
    
    public void addCheck(Checks check) {
        ChecksManager.detections.add(check);
    }
    
    public Checks getCheckByName(String string) {
        return Fiona.getAC().getChecks().getCheckByName(string);
    }
    
    public void removeCheck(Checks check) {
        ChecksManager.detections.remove(check);
    }
    
    public ArrayList<Player> getPlayersBanned() {
        return Fiona.getAC().playersBanned;
    }
    
    public double getTPS() {
        return Fiona.getAC().getPing().getTPS();
    }
    
    public int getPing(Player player) {
        return Fiona.getAC().getPing().getPing(player);
    }
    
    public boolean hasAlerts(Player player) {
        return Fiona.getUserManager().getUser(player.getUniqueId()).isHasAlerts();
    }
    
    public String getVersion() {
        return Fiona.getAC().getDescription().getVersion();
    }
}
