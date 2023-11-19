package dev.elijuh.minerpvp.items.menu;

import com.google.common.collect.ImmutableMap;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.items.ShopEntry;
import dev.elijuh.minerpvp.user.User;
import dev.elijuh.minerpvp.util.ItemBuilder;
import dev.elijuh.minerpvp.util.Text;
import dev.elijuh.minerpvp.util.feature.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShopMenu extends Menu {
    private static final NumberFormat format = NumberFormat.getInstance(Locale.US);
    private static final ItemStack close = ItemBuilder.create(Material.NETHER_STAR).name("&cExit").build();
    private static final Map<Integer, ShopEntry> entries = new ImmutableMap.Builder<Integer, ShopEntry>()
            .put(11, new ShopEntry("1 Golden Apple", ItemBuilder.create(Material.GOLDEN_APPLE).build(), 100, -1))
            .put(12, new ShopEntry("1 Ender Pearl", ItemBuilder.create(Material.ENDER_PEARL).build(), 5000, -1))
            .put(13, new ShopEntry("1 Fishing Rod", ItemBuilder.create(Material.FISHING_ROD).unbreakable(true).build(), 20000, -1))
            .put(14, new ShopEntry("1 Bow", ItemBuilder.create(Material.BOW).build(), 10000, -1))
            .put(15, new ShopEntry("16 Arrows", ItemBuilder.create(Material.ARROW).amount(16).build(), 500, -1))
            .put(20, new ShopEntry("1 Harming I Potion", ItemBuilder.create(Material.POTION).dura(16460).build(), 15000, -1))
            .put(21, new ShopEntry("1 Harming II Potion", ItemBuilder.create(Material.POTION).dura(16428).build(), 100000, -1))
            .put(23, new ShopEntry("1 Healing I Potion", ItemBuilder.create(Material.POTION).dura(16453).build(), 10000, -1))
            .put(24, new ShopEntry("1 Healing II Potion", ItemBuilder.create(Material.POTION).dura(16421).build(), 45000, -1))
            .build();
    private final Inventory inv;

    public ShopMenu() {
        inv = Bukkit.createInventory(this, 45, "Shop");
        fill();
        entries.forEach((slot, entry) -> {
            ItemStack item = entry.getItem();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            if (entry.getBuyPrice() > -1) {
                lore.add(Text.color("&aBuy &8» &e" + format.format(entry.getBuyPrice()) + "⛁ &7(Left-Click)"));
            }
            if (entry.getSellPrice() > -1) {
                lore.add(Text.color("&aSell &8» &e" + format.format(entry.getSellPrice()) + "⛁ &7(Right-Click)"));
                lore.add(Text.color("&7Shift-Click to sell all."));
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(slot, item);
        });
        inv.setItem(40, close);
    }

    @Override
    public void handle(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getRawSlot() == 40) {
            e.getWhoClicked().closeInventory();
            return;
        }
        ShopEntry entry = entries.get(e.getRawSlot());
        if (entry == null) return;
        Player p = (Player) e.getWhoClicked();
        User user = Core.i().getUserManager().getUsers().get(p.getUniqueId());
        if (user == null) return;
        if (e.getAction() == InventoryAction.PICKUP_ALL) {
            long coins = user.getData().get("coins", 0L);
            if (coins >= entry.getBuyPrice()) {
                user.getData().put("coins", coins - entry.getBuyPrice());
                user.getMongoSets().put("coins", user.getData().get("coins", 0L));
                p.getInventory().addItem(entry.getItem()).forEach((i, item)->
                        p.getWorld().dropItem(p.getLocation(), item)
                );
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
                p.sendMessage(Text.color("&6&lShop &8⏐ &7You have purchased &a" + entry.getDisplay() + " &7for &e" + entry.getBuyPrice() + "⛁&7."));
            } else {
                p.sendMessage(Text.color("&cYou don't have enough coins."));
            }
        } else if (e.getAction() == InventoryAction.PICKUP_HALF) {
            //todo sell
        } else if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            //todo sell all
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
