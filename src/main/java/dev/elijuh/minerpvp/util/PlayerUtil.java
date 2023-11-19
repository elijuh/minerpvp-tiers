package dev.elijuh.minerpvp.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.mojang.authlib.GameProfile;
import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Map;

@UtilityClass
public class PlayerUtil {

    @SuppressWarnings("deprecation")
    public void sendTitle(Player p, EnumWrappers.TitleAction action, String title, int fadeIn, int stay, int fadeOut) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.TITLE);
        packet.getChatComponents().write(0, WrappedChatComponent.fromLegacyText(Text.color(title)));
        packet.getTitleActions().write(0, action);
        packet.getIntegers().write(0, fadeIn);
        packet.getIntegers().write(1, stay);
        packet.getIntegers().write(2, fadeOut);
        ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
    }

    public void sendActionBar(Player p, String message) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.CHAT);
        packet.getBytes().write(0, (byte)2);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(Text.color(message)));
        ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
    }

    private final Field usercacheField = ReflectionUtil.getField(
        ReflectionUtil.nmsClass("UserCache", "idk"),
        "c"
    );

    private final Field userCacheEntryGameProfileField = ReflectionUtil.getField(
        ReflectionUtil.nmsClass("UserCache$UserCacheEntry", "idk"),
        "b"
    );

    static {
        usercacheField.setAccessible(true);
        userCacheEntryGameProfileField.setAccessible(true);
    }

    @SuppressWarnings("unchecked")
    public GameProfile getOfflineProfileWithoutMojang(String name) {
        OfflinePlayer op = Bukkit.getPlayerExact(name);
        if (op != null) {
            return new GameProfile(op.getUniqueId(), op.getName());
        }

        try {
            Map<String, Object> map = (Map<String, Object>) usercacheField.get(MinecraftServer.getServer().getUserCache());
            Object entry = map.get(name);

            return entry == null ? null : (GameProfile) userCacheEntryGameProfileField.get(entry);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
