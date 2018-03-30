package pw.skidrevenant.fiona.detections;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;

import pw.skidrevenant.fiona.checks.combat.AimPattern;
import pw.skidrevenant.fiona.checks.combat.AutoClicker;
import pw.skidrevenant.fiona.checks.combat.Criticals;
import pw.skidrevenant.fiona.checks.combat.Fastbow;
import pw.skidrevenant.fiona.checks.combat.KillAuraA;
import pw.skidrevenant.fiona.checks.combat.Reach;
import pw.skidrevenant.fiona.checks.combat.Regen;
import pw.skidrevenant.fiona.checks.movement.Fly;
import pw.skidrevenant.fiona.checks.movement.Gravity;
import pw.skidrevenant.fiona.checks.movement.GroundSpoof;
import pw.skidrevenant.fiona.checks.movement.Jesus;
import pw.skidrevenant.fiona.checks.movement.NoSlowDown;
import pw.skidrevenant.fiona.checks.movement.Phase;
import pw.skidrevenant.fiona.checks.movement.Speed;
import pw.skidrevenant.fiona.checks.movement.Vclip;
import pw.skidrevenant.fiona.checks.movement.Velocity;
import pw.skidrevenant.fiona.checks.other.Exploit;
import pw.skidrevenant.fiona.checks.other.ImpossiblePitch;
import pw.skidrevenant.fiona.checks.other.InvalidPacket;
import pw.skidrevenant.fiona.checks.other.Scaffold;
import pw.skidrevenant.fiona.checks.other.Timer;

public class ChecksManager
{
    public static List<Checks> detections;
    
    public List<Checks> getDetections() {
        return ChecksManager.detections;
    }
    
    public Checks getCheckByName(final String name) {
        for (final Checks check : this.getDetections()) {
            if (check.getName().equalsIgnoreCase(name)) {
                return check;
            }
        }
        return null;
    }
    
    public void init() {
        new Reach();
        new Speed();
        new Gravity();
        new KillAuraA();
        new Vclip();
        new Jesus();
        new Criticals();
        new Fly();
        new NoSlowDown();
        new Exploit();
        new GroundSpoof();
        new Fastbow();
        new Regen();
        new Phase();
        new AimPattern();
        new ImpossiblePitch();
        new Timer();
        new Scaffold();
        new Velocity();
        new AutoClicker();
        new InvalidPacket();
    }
    
    public void event(final Event event) {
        for (int i = 0; i < ChecksManager.detections.size(); ++i) {
            final Checks detection = ChecksManager.detections.get(i);
            final Class<? extends Checks> clazz = detection.getClass();
            if (clazz.isAnnotationPresent(ChecksListener.class)) {
                final Annotation annotation = clazz.getAnnotation(ChecksListener.class);
                final ChecksListener handler = (ChecksListener)annotation;
                for (final Class<?> type : handler.events()) {
                    if (type == event.getClass()) {
                        detection.onEvent(event);
                    }
                }
            }
        }
    }
    
    static {
        ChecksManager.detections = new ArrayList<Checks>();
    }
}
