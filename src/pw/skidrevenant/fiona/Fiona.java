package pw.skidrevenant.fiona;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import pw.skidrevenant.fiona.commands.AlertsCommand;
import pw.skidrevenant.fiona.commands.FionaCommand;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksManager;
import pw.skidrevenant.fiona.events.EventInventory;
import pw.skidrevenant.fiona.events.EventJoinQuit;
import pw.skidrevenant.fiona.events.EventPacket;
import pw.skidrevenant.fiona.events.EventPacketUse;
import pw.skidrevenant.fiona.events.EventPlayerAttack;
import pw.skidrevenant.fiona.events.EventPlayerInteractEvent;
import pw.skidrevenant.fiona.events.EventPlayerMove;
import pw.skidrevenant.fiona.events.EventPlayerRespawn;
import pw.skidrevenant.fiona.events.EventPlayerVelocity;
import pw.skidrevenant.fiona.events.EventProjectileLaunch;
import pw.skidrevenant.fiona.events.EventTick;
import pw.skidrevenant.fiona.events.TickEvent;
import pw.skidrevenant.fiona.events.TickType;
import pw.skidrevenant.fiona.gui.GUI;
import pw.skidrevenant.fiona.gui.GUIListener;
import pw.skidrevenant.fiona.packets.PacketListeners;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.user.UserManager;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.Messages;
import pw.skidrevenant.fiona.utils.Ping;
import pw.skidrevenant.fiona.utils.PlayerUtils;
import pw.skidrevenant.fiona.utils.TxtFile;

public class Fiona extends JavaPlugin { //memememememe
    private static ChecksManager checksmanager;
    private static Fiona Fiona;
    public PacketListeners packet;
    private static UserManager userManager;
    private Ping ping;
    BufferedWriter bw;
    private Messages msgs;
    private FileConfiguration messages;
    private GUI guiManager;
    private File messagesFile;
    public ArrayList<Player> playersBanned;
    public ArrayList<Player> whitelisted;
    File file;
    
    public Fiona() {
        this.bw = null;
        this.messages = null;
        this.messagesFile = null;
        this.playersBanned = new ArrayList<Player>();
        this.whitelisted = new ArrayList<Player>();
        this.file = new File(this.getDataFolder(), "JD.txt");
    }
    
    public Ping getPing() {
        return this.ping;
    }
    
    public static Fiona getAC() {
        return Fiona;
    }
    
    public ChecksManager getChecks() {
        return checksmanager;
    }
    
    public static UserManager getUserManager() {
        return userManager;
    }
    
    public String getPrefix() {
        return Color.translate(this.getMessages().getString("Prefix"));
    }
    
    public GUI getGUIManager() {
        return this.guiManager;
    }
    
    public void onEnable() {
        this.getServer().getConsoleSender().sendMessage(Color.translate("&d------------------------------------------"));
        this.getServer().getConsoleSender().sendMessage(Color.translate("&d&l FIONA SOURCE BY SKIDREVENANT"));
        Fiona = this;
        userManager = new UserManager();
        this.ping = new Ping(this);
        this.getServer().getConsoleSender().sendMessage(Color.translate("&d Fiona &f Loaded Main class!"));
        checksmanager = new ChecksManager();
        this.getServer().getConsoleSender().sendMessage(Color.translate("&d Fiona &f Loaded checks!"));
        this.getCommand("fiona").setExecutor((CommandExecutor)new FionaCommand());
        this.getCommand("togglealerts").setExecutor((CommandExecutor)new AlertsCommand());
        this.getServer().getConsoleSender().sendMessage(Color.translate("&d Fiona &f Loaded commands!"));
        this.registerConfig();
        this.registerBungee();
        this.getServer().getConsoleSender().sendMessage(Color.translate("&d Fiona &f Loaded Configuration!"));
        this.getServer().getConsoleSender().sendMessage(Color.translate("&d Fiona &f Loaded players data's!"));
        checksmanager.init();
        this.registerEvents();
        this.packet = new PacketListeners();
        this.guiManager = new GUI();
        this.getServer().getConsoleSender().sendMessage(Color.translate("&d Fiona &f Registered events!"));
        this.mkdirs();
        this.loadChecks();
        this.loadUsers();
        this.registerUtils();
        this.getServer().getConsoleSender().sendMessage(Color.translate("&d Fiona &f Loaded Fiona!"));
        this.getServer().getConsoleSender().sendMessage(Color.translate("&d------------------------------------------"));
    }
    
    public void registerUtils() {
        new PlayerUtils();
        this.msgs = new Messages(this);
    }
    
