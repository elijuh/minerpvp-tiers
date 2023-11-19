package dev.elijuh.minerpvp.items;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

@Getter
@RequiredArgsConstructor
public enum TierCategory {
    SWORD("Sword", Material.DIAMOND_SWORD),
    PICKAXE("Pickaxe", Material.DIAMOND_PICKAXE),
    HELMET("Helmet", Material.DIAMOND_HELMET),
    CHESTPLATE("Chestplate", Material.DIAMOND_CHESTPLATE),
    LEGGINGS("Leggings", Material.DIAMOND_LEGGINGS),
    BOOTS("Boots", Material.DIAMOND_BOOTS);

    private final String display;
    private final Material displayItem;
}
