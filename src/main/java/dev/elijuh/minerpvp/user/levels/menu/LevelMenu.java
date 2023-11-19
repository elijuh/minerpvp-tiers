package dev.elijuh.minerpvp.user.levels.menu;

import dev.elijuh.minerpvp.user.User;
import dev.elijuh.minerpvp.user.levels.LevelMath;
import dev.elijuh.minerpvp.user.levels.LevelPrestige;
import dev.elijuh.minerpvp.util.ItemBuilder;
import dev.elijuh.minerpvp.util.feature.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;

public class LevelMenu extends Menu {
    private static final NumberFormat FORMAT = NumberFormat.getInstance(Locale.US);
    private final Inventory inv;
    private final User user;
    private final Player p;

    public LevelMenu(User user, Player p) {
        this.user = user;
        this.p = p;
        inv = Bukkit.createInventory(this, 36, "Level Progress");
        fill();
        int level = LevelMath.getLevel(user.getExp());
        double progress = LevelMath.getLeftOverExp(user.getExp());
        LevelPrestige prestige = LevelPrestige.getPrestige(level);
        LevelPrestige nextPrestige = LevelPrestige.getPrestige(level + 1);
        inv.setItem(10, prestige.getIcon().clone()
                .name(level == prestige.getMinLevel() ?
                        prestige.color(prestige.getDisplayName()) :
                        prestige.color("Level " + level)
                ).lore("&aCurrent Level")
                .build());
        for (int i = 0; i < 5; i++) {
            inv.setItem(11 + i, ItemBuilder.create(Material.STAINED_GLASS_PANE)
                    .dura(progress > i * 1000 ? 5 : 4)
                    .name("&7Progress")
                    .lore("&5" + FORMAT.format(progress) + "&7/&5" + FORMAT.format(LevelMath.BASE))
                    .build());
        }
        inv.setItem(16, nextPrestige.getIcon().clone()
                .name(nextPrestige.color(level + 1 == nextPrestige.getMinLevel() ? nextPrestige.getDisplayName() : "Level " + (level + 1)))
                .lore("&aNext Level")
                .lore(" ")
                .lore("&6&lRewards &8»")
                .lore(LevelPrestige.getRewards(level + 1).stream().map(reward -> "&a⏐ " + reward.getDisplay()).collect(Collectors.toList()))
                .build());

        inv.setItem(inv.getSize() - 5, ItemBuilder.create(Material.NETHER_STAR).name("&eLevel Icons").lore("&7Customize your level icon.").build());
        p.openInventory(inv);
    }

    @Override
    public void handle(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getRawSlot() == getInventory().getSize() - 5) {
            p.openInventory(new LevelIconMenu(user, p).getInventory());
            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
