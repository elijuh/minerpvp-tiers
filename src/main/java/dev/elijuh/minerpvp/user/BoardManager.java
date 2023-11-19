package dev.elijuh.minerpvp.user;

import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.hooks.LuckPermsHook;
import dev.elijuh.minerpvp.user.levels.LevelMath;
import dev.elijuh.minerpvp.user.levels.LevelPrestige;
import dev.elijuh.minerpvp.util.DocumentUtil;
import dev.elijuh.minerpvp.util.Text;
import dev.elijuh.minerpvp.util.feature.FastBoard;
import dev.elijuh.minerpvp.util.timers.CombatTimer;
import dev.elijuh.minerpvp.util.timers.PearlTimer;
import lombok.Getter;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

@Getter
public class BoardManager {
    private static final String TITLE = Text.color("&6&lMinerPvP &8⏐ &7[Tiers]");
    private static final NumberFormat format = NumberFormat.getInstance();
    private final Map<Player, FastBoard> boards = new HashMap<>();

    public BoardManager() {
        FastBoard.class.getClass();

        for (Player p : Bukkit.getOnlinePlayers()) {
            FastBoard board = new FastBoard(p);
            board.updateTitle(TITLE);
            boards.put(p, board);
        }

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void on(PlayerJoinEvent e) {
                FastBoard board = new FastBoard(e.getPlayer());
                board.updateTitle(TITLE);
                boards.put(e.getPlayer(), board);
            }

            @EventHandler
            public void on(PlayerQuitEvent e) {
                FastBoard board = boards.remove(e.getPlayer());
                if (board != null) {
                    board.delete();
                }
            }
        }, Core.i());
    }

    public void update() {
        for (Map.Entry<Player, FastBoard> entry : boards.entrySet()) {
            Player p = entry.getKey();
            User user = Core.i().getUserManager().getUsers().get(p.getUniqueId());
            if (user == null) continue;
            Integer combat = CombatTimer.getTimers().get(p.getUniqueId());
            Integer pearl = PearlTimer.getTimers().get(p.getUniqueId());
            int level = LevelMath.getLevel(user.getExp());
            LevelPrestige prestige = LevelPrestige.getPrestige(level);
            String icon = user.getLevelIcon().getIcon();
            entry.getValue().updateLines(
                    Text.color("&6┃ &7Rank &8» " + LuckPermsHook.getPrimaryGroup(p).map(Group::getDisplayName).orElse("&7None")),
                    Text.color("&6┃ " + prestige.color(level + icon)),
                    Text.color("&6┃"),
                    Text.color("&6┃ &7Coins &8» &a" + Text.compactNumber(user.getData().get("coins", 0L)) + "⛁"),
                    Text.color("&6┃ &7Kills &8» &f" + format.format(DocumentUtil.getDotNotation(user.getData(), "stats.kills", 0))),
                    Text.color("&6┃ &7Deaths &8» &f" + format.format(DocumentUtil.getDotNotation(user.getData(), "stats.deaths", 0))),
                    Text.color("&6┃ &7Streak &8» &f" + format.format(DocumentUtil.getDotNotation(user.getData(), "stats.killstreak", 0))),
                    combat != null ? Text.color("&6┃ &7Combat &8» &f" + combat + "s") : null,
                    pearl != null ? Text.color("&6┃ &7Pearl &8» &f" + pearl + "s") : null,
                    p.hasMetadata("staffmode") ? Text.color("&6┃") : null,
                    p.hasMetadata("staffmode") ? Text.color("&6┃ Staff Mode") : null,
                    p.hasMetadata("staffmode") ? Text.color("&6┃ &8» &7Vanish "
                            + (p.hasMetadata("vanished") ? "&aEnabled" : "&cDisabled")) : null,
                    Text.color("&6┃"),
                    Text.color("&6┃ &7&oplay.minerpvp.com")
            );
        }
    }

    public void unload() {
        boards.values().forEach(FastBoard::delete);
        boards.clear();
    }
}
