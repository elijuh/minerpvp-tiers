package dev.elijuh.minerpvp.util.feature.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.*;
import com.google.common.collect.Lists;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.util.MojangAPI;
import dev.elijuh.minerpvp.util.Text;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

@Getter
@SuppressWarnings("deprecation")
public class NPC {
    @Getter
    private static final Map<Integer, NPC> npcs = new HashMap<>();
    private static final Field ENTITY_ID;
    public static final int VIEW_RANGE = 48;
    static {
        Field field = null;
        try {
            field = MinecraftReflection.getEntityClass().getDeclaredField("entityCount");
            field.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ENTITY_ID = field;

        if (Core.i().isEnabled()) {
            ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Core.i(), ListenerPriority.LOWEST, PacketType.Play.Client.USE_ENTITY) {
                @Override
                public void onPacketReceiving(PacketEvent e) {
                    PacketContainer packet = e.getPacket();
                    EnumWrappers.EntityUseAction action = packet.getEntityUseActions().read(0);
                    if (action != EnumWrappers.EntityUseAction.INTERACT) return;

                    NPC npc = npcs.get(packet.getIntegers().read(0));
                    if (npc != null && npc.getOnClick() != null) {
                        Bukkit.getScheduler().runTask(Core.i(), () -> npc.getOnClick().accept(e.getPlayer()));
                        e.setCancelled(true);
                    }
                }
            });

            Bukkit.getPluginManager().registerEvents(new Listener() {
                private void refresh(Player p) {
                    for (NPC npc : npcs.values()) {
                        if (npc.isInRange(p.getLocation())) {
                            double yawDiff = npc.getYawDiff(p.getLocation());
                            if (!npc.getViewers().contains(p.getUniqueId()) && Math.abs(yawDiff) < 70) {
                                npc.show(p);
                            }
                        } else if (npc.getViewers().contains(p.getUniqueId())) {
                            npc.hide(p);
                        }
                    }
                }

                @EventHandler
                public void on(PlayerMoveEvent e) {
                    Bukkit.getScheduler().runTask(Core.i(), () -> refresh(e.getPlayer()));
                }

                @EventHandler
                public void on(PlayerTeleportEvent e) {
                    Bukkit.getScheduler().runTask(Core.i(), () -> refresh(e.getPlayer()));
                }

                @EventHandler
                public void on(PlayerRespawnEvent e) {
                    Bukkit.getScheduler().runTask(Core.i(), () -> refresh(e.getPlayer()));
                }

                @EventHandler
                public void on(PlayerJoinEvent e) {
                    for (NPC npc : npcs.values()) {
                        Player p = e.getPlayer();
                        double yawDiff = npc.getYawDiff(p.getLocation());
                        if (npc.isInRange(p.getLocation()) && Math.abs(yawDiff) < 70) {
                            npc.show(p);
                        }
                    }
                }

                @EventHandler
                public void on(PlayerQuitEvent e) {
                    npcs.values().forEach(npc -> npc.hide(e.getPlayer()));
                }
            }, Core.i());
        }
    }

    private final String name, skinId;
    private final Location location;
    private final int entityId;
    private final WrappedGameProfile profile;
    private final Consumer<Player> onClick;

    private final Set<UUID> viewers = new HashSet<>();

    public NPC(String name, Location location, String skinId, Consumer<Player> onClick) {
        this.name = Text.color(name);
        this.skinId = skinId;
        this.location = location;
        try {
            entityId = (int) ENTITY_ID.get(null);
            ENTITY_ID.set(null, entityId + 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        profile = new WrappedGameProfile(UUID.randomUUID(), this.name);

        if (skinId != null) {
            if (skinId.startsWith("#")) {
                NPCSkin skin = NPCSkin.fromId(skinId);
                if (skin != null) {
                    profile.getProperties().put("textures", skin.getTextures());
                }
            } else {
                UUID uuid = MojangAPI.getUUID(skinId);
                WrappedSignedProperty textures = uuid == null ? null : MojangAPI.getSkin(uuid);
                if (textures != null) {
                    profile.getProperties().put("textures", textures);
                }
            }
        }

        this.onClick = onClick;
        npcs.put(entityId, this);
    }

    public void show(Player... players) {
        for (Player player : players) {

            if (!player.getWorld().equals(location.getWorld())) return;
            WrappedGameProfile profile = new WrappedGameProfile(this.profile.getUUID(), this.profile.getName());
            profile.getProperties().putAll("textures", skinId == null ?
                    WrappedGameProfile.fromPlayer(player).getProperties().get("textures") :
                    this.profile.getProperties().get("textures"));

            PacketContainer addInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
            addInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
            addInfo.getPlayerInfoDataLists().write(0, Lists.newArrayList(new PlayerInfoData(
                    profile, 0, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(name)
            )));

            PacketContainer spawn = new PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
            spawn.getIntegers().write(0, entityId);
            spawn.getUUIDs().write(0, profile.getUUID());
            spawn.getIntegers().write(1, floor(location.getX() * 32.0));
            spawn.getIntegers().write(2, floor(location.getY() * 32.0));
            spawn.getIntegers().write(3, floor(location.getZ() * 32.0));
            spawn.getBytes().write(0, (byte)((int)(location.getYaw() * 256.0F / 360.0F)));
            spawn.getBytes().write(1, (byte)((int)(location.getPitch() * 256.0F / 360.0F)));
            spawn.getIntegers().write(4, 0);
            WrappedDataWatcher watcher = new WrappedDataWatcher();
            watcher.setObject(10, (byte)0xFF);
            spawn.getDataWatcherModifier().write(0, watcher);

            PacketContainer animation = new PacketContainer(PacketType.Play.Server.ANIMATION);
            animation.getIntegers().write(0, entityId);
            animation.getIntegers().write(1, 0);

            PacketContainer removeInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
            removeInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
            removeInfo.getPlayerInfoDataLists().write(0, Lists.newArrayList(new PlayerInfoData(
                    profile, 0, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(name)
            )));

            PacketContainer rotation = new PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
            rotation.getIntegers().write(0, entityId);
            rotation.getBytes().write(0, (byte)((int)(location.getYaw() * 256.0F / 360.0F)));
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, addInfo);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawn);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, rotation);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, animation);
            Bukkit.getScheduler().runTaskLater(Core.i(), ()-> {
                if (!player.isOnline() || !viewers.contains(player.getUniqueId())) return;

                ProtocolLibrary.getProtocolManager().sendServerPacket(player, removeInfo);
            }, 50L);
            viewers.add(player.getUniqueId());
        }
    }

    public void hide(Player... players) {
        PacketContainer playerInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        playerInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        playerInfo.getPlayerInfoDataLists().write(0, Lists.newArrayList(new PlayerInfoData(
                profile, 0, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(name)
        )));

        PacketContainer destroy = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        destroy.getIntegerArrays().write(0, new int[]{entityId});

        for (Player player : players) {
            if (!viewers.contains(player.getUniqueId())) continue;
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, playerInfo);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, destroy);
            viewers.remove(player.getUniqueId());
        }
    }

    public void hideAll() {
        Set<Player> players = new HashSet<>();
        for (UUID uuid : viewers) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                players.add(p);
            }
        }
        hide(players.toArray(new Player[0]));
        viewers.clear();
    }

    public static void deleteAll() {
        for (NPC npc : npcs.values()) {
            npc.hideAll();
        }
        npcs.clear();
    }

    private static int floor(double var0) {
        int var2 = (int)var0;
        return var0 < (double)var2 ? var2 - 1 : var2;
    }

    public double getYawDiff(Location pLoc) {
        double calc = Math.atan2(location.getX() - pLoc.getX(), location.getZ() - pLoc.getZ()) * 57.2957795131f;
        double angle = ((pLoc.getYaw() + calc) % 360);
        if (angle > 180) {
            angle = angle - 360;
        }
        return angle;
    }

    public boolean isInRange(Location location) {
        return location.getWorld().equals(this.location.getWorld()) &&
                location.toVector().setY(0).distance(this.location.toVector().setY(0)) <= VIEW_RANGE && Math.abs(location.getY() - this.location.getY()) < 25;
    }
}
