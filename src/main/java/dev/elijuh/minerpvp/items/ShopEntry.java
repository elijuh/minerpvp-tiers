package dev.elijuh.minerpvp.items;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class ShopEntry {
    private final String display;
    private final ItemStack item;
    private final int buyPrice, sellPrice;

    public ItemStack getItem() {
        return item.clone();
    }
}
