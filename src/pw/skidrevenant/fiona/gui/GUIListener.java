package pw.skidrevenant.fiona.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.ProtocolLibrary;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.packets.PacketListeners;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.Messages;

public class GUIListener implements Listener
{
    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        final GUI gui = Fiona.getAC().getGUIManager();
        final Player player = (Player)event.getWhoClicked();
        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }
        final ItemStack clickedItem = event.getCurrentItem();
        if (event.getInventory().getName().equals(Color.Gold + Color.Bold + "Fiona AntiCheat")) {
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(Color.Gold + "Reload Fiona")) {
                final ItemStack item = event.getCurrentItem();
                final ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Color.Red + Color.Italics + "Working...");
                item.setItemMeta(meta);
                Fiona.getAC().reloadConfig();
                Fiona.getAC().reloadMessages();
                new Messages(Fiona.getAC());
                HandlerList.unregisterAll((Plugin)Fiona.getAC());
                Fiona.getAC().getServer().getScheduler().cancelAllTasks();
                Fiona.getAC().registerEvents();
                ProtocolLibrary.getProtocolManager().getPacketListeners().forEach(ProtocolLibrary.getProtocolManager()::removePacketListener);
                new PacketListeners();
                Fiona.getAC().getChecks().getDetections().clear();
                Fiona.getAC().getChecks().init();
                Fiona.getAC().loadChecks();
                Fiona.getAC().clearVLS();
                meta.setDisplayName(Color.Green + "Reloaded!");
                item.setItemMeta(meta);
                new BukkitRunnable() {
                    public void run() {
                        meta.setDisplayName(Color.Gold + "Reload Fiona");
                        item.setItemMeta(meta);
                    }
                }.runTaskLater((Plugin)Fiona.getAC(), 30L);
            }
            if (gui.hasSameName(clickedItem, Color.Gold + "Reset Violations")) {
                final ItemStack item = event.getCurrentItem();
                final ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Color.Red + Color.Italics + "Working...");
                item.setItemMeta(meta);
                Fiona.getAC().clearVLS();
                meta.setDisplayName(Color.Green + "Successfully Reset Violations!");
                item.setItemMeta(meta);
                new BukkitRunnable() {
                    public void run() {
                        meta.setDisplayName(Color.Gold + "Reset Violations");
                        item.setItemMeta(meta);
                    }
                }.runTaskLater((Plugin)Fiona.getAC(), 30L);
            }
            if (gui.hasSameName(clickedItem, Color.Gold + "Toggle Checks")) {
                gui.openChecksToggleGUI(player);
            }
            if (gui.hasSameName(clickedItem, Color.Gold + "Toggle Bannable Checks")) {
                gui.openChecksBannableGUI(player);
            }
            if (gui.hasSameName(clickedItem, Color.Red + "Fiona Info") && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                player.closeInventory();
                player.sendMessage("");
                player.sendMessage(Color.Gray + "Discord: " + Color.Green + "https://discord.gg/wpHSp5t");
                player.sendMessage("");
            }
            event.setCancelled(true);
        }
        if (event.getInventory().equals(gui.checksBannableGUI)) {
            if (gui.hasSameName(clickedItem, Color.Red + "Combat")) {
                gui.openBannableChecks(player, ChecksType.COMBAT);
            }
            if (gui.hasSameName(clickedItem, Color.Red + "Movement")) {
                gui.openBannableChecks(player, ChecksType.MOVEMENT);
            }
            if (gui.hasSameName(clickedItem, Color.Red + "Miscellaneous")) {
                gui.openBannableChecks(player, ChecksType.OTHER);
            }
            if (gui.hasSameName(clickedItem, Color.Red + "Back")) {
                gui.openMainGUI(player);
            }
            event.setCancelled(true);
        }
        if (event.getInventory().equals(gui.checksToggleGUI)) {
            if (gui.hasSameName(clickedItem, Color.Red + "Combat")) {
                gui.openToggleChecks(player, ChecksType.COMBAT);
            }
            if (gui.hasSameName(clickedItem, Color.Red + "Movement")) {
                gui.openToggleChecks(player, ChecksType.MOVEMENT);
            }
            if (gui.hasSameName(clickedItem, Color.Red + "Miscellaneous")) {
                gui.openToggleChecks(player, ChecksType.OTHER);
            }
            if (gui.hasSameName(clickedItem, Color.Red + "Back")) {
                gui.openMainGUI(player);
            }
            event.setCancelled(true);
        }
        if (event.getInventory().getName().contains(Color.Dark_Gray + "Toggle Checks for:")) {
            if (gui.hasSameName(clickedItem, Color.Red + "Back")) {
                gui.openChecksToggleGUI(player);
            }
            else {
                final Checks check = Fiona.getAC().getChecks().getCheckByName(Color.strip(clickedItem.getItemMeta().getDisplayName()));
                if (check != null) {
                    check.toggle();
                    Fiona.getAC().getConfig().set("checks." + check.getName() + ".enabled", (Object)check.getState());
                    Fiona.getAC().saveConfig();
                    event.setCurrentItem(gui.createItem(check.getState() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, event.getCurrentItem().getAmount(), check.getState() ? (Color.Green + check.getName()) : (Color.Red + check.getName()), new String[0]));
                }
            }
            event.setCancelled(true);
        }
        if (event.getInventory().getName().contains(Color.Dark_Gray + "Toggle Bans for:")) {
            if (gui.hasSameName(clickedItem, Color.Red + "Back")) {
                gui.openChecksBannableGUI(player);
            }
            else {
                final Checks check = Fiona.getAC().getChecks().getCheckByName(Color.strip(clickedItem.getItemMeta().getDisplayName()));
                if (check != null) {
                    check.toggleBans();
                    Fiona.getAC().getConfig().set("checks." + check.getName() + ".bannable", (Object)check.isBannable());
                    Fiona.getAC().saveConfig();
                    event.setCurrentItem(gui.createItem(check.isBannable() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, event.getCurrentItem().getAmount(), check.isBannable() ? (Color.Green + check.getName()) : (Color.Red + check.getName()), new String[0]));
                }
            }
            event.setCancelled(true);
        }
    }
}
