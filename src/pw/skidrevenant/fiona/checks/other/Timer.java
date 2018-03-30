package pw.skidrevenant.fiona.checks.other;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.detections.Checks;
import pw.skidrevenant.fiona.detections.ChecksListener;
import pw.skidrevenant.fiona.detections.ChecksType;
import pw.skidrevenant.fiona.packets.events.PacketEvent;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.MathUtils;
import pw.skidrevenant.fiona.utils.TimerUtils;

@ChecksListener(events = { PacketEvent.class, PlayerQuitEvent.class })
public class Timer extends Checks
{
    private Map<UUID, Map.Entry<Integer, Long>> packets;
    private Map<UUID, Map.Entry<Integer, Long>> packetsB;
    private Map<UUID, Map.Entry<Integer, Long>> packetsC;
    private Map<UUID, Integer> verbose;
    private Map<UUID, Integer> verboseB;
    private Map<UUID, Integer> verboseC;
    private List<Player> toCancel;
    private List<Player> toCancelB;
    private List<Player> toCancelC;
    private boolean typeA;
    private boolean typeB;
    private boolean typeC;
    
    public Timer() {
        super("Timer", ChecksType.OTHER, Fiona.getAC(), 10, true, false);
        this.typeA = true;
        this.typeB = true;
        this.typeC = true;
        this.packets = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.verbose = new HashMap<UUID, Integer>();
        this.toCancel = new ArrayList<Player>();
        this.packetsB = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.verboseB = new HashMap<UUID, Integer>();
        this.toCancelB = new ArrayList<Player>();
        this.packetsC = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.verboseC = new HashMap<UUID, Integer>();
        this.toCancelC = new ArrayList<Player>();
        this.typeA = Fiona.getAC().getConfig().getBoolean("checks.Timer.TypeA");
        this.typeB = Fiona.getAC().getConfig().getBoolean("checks.Timer.TypeB");
        this.typeC = Fiona.getAC().getConfig().getBoolean("checks.Timer.TypeC");
    }
    
