package dev.elijuh.minerpvp.user.levels.menu;

import dev.elijuh.minerpvp.user.User;
import dev.elijuh.minerpvp.user.levels.LevelIcon;
import dev.elijuh.minerpvp.user.levels.LevelMath;
import dev.elijuh.minerpvp.user.levels.LevelPrestige;
import dev.elijuh.minerpvp.util.ItemBuilder;
import dev.elijuh.minerpvp.util.Text;
import dev.elijuh.minerpvp.util.feature.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class LevelIconMenu extends Menu {
    private final Inventory inv;
    private final User user;
    private final Player p;
    private final Map<Integer, LevelIcon> icons = new HashMap<>();

    public LevelIconMenu(User user, Player p) {
        this.user = user;
        this.p = p;
        inv = Bukkit.createInventory(this, 54, "Level Icons");
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, FILLER);
            inv.setItem(inv.getSize() - 9 + i, FILLER);
        }
        int level = LevelMath.getLevel(user.getExp());
        int index = 9;
        for (LevelIcon icon : LevelIcon.values()) {
            if (index > 35) {
                continue;
            }
            ItemStack item = (icon.getPrestige() != null ? icon.getPrestige().getIcon().clone() : ItemBuilder.create(Material.NETHER_STAR))
                    .name(icon.getDisplayName())
                    .lore(" ")
                    .lore("&7Preview &8» " + LevelPrestige.getPrestige(level).getPrefix(level, icon.getIcon()))
                    .lore(" ")
                    .lore(icon.has(user) ? (user.getLevelIcon() == icon ? "&8» &aCurrently Selected." : "&8» &eClick to select.") : "&cYou haven't unlocked this icon.").build();

            icons.put(index, icon);
            inv.setItem(index++, item);
        }
    }

    @Override
    public void handle(InventoryClickEvent e) {
        e.setCancelled(true);
        LevelIcon icon = icons.get(e.getRawSlot());

        if (icon != null) {
            if (user.getLevelIcon() == icon) {
                e.getView().close();
                p.sendMessage(Text.color("&cYou already have this icon selected!"));
            } else if (icon.has(user)) {
                e.getView().close();
                user.setLevelIcon(icon);
                p.sendMessage(Text.color("&aYou have selected the &r" + icon.getDisplayName() + " &aLevel Icon!"));
                p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 2f);
            } else {
                e.getView().close();
                p.sendMessage(Text.color("&cYou have not unlocked that level icon!"));
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
