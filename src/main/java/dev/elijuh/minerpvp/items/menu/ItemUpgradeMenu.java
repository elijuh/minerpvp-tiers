package dev.elijuh.minerpvp.items.menu;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.items.ItemManager;
import dev.elijuh.minerpvp.items.TierCategory;
import dev.elijuh.minerpvp.items.TieredItem;
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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemUpgradeMenu extends Menu {
    private static final ItemStack info = ItemBuilder.create(Material.ANVIL).name("&aUpgrader").lore("&a⇦ &7Insert your item to upgrade!").build();
    private static final int insertSlot = 12, infoSlot = 13, resultSlot = 14;
    private final Inventory inv;
    private final User user;
    private TieredItem next;
    private int price;

    public ItemUpgradeMenu(User user) {
        inv = Bukkit.createInventory(this, 27, "Item Upgrader");
        this.user = user;
        fill();
        inv.setItem(insertSlot, null);
        inv.setItem(infoSlot, info);
        inv.setItem(resultSlot, null);
    }

    @Override
    public void handle(InventoryCloseEvent e) {
        ItemStack item = inv.getItem(insertSlot);
        if (item != null && item.getType() != Material.AIR) {
            for (ItemStack i : e.getPlayer().getInventory().addItem(item).values()) {
                e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), i);
            }
            e.getPlayer().sendMessage(Text.color("&7You have been given your item back."));
        }
    }

    @Override
    public void handle(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if (e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (e.getRawSlot() == insertSlot) {
                ItemStack item = inv.getItem(insertSlot);
                p.getInventory().addItem(item);
                inv.setItem(insertSlot, null);
                inv.setItem(resultSlot, null);
                next = null;
            } else if (e.getRawSlot() == resultSlot && next != null) {
                long coins = user.getData().get("coins", 0L);
                if (coins >= price) {
                    user.getData().put("coins", coins - price);
                } else {
                    p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                    p.sendMessage(Text.color("&cYou don't have enough coins."));
                    return;
                }
                e.getView().getBottomInventory().addItem(next.getItem());
                inv.setItem(insertSlot, null);
                inv.setItem(resultSlot, null);
                next = null;
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                p.playSound(p.getLocation(), Sound.ANVIL_USE, 0.5f, 2f);
            } else if (e.getClickedInventory() instanceof PlayerInventory) {
                ItemStack clicked = e.getCurrentItem();
                if (update(clicked)) {
                    ItemStack current = inv.getItem(insertSlot);
                    inv.setItem(insertSlot, clicked);
                    e.getClickedInventory().setItem(e.getSlot(), null);
                    if (current != null) {
                        e.getClickedInventory().addItem(current);
                    }
                }
            }
        }
    }

    public boolean update(ItemStack item) {
        if (item == null) {
            inv.setItem(resultSlot, null);
            return false;
        }
        NBTItem nbt = new NBTItem(item);
        TierCategory category;
        try {
            category = TierCategory.valueOf(nbt.getOrDefault("category", ""));
        } catch (IllegalArgumentException ex) {
            return false;
        }
        Integer tier = nbt.getInteger("tier");
        if (tier == null || tier == ItemManager.MAX_TIER) return false;
        next = Core.i().getItemManager().getItem(category.name() + (tier + 1));
        price = (tier < 7 ? 25 : 50) * ((int) Math.pow(tier + 1, 2));
        ItemStack display = next.getItem().clone();
        ItemMeta meta = display.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.add(" ");
        lore.add(Text.color("&aPrice &8» &e" + NumberFormat.getInstance().format(price) + "⛁"));
        lore.add(Text.color("&7") + "Left-Click to buy.");
        meta.setLore(lore);
        display.setItemMeta(meta);
        inv.setItem(resultSlot, display);
        return true;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