    @Override
    protected void onEvent(final Event event) {
        if (!this.getState()) {
            return;
        }
        if (event instanceof PacketEvent) {
            final PacketEvent e = (PacketEvent)event;
            final Player player = e.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (System.currentTimeMillis() - user.getLastPacket() > 100L) {
                this.toCancel.add(player);
            }
            if (MathUtils.elapsed(user.getLoginMIllis()) < 3000L || Fiona.getAC().getPing().getTPS() < this.getTps() || MathUtils.elapsed(user.isTeleported()) < 1000L || user.isCancelled() == CancelType.MOVEMENT) {
                return;
            }
            if (this.typeA) {
                int packets = 0;
                long Time = System.currentTimeMillis();
                int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
                if (this.packets.containsKey(player.getUniqueId())) {
                    packets = this.packets.get(player.getUniqueId()).getKey();
                    Time = this.packets.get(player.getUniqueId()).getValue();
                }
                if (System.currentTimeMillis() - user.getLastPacket() > 100L) {
                    this.toCancel.add(player);
                }
                final double threshold = 8.0;
                if (TimerUtils.elapsed(Time, 250L)) {
                    if (packets > threshold + user.getPosPackets() && user.getPosPackets() < 1) {
                        verbose = ((packets - threshold > 9.0) ? (verbose + 2) : (verbose + 1));
                    }
                    else {
                        verbose = 0;
                    }
                    if (verbose > 3) {
                        user.setVL(this, user.getVL(this) + 1);
                        user.setCancelled(this, CancelType.MOVEMENT);
                        if (user.getVL(this) > 1) {
                            this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Type A " + Color.Gray + "Timer Speed: " + Color.Green + MathUtils.round(packets / threshold, 2) + Color.Gray + " Lag: " + Color.Green + user.getPacketsFromLag());
                        }
                    }
                    packets = 0;
                    Time = TimerUtils.nowlong();
                }
                if (!this.toCancel.remove(player) || user.getPacketLoss() > 7) {
                    ++packets;
                    user.setPacketLoss(user.getPacketLoss() + 1);
                }
                this.packets.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(packets, Time));
                this.verbose.put(player.getUniqueId(), verbose);
            }
        }
        if (event instanceof PacketEvent) {
            final PacketEvent e = (PacketEvent)event;
            final Player player = e.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (MathUtils.elapsed(user.getLoginMIllis()) < 3000L || Fiona.getAC().getPing().getTPS() < this.getTps() || MathUtils.elapsed(user.isTeleported()) < 1000L || user.isCancelled() == CancelType.MOVEMENT) {
                return;
            }
            if (this.typeB) {
                int packets = 0;
                long Time = System.currentTimeMillis();
                int verbose = this.verboseB.getOrDefault(player.getUniqueId(), 0);
                if (this.packetsB.containsKey(player.getUniqueId())) {
                    packets = this.packetsB.get(player.getUniqueId()).getKey();
                    Time = this.packetsB.get(player.getUniqueId()).getValue();
                }
                if (System.currentTimeMillis() - user.getLastPacket() > 100L) {
                    this.toCancelB.add(player);
                }
                final double threshold = 18.0;
                if (TimerUtils.elapsed(Time, 750L)) {
                    if (packets > threshold + user.getPosPackets() && user.getPosPackets() < 1) {
                        verbose = ((packets - threshold > 13.0) ? (verbose + 2) : (verbose + 1));
                    }
                    else {
                        verbose = 0;
                    }
                    if (verbose > 2) {
                        user.setVL(this, user.getVL(this) + 1);
                        user.setCancelled(this, CancelType.MOVEMENT, 2);
                        if (user.getVL(this) > 1) {
                            this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Type B " + Color.Gray + "Timer Speed: " + Color.Green + MathUtils.round(packets / threshold, 2) + Color.Gray + " Lag: " + Color.Green + user.getPacketsFromLag());
                        }
                    }
                    packets = 0;
                    Time = TimerUtils.nowlong();
                }
                if (!this.toCancelB.remove(player) || user.getPacketLoss() > 12) {
                    ++packets;
                    user.setPacketLoss(user.getPacketLoss() + 1);
                }
                this.packetsB.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(packets, Time));
                this.verboseB.put(player.getUniqueId(), verbose);
            }
        }
        if (event instanceof PacketEvent) {
            final PacketEvent e = (PacketEvent)event;
            final Player player = e.getPlayer();
            final User user = Fiona.getUserManager().getUser(player.getUniqueId());
            if (MathUtils.elapsed(user.getLoginMIllis()) < 3000L || Fiona.getAC().getPing().getTPS() < this.getTps() || MathUtils.elapsed(user.isTeleported()) < 1000L || user.isCancelled() == CancelType.MOVEMENT) {
                return;
            }
            if (this.typeC) {
                int packets = 0;
                long Time = System.currentTimeMillis();
                int verbose = this.verboseC.getOrDefault(player.getUniqueId(), 0);
                if (this.packetsC.containsKey(player.getUniqueId())) {
                    packets = this.packetsC.get(player.getUniqueId()).getKey();
                    Time = this.packetsC.get(player.getUniqueId()).getValue();
                }
                if (System.currentTimeMillis() - user.getLastPacket() > 100L) {
                    this.toCancelC.add(player);
                }
                final double threshold = 42.0;
                if (TimerUtils.elapsed(Time, 2000L)) {
                    if (this.toCancelC.contains(player) && user.getPacketsFromLag() <= 150.0) {
                        packets = 0;
                        Time = System.currentTimeMillis();
                        this.toCancelC.remove(player);
                    }
                    else {
                        if (packets > threshold + user.getPosPackets() && user.getPosPackets() < 1) {
                            verbose = ((packets - threshold > 50.0) ? (verbose + 2) : (verbose + 1));
                        }
                        else {
                            verbose = 0;
                        }
                        if (verbose > 2) {
                            user.setVL(this, user.getVL(this) + 1);
                            user.setCancelled(this, CancelType.MOVEMENT, 2);
                            if (user.getVL(this) > 1) {
                                this.Alert(player, Color.Gray + "Reason: " + Color.Green + "Type C " + Color.Gray + "Timer Speed: " + Color.Green + MathUtils.round(packets / threshold, 2) + Color.Gray + " Lag: " + Color.Green + user.getPacketsFromLag());
                            }
                        }
                        packets = 0;
                        Time = TimerUtils.nowlong();
                    }
                }
                if (!this.toCancelC.remove(player) || user.getPacketLoss() > 16) {
                    ++packets;
                    user.setPacketLoss(user.getPacketLoss() + 1);
                }
                this.packetsC.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(packets, Time));
                this.verboseC.put(player.getUniqueId(), verbose);
            }
        }
        if (event instanceof PlayerQuitEvent) {
            final PlayerQuitEvent e2 = (PlayerQuitEvent)event;
            if (this.packets.containsKey(e2.getPlayer().getUniqueId())) {
                this.packets.remove(e2.getPlayer().getUniqueId());
            }
            if (this.verbose.containsKey(e2.getPlayer().getUniqueId())) {
                this.verbose.remove(e2.getPlayer().getUniqueId());
            }
            if (this.toCancel.contains(e2.getPlayer())) {
                this.toCancel.remove(e2.getPlayer());
            }
            if (this.packetsB.containsKey(e2.getPlayer().getUniqueId())) {
                this.packetsB.remove(e2.getPlayer().getUniqueId());
            }
            if (this.verboseB.containsKey(e2.getPlayer().getUniqueId())) {
                this.verboseB.remove(e2.getPlayer().getUniqueId());
            }
            if (this.toCancel.contains(e2.getPlayer())) {
                this.toCancel.remove(e2.getPlayer());
            }
            if (this.toCancelB.contains(e2.getPlayer())) {
                this.toCancelB.remove(e2.getPlayer());
            }
        }
    }
}
