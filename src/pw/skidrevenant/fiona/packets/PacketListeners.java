package pw.skidrevenant.fiona.packets;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import pw.skidrevenant.fiona.Fiona;
import pw.skidrevenant.fiona.packets.events.PacketKeepAliveEvent;
import pw.skidrevenant.fiona.packets.events.PacketKillauraEvent;
import pw.skidrevenant.fiona.packets.events.PacketTypes;
import pw.skidrevenant.fiona.user.User;
import pw.skidrevenant.fiona.utils.CancelType;
import pw.skidrevenant.fiona.utils.Color;
import pw.skidrevenant.fiona.utils.JsonMessage;
import pw.skidrevenant.fiona.utils.PlayerUtils;
import pw.skidrevenant.fiona.packets.events.PacketEvent;

public class PacketListeners
{
    private HashSet<EntityType> enabled;
    
    public PacketListeners() {
        (this.enabled = new HashSet<EntityType>()).add(EntityType.valueOf("PLAYER"));
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Client.USE_ENTITY }) {
            public void onPacketReceiving(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                EnumWrappers.EntityUseAction type;
                try {
                    type = (EnumWrappers.EntityUseAction)packet.getEntityUseActions().read(0);
                }
                catch (Exception ex) {
                    return;
                }
                final Entity entity = (Entity)event.getPacket().getEntityModifier(player.getWorld()).read(0);
                if (entity == null) {
                    return;
                }
                if (type == EnumWrappers.EntityUseAction.ATTACK) {
                    Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKillauraEvent(player, entity, PacketTypes.USE));
                }
            }
        });
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        if (version.contains("1_7") || version.contains("1_8")) {
            ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.ENTITY_METADATA }) {
                public void onPacketSending(final PacketEvent event) {
                    PacketContainer packet = event.getPacket();
                    final Entity e = (Entity)packet.getEntityModifier(event).read(0);
                    if (e instanceof LivingEntity && PacketListeners.this.enabled.contains(e.getType()) && packet.getWatchableCollectionModifier().read(0) != null && e.getUniqueId() != event.getPlayer().getUniqueId()) {
                        packet = packet.deepClone();
                        event.setPacket(packet);
                        if (event.getPacket().getType() == PacketType.Play.Server.ENTITY_METADATA) {
                            final WrappedDataWatcher watcher = new WrappedDataWatcher((List)packet.getWatchableCollectionModifier().read(0));
                            this.processDataWatcher(watcher);
                            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
                        }
                    }
                }
                
                private void processDataWatcher(final WrappedDataWatcher watcher) {
                    if (watcher != null && watcher.getObject(6) != null && watcher.getFloat(6) != 0.0f) {
                        watcher.setObject(6, (Object)1.0f);
                    }
                }
            });
        }
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Client.POSITION }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketEvent(player, player.getLocation().getYaw(), player.getLocation().getPitch(), PacketTypes.POSITION));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Server.POSITION }) {
            public void onPacketSending(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                final User user = Fiona.getUserManager().getUser(player.getUniqueId());
                if (user != null && user.isCancelled() != CancelType.MOVEMENT) {
                    user.setPosPacket(user.getPosPackets() + 1);
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Client.KEEP_ALIVE }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKeepAliveEvent(player, PacketKeepAliveEvent.PacketKeepAliveType.CLIENT));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Server.KEEP_ALIVE }) {
            public void onPacketSending(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKeepAliveEvent(player, PacketKeepAliveEvent.PacketKeepAliveType.SERVER));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Client.CHAT }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                if (PlayerUtils.noChats.contains(player)) {
                    if (((String)event.getPacket().getStrings().read(0)).equalsIgnoreCase("done")) {
                        player.sendMessage(Color.Green + "Left debug mode!");
                        PlayerUtils.noChats.remove(player);
                    }
                    if (((String)event.getPacket().getStrings().read(0)).equalsIgnoreCase("help")) {
                        player.sendMessage(Color.Dark_Gray + Color.Strikethrough + "-----------------------------");
                        final JsonMessage one = new JsonMessage();
                        one.addText(Color.Gray + "- ");
                        one.addText(Color.White + Color.Italics + "Create information log!").addHoverText(Color.Green + "Click me to generate information log").setClickEvent(JsonMessage.ClickableType.RunCommand, "fiona debug generatelog");
                        one.sendToPlayer(player);
                        player.sendMessage(Color.Dark_Gray + Color.Strikethrough + "-----------------------------");
                    }
                    event.setCancelled(true);
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Client.POSITION_LOOK }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketEvent(player, (float)event.getPacket().getFloat().read(0), (float)event.getPacket().getFloat().read(1), PacketTypes.POSLOOK));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Client.LOOK }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketEvent(player, player.getLocation().getYaw(), player.getLocation().getPitch(), PacketTypes.LOOK));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Client.ARM_ANIMATION }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKillauraEvent(player, null, PacketTypes.SWING));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Fiona.getAC(), new PacketType[] { PacketType.Play.Client.FLYING }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketEvent(player, player.getLocation().getYaw(), player.getLocation().getPitch(), PacketTypes.FLYING));
            }
        });
    }
    
    public Class<?> getNMSClass(final String name) {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
