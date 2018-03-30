package pw.skidrevenant.fiona.utils;

import org.bukkit.Bukkit;

public class ServerUtils
{
    public static boolean isBukkitVerison(final String version) {
        final String bukkit = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        return bukkit.contains(version);
    }
}
