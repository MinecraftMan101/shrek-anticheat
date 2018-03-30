package pw.skidrevenant.fiona.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.Color;

public class AlertsCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("fiona.staff") || !(sender instanceof Player)) {
            sender.sendMessage(Fiona.getAC().getMessage().NO_PERMISSION);
            return true;
        }
        final Player player = (Player)sender;
        final User user = Fiona.getUserManager().getUser(player.getUniqueId());
        user.setHasAlerts(!user.isHasAlerts());
        final String state = user.isHasAlerts() ? (Color.Green + "true") : (Color.Dark_Red + "false");
        player.sendMessage(Fiona.getAC().getMessage().ALERTS_TOGGLE.replaceAll("%state%", state));
        return true;
    }
}
