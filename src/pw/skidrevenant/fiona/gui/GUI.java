package pw.skidrevenant.fiona.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.utils.Color;

public class GUI
{
    public Inventory mainGUI;
    public Inventory checksBannableGUI;
    public Inventory checksToggleGUI;
    public Inventory checksSetToggleGUI;
    public Inventory checksSetBannableGUI;
    public ItemStack backButton;
    String color;
    
    public GUI() {
        this.mainGUI = Bukkit.createInventory((InventoryHolder)null, 27, Color.Gold + Color.Bold + "Fiona AntiCheat");
        this.checksBannableGUI = Bukkit.createInventory((InventoryHolder)null, 9, Color.Dark_Gray + "Choose a type to set bannable.");
        this.checksToggleGUI = Bukkit.createInventory((InventoryHolder)null, 9, Color.Dark_Gray + "Choose a type to toggle.");
        this.backButton = this.createItem(Material.ARROW, 1, "&cBack", "", "&fLeft Click &7to go back to previous page.");
        this.loadMainGUIItems();
    }
    
    private void loadMainGUIItems() {
        this.mainGUI.setItem(9, this.createItem(Material.BOOK, 1, "&6Toggle Bannable Checks", "", "&fLeft Click &7to open &fGUI Editor", "&7for &fToggling Bans&7."));
        this.mainGUI.setItem(11, this.createItem(Material.BOOK, 1, "&6Toggle Checks", "", "&fLeft Click &7to open &fGUI Editor", "&7for &fEnabling/Disabling Checks&7."));
        this.mainGUI.setItem(13, this.createItem(Material.ENCHANTED_BOOK, 1, "&cFiona Info", "", "", "&7You are currently using &6Fiona v" + Fiona.getAC().getDescription().getVersion(), "&7by &ffunkemunky &7and &fXTasyCode&7.", "&7Any questions or concerns?", "&fShift Left Click &7to receive an &fInvite to the Fiona Discord&7."));
        this.mainGUI.setItem(15, this.createItem(Material.BOOK, 1, "&6Reload Fiona", "", "&fLeft Click &7to &fReload Fiona&7."));
        this.mainGUI.setItem(17, this.createItem(Material.BOOK, 1, "&6Reset Violations", "", "&fLeft Click &7to", "&fReset all player Violations&7."));
    }
    
    public void openMainGUI(final Player player) {
        player.openInventory(this.mainGUI);
    }
    
    public void openChecksBannableGUI(final Player player) {
        this.checksBannableGUI.setItem(0, Fiona.getAC().getConfig().getBoolean("gold") ? this.createItem(Material.GOLD_BLOCK, 1, "&6Gold", new String[0]) : this.backButton);
        this.checksBannableGUI.setItem(2, this.createItem(Material.REDSTONE_BLOCK, 1, "&cCombat", new String[0]));
        this.checksBannableGUI.setItem(4, this.createItem(Material.REDSTONE_BLOCK, 1, "&cMovement", new String[0]));
        this.checksBannableGUI.setItem(6, this.createItem(Material.REDSTONE_BLOCK, 1, "&cMiscellaneous", new String[0]));
        this.checksBannableGUI.setItem(8, this.backButton);
        player.openInventory(this.checksBannableGUI);
    }
    
    public void openBannableChecks(final Player player, final ChecksType type) {
        this.checksSetBannableGUI = Bukkit.createInventory((InventoryHolder)null, 45, Color.Dark_Gray + "Toggle Bans for: " + Color.Gold + type.getName());
        int i = 0;
        for (final Checks check : Fiona.getAC().getChecks().getDetections()) {
            if (check.getType() == type && i != 44) {
                this.checksSetBannableGUI.setItem(i, this.createItem(check.isBannable() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, i + 1, check.isBannable() ? (Color.Green + check.getName()) : (Color.Red + check.getName()), new String[0]));
                ++i;
            }
        }
        if (type.equals(ChecksType.GOLD) && this.checksSetBannableGUI.getSize() == 1) {
            this.checksSetBannableGUI.setItem(22, this.createItem(Material.PAPER, 1, Color.Red + Color.Bold + "ERROR", "", Color.Gray + "You haven't purchased Fiona" + Color.Gold + "Gold", "", Color.White + "Purchased Gold?", Color.Gray + "Open a ticket in the Fiona Discord."));
        }
        this.checksSetBannableGUI.setItem(44, this.backButton);
        player.openInventory(this.checksSetBannableGUI);
    }
    
    public void openChecksToggleGUI(final Player player) {
        this.checksToggleGUI.setItem(0, Fiona.getAC().getConfig().getBoolean("gold") ? this.createItem(Material.GOLD_BLOCK, 1, "&6Gold", new String[0]) : this.backButton);
        this.checksToggleGUI.setItem(2, this.createItem(Material.REDSTONE_BLOCK, 1, "&cCombat", new String[0]));
        this.checksToggleGUI.setItem(4, this.createItem(Material.REDSTONE_BLOCK, 1, "&cMovement", new String[0]));
        this.checksToggleGUI.setItem(6, this.createItem(Material.REDSTONE_BLOCK, 1, "&cMiscellaneous", new String[0]));
        this.checksToggleGUI.setItem(8, this.backButton);
        player.openInventory(this.checksToggleGUI);
    }
    
    public void openToggleChecks(final Player player, final ChecksType type) {
        this.checksSetToggleGUI = Bukkit.createInventory((InventoryHolder)null, 45, Color.Dark_Gray + "Toggle Checks for: " + Color.Gold + type.getName());
        int i = 0;
        for (final Checks check : Fiona.getAC().getChecks().getDetections()) {
            if (check.getType() == type && i != 44) {
                this.checksSetToggleGUI.setItem(i, this.createItem(check.getState() ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK, i + 1, check.getState() ? (Color.Green + check.getName()) : (Color.Red + check.getName()), new String[0]));
                ++i;
            }
        }
        this.checksSetToggleGUI.setItem(44, this.backButton);
        player.openInventory(this.checksSetToggleGUI);
    }
    
    public ItemStack createItem(final Material material, final int amount, final String name, final String... lore) {
        final ItemStack thing = new ItemStack(material, amount);
        final ItemMeta thingm = thing.getItemMeta();
        thingm.setDisplayName(Color.translate(name));
        final List<String> loreList = new ArrayList<String>();
        for (final String string : lore) {
            loreList.add(Color.translate(string));
        }
        thingm.setLore((List)loreList);
        thing.setItemMeta(thingm);
        return thing;
    }
    
    public boolean hasSameName(final ItemStack item, final String name) {
        return item.getItemMeta().getDisplayName().equals(name);
    }
}
