package dev.elijuh.minerpvp.mine;

import com.google.common.collect.ImmutableSet;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.items.SellableItem;
import dev.elijuh.minerpvp.items.TierCategory;
import dev.elijuh.minerpvp.items.TieredItem;
import dev.elijuh.minerpvp.items.menu.ItemUpgradeMenu;
import dev.elijuh.minerpvp.leaderboards.Leaderboard;
import dev.elijuh.minerpvp.leaderboards.sub.LevelLeaderboard;
import dev.elijuh.minerpvp.leaderboards.sub.RatioLeaderboard;
import dev.elijuh.minerpvp.user.User;
import dev.elijuh.minerpvp.util.MathUtil;
import dev.elijuh.minerpvp.util.Text;
import dev.elijuh.minerpvp.util.feature.npc.NPC;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.*;

@Getter
public class MineManager implements Listener {
    private final Set<NPC> npcs = ImmutableSet.of(new NPC(
            "&a&lSell Coal", Core.i().getStorage().getLocation("npc.coal"), "#MINER", p -> this.sellAll(p, SellableItem.COAL, SellableItem.COAL_BLOCK)
    ), new NPC(
            "&a&lSell All", Core.i().getStorage().getLocation("npc.sell-all"), "#PIRATE", p -> this.sellAll(p, SellableItem.all().toArray(new SellableItem[0]))
    ), new NPC(
            "&a&lUpgrader", Core.i().getStorage().getLocation("npc.upgrader"), "#BLACKSMITH", p -> Core.i().getUserManager().getUser(p.getUniqueId()).ifPresent(user -> p.openInventory(new ItemUpgradeMenu(user).getInventory()))
    ), new NPC(
            "&a&lStarter Kit", Core.i().getStorage().getLocation("npc.starter-kit"), "#KNIGHT", this::starterItems
    ));

    private final Set<Leaderboard> leaderboards = ImmutableSet.of(
            new Leaderboard("&6&lKills", "stats.kills", Core.i().getStorage().getLocation("leaderboards.kills")),
            new Leaderboard("&6&lCoins", "coins", Core.i().getStorage().getLocation("leaderboards.coins")),
            new LevelLeaderboard(Core.i().getStorage().getLocation("leaderboards.levels")),
            new Leaderboard("&6&lKillstreak", "stats.killstreak", Core.i().getStorage().getLocation("leaderboards.killstreaks")),
            new RatioLeaderboard("&6&lKDR", "stats.kills", "stats.deaths", Core.i().getStorage().getLocation("leaderboards.kdr"))
    );

    private final Set<Hologram> holograms = new HashSet<>();

    public MineManager() {
        Bukkit.getPluginManager().registerEvents(new MineListener(), Core.i());
        Bukkit.getPluginManager().registerEvents(this, Core.i());

        Bukkit.getScheduler().runTaskLater(Core.i(), ()-> {
            for (NPC npc : npcs) {
                Hologram hologram = new Hologram("npc-" + npc.getProfile().getUUID().toString(), npc.getLocation().clone().add(0, 2.6, 0), false);
                HologramPage page = hologram.getPage(0);
                page.addLine(new HologramLine(page, page.getNextLineLocation(), Text.color("&7") + "(Right-Click)"));
                DecentHologramsAPI.get().getHologramManager().registerHologram(hologram);
                holograms.add(hologram);
            }
        }, 20L);

        Bukkit.getScheduler().runTaskTimerAsynchronously(Core.i(), ()-> {
            Core.i().getUserManager().saveAll();
            leaderboards.forEach(Leaderboard::refresh);
        }, 0L, 1200L);
    }

    private final Map<UUID, Long> lastUsedStarter = new HashMap<>();

    private void starterItems(Player p) {
        long lastUsed = lastUsedStarter.getOrDefault(p.getUniqueId(), 0L);
        long timeLeft = 60000 - (System.currentTimeMillis() - lastUsed);
        if (timeLeft > 0) {
            p.sendMessage(Text.color("&7You cannot use this for another &c" + MathUtil.round(timeLeft / 1000.0, 1) + "s"));
        } else {
            for (TierCategory category :  TierCategory.values()) {
                TieredItem item = Core.i().getItemManager().getItem(category.name() + 1);
                if (item == null) continue;
                p.getInventory().addItem(item.getItem()).forEach((index, leftOver) -> p.getWorld().dropItem(p.getLocation(), leftOver));
            }
            lastUsedStarter.put(p.getUniqueId(), System.currentTimeMillis());
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        }
    }

    private void sellAll(Player p, SellableItem... types) {
        User user = Core.i().getUserManager().getUsers().get(p.getUniqueId());
        if (user == null) return;
        List<SellableItem> list = Arrays.asList(types);
        int total = 0;
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null) continue;
            NBTItem nbt = new NBTItem(item);
            SellableItem sellable = SellableItem.getById(nbt.getString("sell_id"));
            if (!list.contains(sellable)) continue;
            Integer worth = nbt.getInteger("worth");
            if (worth == null) continue;
            total += worth * item.getAmount();
            p.getInventory().setItem(i, null);
        }
        if (total != 0) {
            user.getData().put("coins", user.getData().get("coins", 0L) + total);
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
            p.sendMessage(Text.color("&a&lSell &8» &a+ &e" + NumberFormat.getInstance().format(total) + "⛁"));
        } else {
            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            p.sendMessage(Text.color("&cYou don't have anything to sell here!"));
        }
    }
}
