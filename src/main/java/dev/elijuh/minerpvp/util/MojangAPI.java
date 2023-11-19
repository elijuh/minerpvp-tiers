package dev.elijuh.minerpvp.util;

import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

@UtilityClass
public class MojangAPI {
    private final JsonParser parser = new JsonParser();

    public UUID getUUID(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            URLConnection connection = url.openConnection();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonElement parse = parser.parse(reader);
            if (parse == null || !parse.isJsonObject()) return null;
            String id = parse.getAsJsonObject().get("id").getAsString();
            return new UUID(Long.parseLong(id.substring(0, 16)), Long.parseLong(id.substring(16, 32)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public WrappedSignedProperty getSkin(UUID uuid) {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString() + "?unsigned=false");
            URLConnection connection = url.openConnection();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonElement parse = parser.parse(reader);
            if (parse == null || !parse.isJsonObject()) return null;
            JsonArray properties = parse.getAsJsonObject().getAsJsonArray("properties");
            if (properties != null && properties.size() > 0) {
                JsonObject object = properties.get(1).getAsJsonObject();
                return new WrappedSignedProperty("textures", object.get("value").getAsString(), object.get("signature").getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
