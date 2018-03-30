package pw.skidrevenant.fiona.utils;

import java.util.Arrays;
import java.util.List;

import pw.skidrevenant.fiona.Fiona;

public class Messages
{
    public String LOG_SAVING;
    public String LOG_SAVED;
    public String RELOADING_CONFIG;
    public String RELOADING_CHECKS;
    public String RELOADING_VIOLATIONS;
    public String RELOADING_PLUGIN;
    public String RELOADING_PARTLY_DONE;
    public String RELOADING_DONE;
    public String NO_PERMISSION;
    public String INVALID_ARGUMENT;
    public List<String> FIONA_STATUS;
    public String SET_BANNABLE;
    public String SET_TOGGLE;
    public String ALERTS_JOIN;
    public String VERBOSE_NOCHECKS;
    public List<String> VERBOSE_FORMAT;
    public String ALERTS_TOGGLE;
    public String PEARLFIX_DETECTED;
    public String PEARLFIX_SAFELOC;
    
    public Messages(final Fiona fiona) {
        this.LOG_SAVING = this.getPrefix("%prefix%&cSaving logs for all online users...");
        this.LOG_SAVED = this.getPrefix("%prefix%&aSaved!");
        this.RELOADING_CONFIG = this.getPrefix("%prefix%&cReloading config...");
        this.RELOADING_CHECKS = this.getPrefix("%prefix%&cReloading checks...");
        this.RELOADING_VIOLATIONS = this.getPrefix("%prefix%&cResetting violations...");
        this.RELOADING_PLUGIN = this.getPrefix("%prefix%&cReloading plugin...");
        this.RELOADING_PARTLY_DONE = this.getPrefix("%prefix%&eDone!");
        this.RELOADING_DONE = this.getPrefix("%prefix%&aSuccessfully reloaded Fiona!");
        this.NO_PERMISSION = this.getPrefix("&cNo permission.");
        this.INVALID_ARGUMENT = this.getPrefix("%prefix%&cInvalid argument(s).");
        this.FIONA_STATUS = Arrays.asList("", "&8&m----------------------------------", "&6&lFiona Status:", "", "&eTPS: &c%tps%", "", "&eSilent Checks: &7%notbannable%", "&eBannable Checks: &7%bannable%", "&8&m----------------------------------", "");
        this.SET_BANNABLE = this.getPrefix("%prefix%&c%check% bannable state has been set to %state%");
        this.SET_TOGGLE = this.getPrefix("%prefix%&c%check% state has been set to %state%");
        this.ALERTS_JOIN = this.getPrefix("%prefix%&7&oToggled your alerts on automatically. Do &a/fiona alerts &7&oto toggle them.");
        this.VERBOSE_NOCHECKS = this.getPrefix("&cThis player set off no checks!");
        this.VERBOSE_FORMAT = Arrays.asList("&8&m--------------------------------------------", "&6&l%player%'s Violations/Info", "", "&7Ping: &f%ping%", "", "&c&lSet Off:", "", "%checks%", "&8&m--------------------------------------------");
        this.ALERTS_TOGGLE = this.getPrefix("%prefix%&cAlerts state set to %state%&c.");
        this.PEARLFIX_DETECTED = this.getPrefix("%prefix%&7You have been detected trying to pearl glitch, therefore your pearl was cancelled.");
        this.PEARLFIX_SAFELOC = this.getPrefix("%prefix%&7Could not find a safe location, therefore your pearl was cancelled.");
        this.LOG_SAVING = this.getPrefix(fiona.getMessages().getString("LOG_SAVING"));
        this.LOG_SAVED = this.getPrefix(fiona.getMessages().getString("LOG_SAVED"));
        this.RELOADING_CONFIG = this.getPrefix(fiona.getMessages().getString("RELOADING_CONFIG"));
        this.RELOADING_CHECKS = this.getPrefix(fiona.getMessages().getString("RELOADING_CHECKS"));
        this.RELOADING_VIOLATIONS = this.getPrefix(fiona.getMessages().getString("RELOADING_VIOLATIONS"));
        this.RELOADING_PLUGIN = this.getPrefix(fiona.getMessages().getString("RELOADING_PLUGIN"));
        this.RELOADING_PARTLY_DONE = this.getPrefix(fiona.getMessages().getString("RELOADING_PARTLY_DONE"));
        this.RELOADING_DONE = this.getPrefix(fiona.getMessages().getString("RELOADING_DONE"));
        this.NO_PERMISSION = this.getPrefix(fiona.getMessages().getString("NO_PERMISSION"));
        this.INVALID_ARGUMENT = this.getPrefix(fiona.getMessages().getString("INVALID_ARGUMENT"));
        this.FIONA_STATUS = (List<String>)fiona.getMessages().getStringList("FIONA_STATUS");
        this.SET_BANNABLE = this.getPrefix(fiona.getMessages().getString("SET_BANNABLE"));
        this.SET_TOGGLE = this.getPrefix(fiona.getMessages().getString("SET_TOGGLE"));
        this.ALERTS_JOIN = this.getPrefix(fiona.getMessages().getString("ALERTS_JOIN"));
        this.ALERTS_TOGGLE = this.getPrefix(fiona.getMessages().getString("ALERTS_TOGGLE"));
        this.PEARLFIX_DETECTED = this.getPrefix(fiona.getMessages().getString("PEARLFIX_DETECTED"));
        this.PEARLFIX_SAFELOC = this.getPrefix(fiona.getMessages().getString("PEARLFIX_SAFELOC"));
        this.VERBOSE_FORMAT = (List<String>)fiona.getMessages().getStringList("VERBOSE_FORMAT");
        this.VERBOSE_NOCHECKS = this.getPrefix(fiona.getMessages().getString("VERBOSE_NOCHECKS"));
    }
    
    private String getPrefix(final String string) {
        return Color.translate(string.replaceAll("%prefix%", Fiona.getAC().getPrefix()));
    }
}
