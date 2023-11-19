package dev.elijuh.minerpvp.user;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.hooks.LuckPermsHook;
import dev.elijuh.minerpvp.util.Text;
import lombok.Getter;
import org.bson.Document;
import org.bson.diagnostics.Loggers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class UserManager implements Listener {
    private final Map<UUID, User> users = new HashMap<>();
    private final MongoClient client;
    private final MongoCollection<Document> collection;

    public UserManager() {
        try {
            Field field = Loggers.class.getDeclaredField("USE_SLF4J");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        client = MongoClients.create(Core.i().getConfig().getString("mongo.connection-string"));
        collection = client.getDatabase("minerpvp").getCollection("userdata");

        Bukkit.getPluginManager().registerEvents(this, Core.i());
        Bukkit.getPluginManager().registerEvents(new UserListener(), Core.i());
    }

    public Optional<User> getUser(UUID uuid) {
        return Optional.ofNullable(users.get(uuid));
    }

    public Optional<User> getUser(String name) {
        for (User user : users.values()) {
            if (user.getData().getString("name").equalsIgnoreCase(name)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Document fetch(UUID uuid, String name) {
        Document data = collection.find(Filters.eq("_id", uuid.toString())).first();
        if (data != null) return data;

        return create(uuid, name);
    }

    private Document create(UUID uuid, String name) {
        Document data = new Document("_id", uuid.toString()).append("name", name);
        collection.insertOne(data);
        return data;
    }

    public void saveAll() {
        users.values().forEach(User::save);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(AsyncPlayerPreLoginEvent e) {
        if (e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        users.put(e.getUniqueId(), new User(e.getUniqueId(), e.getName()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Optional<User> op = Core.i().getUserManager().getUser(p.getUniqueId());
        if (op.isPresent()) {
            net.luckperms.api.model.user.User lpUser = LuckPermsHook.getUser(p);
            String prefix = lpUser.getCachedData().getMetaData().getPrefix();
            if (prefix == null) prefix = "&7";
            op.get().getMongoSets().put("coloredName", ChatColor.getLastColors(Text.color(prefix)) + p.getName());
            op.get().setPlayer(p);
        } else {
            p.kickPlayer(Text.color("&cFailed to load data, please rejoin."));
        }
        e.setJoinMessage(null);
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        String prefix = LuckPermsHook.getUser(e.getPlayer()).getCachedData().getMetaData().getPrefix();
        if (prefix == null) prefix = "";

        e.setQuitMessage(null);
        User user = users.remove(e.getPlayer().getUniqueId());
        if (user != null) {
            user.getMongoSets().append("coloredName", ChatColor.getLastColors(Text.color(prefix)) + e.getPlayer().getName());
            CompletableFuture.runAsync(user::save);
        }
    }
}
