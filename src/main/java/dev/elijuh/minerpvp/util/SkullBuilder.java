package dev.elijuh.minerpvp.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class SkullBuilder extends ItemBuilder {
    private static final Field PROFILE_FIELD;

    static {
        Field field = null;

        try {
            Class<?> clazz = ReflectionUtil.obcClass("inventory.CraftMetaSkull");
            field = clazz.getDeclaredField("profile");
            field.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PROFILE_FIELD = field;
    }

    protected SkullBuilder() {
        super(Material.SKULL_ITEM);
        dura(3);
    }

    public static SkullBuilder create() {
        return new SkullBuilder();
    }

    public SkullBuilder owner(String name) {
        SkullMeta skullMeta = (SkullMeta) getMeta();
        skullMeta.setOwner(name);
        return this;
    }

    public SkullBuilder texture(String url) {
        if (PROFILE_FIELD == null) {
            return this;
        }

        SkullMeta skullMeta = (SkullMeta) getMeta();
        byte[] bytes = ("{\"textures\":{\"SKIN\":{\"url\":\"" + url + "\"}}}").getBytes();
        String texture = Base64.getEncoder().encodeToString(bytes);
        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(url.getBytes()), null);
        profile.getProperties().put("textures", new Property("textures", texture));

        try {
            PROFILE_FIELD.set(skullMeta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }
}
