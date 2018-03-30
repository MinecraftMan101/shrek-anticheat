package pw.skidrevenant.fiona.detections;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.events.FionaFlagEvent;
import pw.skidrevenant.fiona.events.FionaPunishEvent;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.JsonMessage;

public class Checks
{
    private static Fiona ac;
    public ChecksType type;
    private String name;
    private boolean state;
    private boolean bannable;
    private static ArrayList<String> playersToBan;
    private long delay;
    private long interval;
    private double tps;
    private int weight;
    
    public Checks(final String name, final ChecksType type, final Fiona ac, final Integer weight, final boolean state, final boolean bannable) {
        this.delay = -1L;
        this.name = name;
        Checks.ac = ac;
        this.type = type;
        this.weight = weight;
        this.bannable = bannable;
        this.state = state;
        this.interval = 1000L;
        Checks.playersToBan = new ArrayList<String>();
        this.tps = 17.0;
        Fiona.getAC().getChecks().getDetections().add(this);
    }
    
    public double getTps() {
        return this.tps;
    }
    
    public void setTps(final double tps) {
        this.tps = tps;
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    public void setWeight(final int weight) {
        this.weight = weight;
    }
    
    public boolean isBannable() {
        return this.bannable;
    }
    
    public void setBannable(final boolean bannable) {
        this.bannable = bannable;
    }
    
    public void debug(final String string) {
        for (final User user : Fiona.getUserManager().allUsers) {
            if (user.isDebugMode()) {
                user.getPlayer().sendMessage(Color.translate(Color.Aqua + "DEBUG: " + string));
            }
        }
    }
    
    public ChecksType getType() {
        return this.type;
    }
    
    public boolean getState() {
        return this.state;
    }
    
    public void setState(final boolean state) {
        this.state = state;
    }
    
    public void toggle() {
        this.setState(!this.state);
    }
    
    public void toggleBans() {
        this.setBannable(!this.bannable);
    }
    
    public String getName() {
        return this.name;
    }
    
    protected void onEvent(final Event event) {
    }
    
    public void Alert(final Player p, final String value) {
        final FionaFlagEvent e = new FionaFlagEvent(p, this, value);
        Bukkit.getServer().getPluginManager().callEvent((Event)e);
        if (!e.isCancelled() && !Fiona.getAC().whitelisted.contains(p)) {
            if (!Fiona.getAC().getConfig().getBoolean("debug") && !Fiona.getAC().getConfig().getBoolean("testmode")) {
                final long l = System.currentTimeMillis() - this.delay;
                if (l > this.interval) {
                    for (final Player player : Bukkit.getOnlinePlayers()) {
                        if (Fiona.getUserManager().getUser(player.getUniqueId()).isHasAlerts() && (player.isOp() || player.hasPermission("Fiona.staff"))) {
                            final JsonMessage msg = new JsonMessage();
                            msg.addText(Color.translate(Fiona.getAC().getMessages().getString("Alert_Message").replaceAll("%prefix%", Fiona.getAC().getPrefix()).replaceAll("%player%", p.getName()).replaceAll("%check%", this.getName().toUpperCase()).replaceAll("%info%", value).replaceAll("%violations%", String.valueOf(Fiona.getUserManager().getUser(p.getUniqueId()).getVL(this))))).addHoverText(Color.Gray + "Teleport to " + p.getName() + "?").setClickEvent(JsonMessage.ClickableType.RunCommand, "/tp " + p.getName());
                            if (!Fiona.getAC().getConfig().getBoolean("bungee")) {
                                msg.sendToPlayer(player);
                            }
                            else {
                                final ByteArrayOutputStream b = new ByteArrayOutputStream();
                                final DataOutputStream out = new DataOutputStream(b);
                                try {
                                    out.writeUTF(player.getName());
                                    out.writeUTF(this.getName());
                                    out.writeUTF(value);
                                    out.writeUTF(String.valueOf(Fiona.getUserManager().getUser(p.getUniqueId()).getVL(this)));
                                }
                                catch (IOException er) {
                                    er.printStackTrace();
                                }
                                Bukkit.getServer().sendPluginMessage((Plugin)Fiona.getAC(), "FionaAlerts", b.toByteArray());
                            }
                        }
                    }
                    this.delay = System.currentTimeMillis();
                }
            }
            else if (Fiona.getAC().getConfig().getBoolean("testmode")) {
                final JsonMessage msg2 = new JsonMessage();
                msg2.addText(Color.translate(Fiona.getAC().getMessages().getString("Alert_Message").replaceAll("%prefix%", Fiona.getAC().getPrefix()).replaceAll("%player%", p.getName()).replaceAll("%check%", this.getName().toUpperCase()).replaceAll("%info%", value).replaceAll("%violations%", String.valueOf(Fiona.getUserManager().getUser(p.getUniqueId()).getVL(this))))).addHoverText(Color.Gray + "Teleport to " + p.getName() + "?").setClickEvent(JsonMessage.ClickableType.RunCommand, "/tp " + p.getName());
                msg2.sendToPlayer(p);
            }
            else {
                for (final Player player2 : Bukkit.getOnlinePlayers()) {
                    if (Fiona.getUserManager().getUser(player2.getUniqueId()).isHasAlerts() && (player2.isOp() || player2.hasPermission("Fiona.staff"))) {
                        final JsonMessage msg3 = new JsonMessage();
                        msg3.addText(Color.translate(Fiona.getAC().getMessages().getString("Alert_Message").replaceAll("%prefix%", Fiona.getAC().getPrefix()).replaceAll("%player%", p.getName()).replaceAll("%check%", this.getName().toUpperCase()).replaceAll("%info%", value).replaceAll("%violations%", String.valueOf(Fiona.getUserManager().getUser(p.getUniqueId()).getVL(this))))).addHoverText(Color.Gray + "Teleport to " + p.getName() + "?").setClickEvent(JsonMessage.ClickableType.RunCommand, "/tp " + p.getName());
                        if (!Fiona.getAC().getConfig().getBoolean("bungee")) {
                            msg3.sendToPlayer(player2);
                        }
                        else {
                            final ByteArrayOutputStream b2 = new ByteArrayOutputStream();
                            final DataOutputStream out2 = new DataOutputStream(b2);
                            try {
                                out2.writeUTF(p.getName());
                                out2.writeUTF(this.getName());
                                out2.writeUTF(value);
                                out2.writeUTF(String.valueOf(Fiona.getUserManager().getUser(p.getUniqueId()).getVL(this)));
                                Bukkit.getServer().sendPluginMessage((Plugin)Fiona.getAC(), "FionaAlerts", b2.toByteArray());
                                out2.close();
                                b2.close();
                            }
                            catch (IOException er2) {
                                er2.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (Fiona.getAC().getConfig().getBoolean("logs.enabled")) {
                Fiona.getUserManager().getUser(p.getUniqueId()).addToList(p.getName() + " set off check: " + this.getName() + " (" + Fiona.getUserManager().getUser(p.getUniqueId()).getVL(this) + " VL). TPS: " + Fiona.getAC().getPing().getTPS() + " Ping: " + Fiona.getAC().getPing().getPing(p));
            }
        }
    }
    
    public void kick(final Player p) {
        if (Fiona.getUserManager().getUser(p.getUniqueId()).needBan(this) && this.isBannable() && !Fiona.getAC().whitelisted.contains(p)) {
            final FionaPunishEvent e = new FionaPunishEvent(p, this);
            if (!e.isCancelled()) {
                if (!Fiona.getAC().getConfig().getBoolean("testmode") && !Fiona.getAC().playersBanned.contains(p) && !p.isOp()) {
                    if (Fiona.getAC().getConfig().getBoolean("Punish_Broadcast.Enabled")) {
                        Bukkit.broadcastMessage(Color.translate(Fiona.getAC().getMessages().getString("Punish_Broadcast.Message").replaceAll("%player%", p.getName()).replaceAll("%check%", this.getName().toUpperCase())));
                    }
                    if (Fiona.getAC().getConfig().getBoolean("bungee")) {
                        final ByteArrayOutputStream b = new ByteArrayOutputStream();
                        final DataOutputStream out = new DataOutputStream(b);
                        try {
                            out.writeUTF(p.getName());
                            out.writeUTF(this.getName());
                        }
                        catch (IOException er) {
                            er.printStackTrace();
                        }
                        Bukkit.getServer().sendPluginMessage((Plugin)Fiona.getAC(), "FionaBan", b.toByteArray());
                    }
                    Fiona.getAC().playersBanned.add(p);
                    if (Fiona.getAC().getConfig().getBoolean("logs.enabled")) {
                        Fiona.getUserManager().getUser(p.getUniqueId()).addToList(p.getName() + " has been banned for: " + this.getName() + " (" + Fiona.getUserManager().getUser(p.getUniqueId()).getVL(this) + " VL)");
                        Fiona.getAC().writeInLog(Fiona.getUserManager().getUser(p.getUniqueId()));
                    }
                    Fiona.getAC().getServer().dispatchCommand((CommandSender)Fiona.getAC().getServer().getConsoleSender(), Color.translate(Fiona.getAC().getConfig().getString("Punish_Cmd").replaceAll("%player%", p.getName()).replaceAll("%check%", this.getName().toUpperCase())));
                }
                else if (Fiona.getAC().getConfig().getBoolean("testmode")) {
                    p.sendMessage(Fiona.getAC().getPrefix() + Color.Gray + "You would have been punished for: " + Color.Gold + this.getName().toUpperCase());
                    Fiona.getUserManager().getUser(p.getUniqueId()).setVL(this, 0);
                }
            }
        }
    }
}
