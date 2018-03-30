package pw.skidrevenant.fiona.utils;

import org.bukkit.ChatColor;

public class Color
{
    public static String Dark_Red;
    public static String Red;
    public static String Yellow;
    public static String Gold;
    public static String Green;
    public static String Dark_Green;
    public static String Aqua;
    public static String Blue;
    public static String Dark_Blue;
    public static String Pink;
    public static String Purple;
    public static String Gray;
    public static String Dark_Gray;
    public static String Black;
    public static String Bold;
    public static String Italics;
    public static String Underline;
    public static String Strikethrough;
    public static String White;
    
    public static String translate(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    
    public static String strip(final String string) {
        return ChatColor.stripColor(string);
    }
    
    static {
        Color.Dark_Red = ChatColor.DARK_RED.toString();
        Color.Red = ChatColor.RED.toString();
        Color.Yellow = ChatColor.YELLOW.toString();
        Color.Gold = ChatColor.GOLD.toString();
        Color.Green = ChatColor.GREEN.toString();
        Color.Dark_Green = ChatColor.DARK_GREEN.toString();
        Color.Aqua = ChatColor.AQUA.toString();
        Color.Blue = ChatColor.BLUE.toString();
        Color.Dark_Blue = ChatColor.DARK_BLUE.toString();
        Color.Pink = ChatColor.LIGHT_PURPLE.toString();
        Color.Purple = ChatColor.DARK_PURPLE.toString();
        Color.Gray = ChatColor.GRAY.toString();
        Color.Dark_Gray = ChatColor.DARK_GRAY.toString();
        Color.Black = ChatColor.BLACK.toString();
        Color.Bold = ChatColor.BOLD.toString();
        Color.Italics = ChatColor.ITALIC.toString();
        Color.Underline = ChatColor.UNDERLINE.toString();
        Color.Strikethrough = ChatColor.STRIKETHROUGH.toString();
        Color.White = ChatColor.WHITE.toString();
    }
}