    public void reloadMessages() {
        if (this.messagesFile == null) {
            this.messagesFile = new File(this.getDataFolder(), "messages.yml");
        }
        this.messages = (FileConfiguration)YamlConfiguration.loadConfiguration(this.messagesFile);
        try {
            Reader defConfigStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                this.messages.setDefaults((Configuration)defConfig);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveDefaultConfig() {
        File config = new File(this.getDataFolder(), "config.yml");
        if (!config.exists()) {
            this.saveResource("config.yml", false);
        }
    }
    
    public FileConfiguration getMessages() {
        if (this.messages == null) {
            this.reloadMessages();
        }
        return this.messages;
    }
    
    public void saveMessages() {
        if (this.messages == null || this.messagesFile == null) {
            return;
        }
        try {
            this.getMessages().save(this.messagesFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + this.messagesFile, ex);
        }
    }
    
    public void createMessages() {
        if (this.messagesFile == null) {
            this.messagesFile = new File(this.getDataFolder(), "messages.yml");
        }
        if (!this.messagesFile.exists()) {
            this.saveResource("messages.yml", false);
        }
    }
    
    public void loadChecks() {
        for (Checks check : this.getChecks().getDetections()) {
            if (this.getConfig().get("checks." + check.getName() + ".enabled") != null || this.getConfig().get("checks." + check.getName() + ".bannable") != null) {
                check.setState(this.getConfig().getBoolean("checks." + check.getName() + ".enabled"));
                check.setBannable(this.getConfig().getBoolean("checks." + check.getName() + ".bannable"));
                check.setWeight(this.getConfig().getInt("checks." + check.getName() + ".maxViolations"));
                check.setTps(this.getConfig().getDouble("checks." + check.getName() + ".tpsCancel"));
            } else {
                this.getConfig().set("checks." + check.getName() + ".enabled", (Object)check.getState());
                this.getConfig().set("checks." + check.getName() + ".bannable", (Object)check.isBannable());
                this.getConfig().set("checks." + check.getName() + ".maxViolations", (Object)check.getWeight());
                this.getConfig().set("checks." + check.getName() + ".tpsCancel", (Object)check.getTps());
                if (!check.getName().equalsIgnoreCase("Phase")) {
                    this.getConfig().set("checks." + check.getName() + ".cancelled", (Object)false);
                } else {
                    this.getConfig().set("checks." + check.getName() + ".cancelled", (Object)true);
                }
                this.saveConfig();
            }
        }
    }
    
    public void mkdirs() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
            this.getServer().getConsoleSender().sendMessage(Color.translate("&d Fiona &f Made Fiona file!"));
        }
    }
    
    public void registerBungee() {
        if (this.getConfig().getBoolean("bungee")) {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "FionaBans");
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "FionaAlerts");
        }
    }
    
    public void registerConfig() {
        this.saveDefaultConfig();
        this.createMessages();
    }
    
    public void writeInLog(User user) {
        if (user != null && user.getList() != null && user.getList().size() > 0) {
            TxtFile logFile = new TxtFile(this, File.separator + "logs", user.getPlayer().getName());
            for (String string : user.getList()) {
                logFile.addLine(string);
            }
            user.clearList();
            logFile.write();
        }
    }
    
    public void loadUsers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null) {
                getUserManager().add(new User(player));
            }
        }
    }
    
    public void removeUsers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getUserManager().getUser(player.getUniqueId()) != null) {
                getUserManager().remove(getUserManager().getUser(player.getUniqueId()));
            }
        }
    }
    
    public void clearVLS() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            getUserManager().getUser(online.getUniqueId()).getVLs().clear();
        }
    }
    
    public Messages getMessage() {
        return this.msgs;
    }
    
    public void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new EventPlayerMove(), this);
        pm.registerEvents(new EventPlayerAttack(), this);
        pm.registerEvents(new EventTick(), this);
        pm.registerEvents(new EventJoinQuit(), this);
        pm.registerEvents(new EventPlayerVelocity(), this);
        pm.registerEvents(new EventPlayerInteractEvent(), this);
        pm.registerEvents(new EventPacketUse(), this);
        pm.registerEvents(new EventPacket(), this);
        pm.registerEvents(new EventPlayerRespawn(), this);
        pm.registerEvents(new EventProjectileLaunch(), this);
        pm.registerEvents(new EventInventory(), this);
        pm.registerEvents(new GUIListener(), this);
        new BukkitRunnable() {
            public void run() {
                Fiona.this.clearVLS();
            }
        }.runTaskTimerAsynchronously(this, 0L, 12000L);
        new BukkitRunnable() {
            public void run() {
                Fiona.this.getServer().getPluginManager().callEvent(new TickEvent(TickType.FASTEST));
            }
        }.runTaskTimer(this, 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                Fiona.this.getServer().getPluginManager().callEvent(new TickEvent(TickType.FAST));
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (Checks check : Fiona.this.getChecks().getDetections()) {
                        if (player != null && check != null) {
                            check.kick(player);
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 5L);
        new BukkitRunnable() {
            public void run() {
                Fiona.this.getServer().getPluginManager().callEvent(new TickEvent(TickType.SECOND));
            }
        }.runTaskTimer(this, 0L, 20L);
        new BukkitRunnable() {
            public void run() {
                for (User user : getUserManager().allUsers) {
                    if (user != null) {
                        user.setPosPacket(0);
                        user.setPacketLoss(0);
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, 80L);
        new BukkitRunnable() {
            public void run() {
                if (Fiona.this.getConfig().getBoolean("logs.enabled")) {
                    Fiona.this.getServer().getConsoleSender().sendMessage(Fiona.this.getMessage().LOG_SAVING);
                    if (Fiona.this.getConfig().getBoolean("logs.broadcast")) {
                        Bukkit.broadcast(Fiona.this.getMessage().LOG_SAVING, "fiona.admin");
                    }
                    if (getUserManager().allUsers.size() > 0) {
                        for (User user : getUserManager().allUsers) {
                            if (user != null) {
                                Fiona.this.writeInLog(user);
                            }
                        }
                    }
                    Fiona.this.getServer().getConsoleSender().sendMessage(Fiona.this.getMessage().LOG_SAVED);
                    if (Fiona.this.getConfig().getBoolean("logs.broadcast")) {
                        Bukkit.broadcast(Fiona.this.getMessage().LOG_SAVED, "fiona.admin");
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, 1200L * this.getConfig().getLong("logs.interval"));
    }
}
