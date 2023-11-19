package dev.elijuh.minerpvp.items.menu;

import com.google.common.collect.ImmutableMap;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.items.ItemManager;
import dev.elijuh.minerpvp.items.TierCategory;
import dev.elijuh.minerpvp.util.ItemBuilder;
import dev.elijuh.minerpvp.util.feature.Menu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
public class TiersMenu extends Menu {
    private static final Map<Integer, TierCategory> categorySlots = new ImmutableMap.Builder<Integer, TierCategory>()
            .put(10, TierCategory.SWORD)
            .put(11, TierCategory.PICKAXE)
            .put(13, TierCategory.HELMET)
            .put(14, TierCategory.CHESTPLATE)
            .put(15, TierCategory.LEGGINGS)
            .put(16, TierCategory.BOOTS).build();
    private static final ItemStack exit = ItemBuilder.create(Material.NETHER_STAR).name("&cExit").build();
    private static final ItemStack back = ItemBuilder.create(Material.ARROW).name("&cBack to menu").build();

    private final Inventory inventory;

    private TierCategory category;

    public TiersMenu() {
        this.inventory = Bukkit.createInventory(this, 36, "Tiers");
        draw();
    }

    private void draw() {
        fill();
        if (category == null) {
            categorySlots.forEach((slot, cg) ->
                    inventory.setItem(slot, ItemBuilder.create(cg.getDisplayItem())
                    .name("&a" + cg.getDisplay() + (cg.getDisplay().endsWith("s") ? "" : "s"))
                    .lore("&7Click to view all tiers.").build()));
            inventory.setItem(31, exit);
        } else {
            for (int i = 0; i < ItemManager.MAX_TIER;) {
                inventory.setItem(i, Core.i().getItemManager().getItem(category.name() + ++i).getItem());
            }
            inventory.setItem(31, back);
        }
    }

    @Override
    public void handle(InventoryClickEvent e) {
        e.setCancelled(true);
        if (category == null) {
            TierCategory newCategory = categorySlots.get(e.getRawSlot());
            if (newCategory != null) {
                category = newCategory;
                draw();
            }
        }
        if (e.getRawSlot() == 31) {
            if (category == null) {
                e.getView().close();
            } else {
                category = null;
                draw();
            }
        }
    }
}
