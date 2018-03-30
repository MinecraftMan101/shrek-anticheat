package pw.skidrevenant.fiona.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.ProtocolLibrary;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.packets.PacketListeners;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.Messages;

public class FionaCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("fiona.staff")) {
            sender.sendMessage(Color.Red + "This server is using Fiona " + Fiona.getAC().getDescription().getVersion() + " by funkemunky and XTasyCode");
            return true;
        }
        if (args.length == 0) {
            if (sender instanceof Player) {
                if (sender.hasPermission("fiona.admin")) {
                    final Player player = (Player)sender;
                    Fiona.getAC().getGUIManager().openMainGUI(player);
                    player.sendMessage(Color.Green + "Opened GUI.");
                }
                else {
                    sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona toggle <CheckName> to enable/disable checks.");
                    if (sender instanceof Player) {
                        sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona Alerts on/off to enable/disable alerts.");
                    }
                    sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona bannable <checkName> to make a check bannable/silent.");
                    sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona status to check the current Fiona status.");
                    sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona verbose <player> to see a player's violations.");
                    sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona reload to reload the plugin.");
                    sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona whitelist [add/remove] <player> to add a player to the ignore list.");
                }
            }
            else {
                sender.sendMessage(Color.Red + "You must be a player to open the GUI. Do /fiona help for the commands a non-player can do.");
            }
        }
        else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("fiona.admin")) {
                    sender.sendMessage(Fiona.getAC().getMessage().RELOADING_CONFIG);
                    Fiona.getAC().reloadConfig();
                    Fiona.getAC().reloadMessages();
                    new Messages(Fiona.getAC());
                    sender.sendMessage(Fiona.getAC().getMessage().RELOADING_PARTLY_DONE);
                    sender.sendMessage(Fiona.getAC().getMessage().RELOADING_PLUGIN);
                    HandlerList.unregisterAll((Plugin)Fiona.getAC());
                    Fiona.getAC().getServer().getScheduler().cancelAllTasks();
                    Fiona.getAC().registerEvents();
                    ProtocolLibrary.getProtocolManager().getPacketListeners().forEach(ProtocolLibrary.getProtocolManager()::removePacketListener);
                    new PacketListeners();
                    sender.sendMessage(Fiona.getAC().getMessage().RELOADING_PARTLY_DONE);
                    sender.sendMessage(Fiona.getAC().getMessage().RELOADING_CHECKS);
                    Fiona.getAC().getChecks().getDetections().clear();
                    Fiona.getAC().getChecks().init();
                    Fiona.getAC().loadChecks();
                    sender.sendMessage(Fiona.getAC().getMessage().RELOADING_PARTLY_DONE);
                    sender.sendMessage(Fiona.getAC().getMessage().RELOADING_VIOLATIONS);
                    Fiona.getAC().clearVLS();
                    sender.sendMessage(Fiona.getAC().getMessage().RELOADING_PARTLY_DONE);
                    sender.sendMessage(Fiona.getAC().getMessage().RELOADING_DONE);
                }
                else {
                    sender.sendMessage(Fiona.getAC().getMessage().NO_PERMISSION);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("whitelist")) {
                if (sender.hasPermission("fiona.admin")) {
                    if (args.length == 3) {
                        if (args[1].equalsIgnoreCase("add")) {
                            final Player whitelisted = Bukkit.getPlayer(args[2]);
                            if (whitelisted == null) {
                                sender.sendMessage(Color.Red + "That player is not online!");
                                return true;
                            }
                            if (!Fiona.getAC().whitelisted.contains(whitelisted)) {
                                Fiona.getAC().whitelisted.add(whitelisted);
                                sender.sendMessage(Fiona.getAC().getPrefix() + Color.Green + "Added " + whitelisted.getName() + " to the ignore list.");
                            }
                            else {
                                sender.sendMessage(Color.Red + "That player is already whitelisted!");
                            }
                        }
                        else if (args[1].equalsIgnoreCase("remove")) {
                            final Player whitelisted = Bukkit.getPlayer(args[2]);
                            if (whitelisted == null) {
                                sender.sendMessage(Color.Red + "That player is not online!");
                                return true;
                            }
                            if (Fiona.getAC().whitelisted.contains(whitelisted)) {
                                Fiona.getAC().whitelisted.remove(whitelisted);
                                sender.sendMessage(Fiona.getAC().getPrefix() + Color.Green + "Removed " + whitelisted.getName() + " from the ignore list.");
                            }
                            else {
                                sender.sendMessage(Color.Red + "That player isn't in the ignore list!");
                            }
                        }
                        else if (args[1].equalsIgnoreCase("isWhitelisted")) {
                            final Player player = Bukkit.getPlayer(args[2]);
                            if (player == null) {
                                sender.sendMessage("false");
                                return true;
                            }
                            sender.sendMessage(String.valueOf(Fiona.getAC().whitelisted.contains(player)));
                        }
                        else {
                            sender.sendMessage(Color.Red + "Invalid arguments! /fiona help");
                        }
                    }
                    else {
                        sender.sendMessage(Color.Red + "Invalid arguments! /fiona help");
                    }
                }
                else {
                    sender.sendMessage(Fiona.getAC().getMessage().NO_PERMISSION);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("debug")) {
                if (sender.hasPermission("fiona.admin") || sender.hasPermission("fiona.debug")) {
                    if (sender instanceof Player) {
                        final Player player = (Player)sender;
                        final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                        user.setDebugMode(!user.isDebugMode());
                        player.sendMessage(Color.Green + "Set debug mode to " + user.isDebugMode() + "!");
                    }
                    else {
                        sender.sendMessage(Color.Red + "You need to be a player to initiate debug mode!");
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("status")) {
                if (sender.hasPermission("fiona.admin")) {
                    final ArrayList<String> bannable = new ArrayList<String>();
                    for (final Checks check1 : Fiona.getAC().getChecks().getDetections()) {
                        if (check1.isBannable()) {
                            bannable.add(Color.Gray + (check1.getState() ? (Color.Green + check1.getName()) : (Color.Red + check1.getName())).toString() + Color.Gray);
                        }
                    }
                    final ArrayList<String> notbannable = new ArrayList<String>();
                    for (final Checks check2 : Fiona.getAC().getChecks().getDetections()) {
                        if (!check2.isBannable()) {
                            notbannable.add(Color.Gray + (check2.getState() ? (Color.Green + check2.getName()) : (Color.Red + check2.getName())).toString() + Color.Gray);
                        }
                    }
                    for (final String string : Fiona.getAC().getMessage().FIONA_STATUS) {
                        sender.sendMessage(Color.translate(string).replaceAll("%notbannable%", notbannable.toString()).replaceAll("%bannable%", bannable.toString()).replaceAll("%tps%", String.valueOf(MathUtils.trim(1, Fiona.getAC().getPing().getTPS()))));
                    }
                }
                else {
                    sender.sendMessage(Fiona.getAC().getMessage().NO_PERMISSION);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("alerts")) {
                if (sender instanceof Player) {
                    final Player player = (Player)sender;
                    final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                    user.setHasAlerts(!user.isHasAlerts());
                    final String state = user.isHasAlerts() ? (Color.Green + "true") : (Color.Dark_Red + "false");
                    player.sendMessage(Fiona.getAC().getMessage().ALERTS_TOGGLE.replaceAll("%state%", state));
                }
                else {
                    sender.sendMessage(Color.Red + "You must be a player to run this command. Do /fiona help to see a list of commands a non-player can do.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("sethunger")) {
                if (args.length == 3 && sender.hasPermission("fiona.admin")) {
                    final Player target = Bukkit.getPlayer(args[1]);
                    final int hunger = Integer.parseInt(args[2]);
                    if (target != null) {
                        target.setFoodLevel(hunger);
                        sender.sendMessage(Color.Green + "Successfully set " + target.getName() + "'s hunger to " + hunger + "!");
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("bannable")) {
                if (args.length > 1) {
                    if (sender.hasPermission("fiona.admin")) {
                        final String checkName = args[1];
                        final Checks check3 = Fiona.getAC().getChecks().getCheckByName(checkName);
                        if (check3 == null) {
                            sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + " Check ' " + checkName + " ' not found.");
                            final ArrayList<String> list = new ArrayList<String>();
                            for (final Checks check4 : Fiona.getAC().getChecks().getDetections()) {
                                list.add(Color.Gray + (check4.isBannable() ? (Color.Green + check4.getName()) : (Color.Red + check4.getName())).toString() + Color.Gray);
                            }
                            sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + " Bannable Status: " + Color.Gray + list.toString());
                            return true;
                        }
                        check3.toggleBans();
                        Fiona.getAC().getConfig().set("checks." + check3.getName() + ".bannable", (Object)check3.isBannable());
                        Fiona.getAC().saveConfig();
                        final String state = String.valueOf(check3.isBannable() ? (Color.Green + check3.isBannable()) : (Color.Dark_Red + check3.isBannable()));
                        sender.sendMessage(Fiona.getAC().getMessage().SET_BANNABLE.replaceAll("%check%", check3.getName()).replaceAll("%state%", state));
                    }
                    else {
                        sender.sendMessage(Fiona.getAC().getMessage().NO_PERMISSION);
                    }
                }
                else {
                    sender.sendMessage(Color.Red + "Please specify a check to toggle bans for.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("verbose")) {
                if (args.length > 1) {
                    final Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(Color.Red + "That player is not online!");
                        return true;
                    }
                    final User user = Fiona.getUserManager().getUser(target.getUniqueId());
                    if (user.getVLs().size() > 0) {
                        final List<String> list2 = new ArrayList<String>();
                        for (final String string2 : Fiona.getAC().getMessage().VERBOSE_FORMAT) {
                            list2.add(string2);
                        }
                        int i = list2.indexOf("%checks%");
                        list2.remove("%checks%");
                        for (final Checks check5 : user.getVLs().keySet()) {
                            list2.add(i, Color.White + "- " + check5.getName() + " VL: " + user.getVL(check5));
                            ++i;
                        }
                        for (final String string3 : list2) {
                            sender.sendMessage(Color.translate(string3.replaceAll("%player%", target.getName()).replaceAll("%ping%", String.valueOf(Fiona.getAC().getPing().getPing(target)))));
                        }
                    }
                    else {
                        final List<String> list2 = new ArrayList<String>();
                        for (final String string2 : Fiona.getAC().getMessage().VERBOSE_FORMAT) {
                            list2.add(string2);
                        }
                        final int i = list2.indexOf("%checks%");
                        list2.remove("%checks%");
                        list2.add(i, Fiona.getAC().getMessage().VERBOSE_NOCHECKS);
                        for (final String string3 : list2) {
                            sender.sendMessage(Color.translate(string3.replaceAll("%player%", target.getName()).replaceAll("%ping%", String.valueOf(Fiona.getAC().getPing().getPing(target)))));
                        }
                    }
                }
                else {
                    sender.sendMessage(Color.Red + "Please specify a player to check violations for.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("toggle")) {
                if (args.length > 1) {
                    final String checkName = args[1];
                    if (sender.hasPermission("fiona.admin")) {
                        final Checks check3 = Fiona.getAC().getChecks().getCheckByName(checkName);
                        if (check3 == null) {
                            sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + " Check ' " + checkName + " ' not found.");
                            final ArrayList<String> list = new ArrayList<String>();
                            for (final Checks check4 : Fiona.getAC().getChecks().getDetections()) {
                                list.add(Color.Gray + (check4.getState() ? (Color.Green + check4.getName()) : (Color.Red + check4.getName())).toString() + Color.Gray);
                            }
                            sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + " Available checks: " + Color.Gray + list.toString());
                            return true;
                        }
                        check3.toggle();
                        Fiona.getAC().getConfig().set("checks." + check3.getName() + ".enabled", (Object)check3.getState());
                        Fiona.getAC().saveConfig();
                        final String state = String.valueOf(check3.getState() ? (Color.Green + check3.getState()) : (Color.Dark_Red + check3.getState()));
                        sender.sendMessage(Fiona.getAC().getMessage().SET_TOGGLE.replaceAll("%check%", check3.getName()).replaceAll("%state%", state));
                    }
                    else {
                        sender.sendMessage(Fiona.getAC().getMessage().NO_PERMISSION);
                    }
                }
                else {
                    sender.sendMessage(Color.Red + "Please specify a check to toggle on/off.");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona toggle <CheckName> to enable/disable checks.");
                if (sender instanceof Player) {
                    sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona Alerts on/off to enable/disable alerts.");
                }
                sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona bannable <checkName> to make a check bannable/silent.");
                sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona status to check the current Fiona status.");
                sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona verbose <player> to see a player's violations.");
                sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona reload to reload the plugin.");
                sender.sendMessage(Fiona.getAC().getPrefix() + ChatColor.RED + "Use /Fiona whitelist [add/remove] <player> to add a player to the ignore list.");
                return true;
            }
            sender.sendMessage(Color.Red + "Invalid argument(s). Do /fiona help to see the list of commands you can do.");
        }
        return true;
    }
}
