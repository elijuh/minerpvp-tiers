package dev.elijuh.minerpvp;

import com.google.common.collect.Lists;
import dev.elijuh.minerpvp.command.CommandManager;
import dev.elijuh.minerpvp.command.commands.*;
import dev.elijuh.minerpvp.hooks.LuckPermsHook;
import dev.elijuh.minerpvp.hooks.MinerPvPExpansion;
import dev.elijuh.minerpvp.items.ItemManager;
import dev.elijuh.minerpvp.leaderboards.Leaderboard;
import dev.elijuh.minerpvp.mine.MineListener;
import dev.elijuh.minerpvp.mine.MineManager;
import dev.elijuh.minerpvp.user.BoardManager;
import dev.elijuh.minerpvp.user.User;
import dev.elijuh.minerpvp.user.UserManager;
import dev.elijuh.minerpvp.util.Text;
import dev.elijuh.minerpvp.util.YamlFile;
import dev.elijuh.minerpvp.util.feature.Library;
import dev.elijuh.minerpvp.util.feature.Menu;
import dev.elijuh.minerpvp.util.feature.npc.NPC;
import dev.elijuh.minerpvp.util.timers.CombatTimer;
import dev.elijuh.minerpvp.util.timers.PearlTimer;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class Core extends JavaPlugin {
    private static Core instance;

    private YamlFile storage;

    private CommandManager commandManager;
    private ItemManager itemManager;
    private UserManager userManager;
    private MineManager mineManager;
    private BoardManager boardManager;

    @Override
    public void onLoad() {
        for (Library lib : Library.values()) {
            lib.load();
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        de.tr7zw.changeme.nbtapi.utils.MinecraftVersion.disableBStats();
        xyz.xenondevs.particle.utils.ReflectionUtils.setPlugin(this);
        Menu.init();

        storage = new YamlFile(new File(getDataFolder(), "storage.yml"));
        storage.addDefault("npc.coal", "map,0,0,0,0,0");
        storage.addDefault("npc.sell-all", "map,0,0,0,0,0");
        storage.addDefault("npc.upgrader", "map,0,0,0,0,0");
        storage.addDefault("npc.starter-kit", "map,0,0,0,0,0");
        storage.addDefault("leaderboards.kills", "map,0,0,0,0,0");
        storage.addDefault("leaderboards.coins", "map,0,0,0,0,0");
        storage.addDefault("leaderboards.deaths", "map,0,0,0,0,0");
        storage.addDefault("leaderboards.killstreaks", "map,0,0,0,0,0");
        storage.addDefault("leaderboards.kdr", "map,0,0,0,0,0");
        storage.addDefault("spawn", "map,0,0,0,0,0");
        storage.save();

        getConfig().options().copyDefaults(true);
        getConfig().addDefault("mongo.connection-string", "");
        getConfig().addDefault("stat-affecting-worlds", Lists.newArrayList("map"));
        saveConfig();

        commandManager = new CommandManager();
        itemManager = new ItemManager();
        userManager = new UserManager();
        mineManager = new MineManager();
        boardManager = new BoardManager();

        for (Player p : Bukkit.getOnlinePlayers()) {
            User user = new User(p.getUniqueId(), p.getName());
            user.setPlayer(p);
            userManager.getUsers().put(p.getUniqueId(), user);
            for (NPC npc : mineManager.getNpcs()) {
                if (npc.isInRange(p.getLocation()) && npc.getYawDiff(p.getLocation()) < 70) {
                    npc.show(p);
                }
            }
            net.luckperms.api.model.user.User lpUser = LuckPermsHook.getUser(p);
            String prefix = lpUser.getCachedData().getMetaData().getPrefix();
            if (prefix == null) prefix = "&7";

            user.getData().put("coloredName", ChatColor.getLastColors(Text.color(prefix)) + p.getName());
            user.getMongoSets().put("coloredName", ChatColor.getLastColors(Text.color(prefix)) + p.getName());
        }

        commandManager.register(new DevCommand());
        commandManager.register(new GiveTieredItemCommand());
        commandManager.register(new CoinsCommand());
        commandManager.register(new TiersCommand());
        commandManager.register(new SpawnCommand());
        commandManager.register(new ShopCommand());
        commandManager.register(new InventoryManagerCommand());
        commandManager.register(new PlayerVaultCommand());
        commandManager.register(new StatsCommand());
        commandManager.register(new LevelCommand());
        commandManager.register(new SoundCommand());

        new MinerPvPExpansion().register();
        Bukkit.getPluginManager().registerEvents(new CombatTimer(), this);
        Bukkit.getPluginManager().registerEvents(new PearlTimer(), this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, ()-> {
            PearlTimer.update();
            CombatTimer.update();
            boardManager.update();
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        if (commandManager != null) {
            commandManager.unregisterAll();
        }
        if (boardManager != null) {
            boardManager.unload();
        }
        if (!NPC.getNpcs().isEmpty()) {
            NPC.deleteAll();
        }
        MinerPvPExpansion.unload();
        for (User user : userManager.getUsers().values()) {
            user.save();
        }
        userManager.getClient().close();
        try {
            for (Player p : Bukkit.getOnlinePlayers()) {
                InventoryView view = p.getOpenInventory();
                if (view != null && view.getTopInventory().getHolder() instanceof Menu) {
                    p.closeInventory();
                }
            }
        } catch (Exception ignored) {}
        if (mineManager != null) {
            for (Hologram hologram : mineManager.getHolograms()) {
                hologram.destroy();
            }
            for (Leaderboard lb : mineManager.getLeaderboards()) {
                lb.getHologram().destroy();
            }
            MineListener.getNotReplaced().forEach(Block::setType);
        }
    }

    public static Core i() {
        return instance;
    }
}
