package dev.elijuh.minerpvp.items;

import dev.elijuh.minerpvp.util.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ItemManager {
    public static final int MAX_TIER = 20;
    private final Map<String, TieredItem> items = new HashMap<>();

    public ItemManager() {
        register(new TieredItem(1, TierCategory.PICKAXE, ItemBuilder.create(Material.WOOD_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 1)
        ));
        register(new TieredItem(2, TierCategory.PICKAXE, ItemBuilder.create(Material.WOOD_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 3)
        ));
        register(new TieredItem(3, TierCategory.PICKAXE, ItemBuilder.create(Material.WOOD_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 4)
        ));
        register(new TieredItem(4, TierCategory.PICKAXE, ItemBuilder.create(Material.WOOD_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 5)
        ));
        register(new TieredItem(5, TierCategory.PICKAXE, ItemBuilder.create(Material.STONE_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 2)
        ));
        register(new TieredItem(6, TierCategory.PICKAXE, ItemBuilder.create(Material.STONE_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 3)
        ));
        register(new TieredItem(7, TierCategory.PICKAXE, ItemBuilder.create(Material.STONE_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 4)
        ));
        register(new TieredItem(8, TierCategory.PICKAXE, ItemBuilder.create(Material.STONE_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 5)
        ));
        register(new TieredItem(9, TierCategory.PICKAXE, ItemBuilder.create(Material.IRON_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 4)
        ));
        register(new TieredItem(10, TierCategory.PICKAXE, ItemBuilder.create(Material.IRON_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 5)
        ));
        register(new TieredItem(11, TierCategory.PICKAXE, ItemBuilder.create(Material.DIAMOND_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 4)
        ));
        register(new TieredItem(12, TierCategory.PICKAXE, ItemBuilder.create(Material.DIAMOND_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 5)
        ));
        register(new TieredItem(13, TierCategory.PICKAXE, ItemBuilder.create(Material.DIAMOND_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 6)
        ));
        register(new TieredItem(14, TierCategory.PICKAXE, ItemBuilder.create(Material.DIAMOND_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 7)
        ));
        register(new TieredItem(15, TierCategory.PICKAXE, ItemBuilder.create(Material.DIAMOND_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 8)
                .enchant(Enchantment.LOOT_BONUS_BLOCKS, 1)
        ));
        register(new TieredItem(16, TierCategory.PICKAXE, ItemBuilder.create(Material.DIAMOND_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 9)
                .enchant(Enchantment.LOOT_BONUS_BLOCKS, 1)
        ));
        register(new TieredItem(17, TierCategory.PICKAXE, ItemBuilder.create(Material.DIAMOND_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 10)
                .enchant(Enchantment.LOOT_BONUS_BLOCKS, 2)
        ));
        register(new TieredItem(18, TierCategory.PICKAXE, ItemBuilder.create(Material.DIAMOND_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 15)
                .enchant(Enchantment.LOOT_BONUS_BLOCKS, 3)
        ));
        register(new TieredItem(19, TierCategory.PICKAXE, ItemBuilder.create(Material.DIAMOND_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 25)
                .enchant(Enchantment.LOOT_BONUS_BLOCKS, 4)
        ));
        register(new TieredItem(20, TierCategory.PICKAXE, ItemBuilder.create(Material.DIAMOND_PICKAXE)
                .enchant(Enchantment.DIG_SPEED, 50)
                .enchant(Enchantment.LOOT_BONUS_BLOCKS, 5)
        ));

        register(new TieredItem(1, TierCategory.SWORD, ItemBuilder.create(Material.WOOD_SWORD)
        ));
        register(new TieredItem(2, TierCategory.SWORD, ItemBuilder.create(Material.STONE_SWORD)
        ));
        register(new TieredItem(3, TierCategory.SWORD, ItemBuilder.create(Material.STONE_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 1)
        ));
        register(new TieredItem(4, TierCategory.SWORD, ItemBuilder.create(Material.STONE_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 2)
        ));
        register(new TieredItem(5, TierCategory.SWORD, ItemBuilder.create(Material.IRON_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 2)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 1)
        ));
        register(new TieredItem(6, TierCategory.SWORD, ItemBuilder.create(Material.IRON_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 3)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 1)
        ));
        register(new TieredItem(7, TierCategory.SWORD, ItemBuilder.create(Material.IRON_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 4)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 1)
        ));
        register(new TieredItem(8, TierCategory.SWORD, ItemBuilder.create(Material.IRON_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 5)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 2)
        ));
        register(new TieredItem(9, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 5)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 2)
        ));
        register(new TieredItem(10, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 5)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 3)
        ));
        register(new TieredItem(11, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 6)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 3)
        ));
        register(new TieredItem(12, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 7)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 3)
        ));
        register(new TieredItem(13, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 8)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 3)
        ));
        register(new TieredItem(14, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 9)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 3)
        ));
        register(new TieredItem(15, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 10)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 3)
        ));
        register(new TieredItem(16, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 11)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 4)
        ));
        register(new TieredItem(17, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 12)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 4)
        ));
        register(new TieredItem(18, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 13)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 4)
        ));
        register(new TieredItem(19, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 14)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 4)
        ));
        register(new TieredItem(20, TierCategory.SWORD, ItemBuilder.create(Material.DIAMOND_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 15)
                .enchant(Enchantment.LOOT_BONUS_MOBS, 5)
        ));

        register(new TieredItem(1, TierCategory.HELMET, ItemBuilder.create(Material.LEATHER_HELMET)
        ));
        register(new TieredItem(2, TierCategory.HELMET, ItemBuilder.create(Material.LEATHER_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        ));
        register(new TieredItem(3, TierCategory.HELMET, ItemBuilder.create(Material.LEATHER_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        ));
        register(new TieredItem(4, TierCategory.HELMET, ItemBuilder.create(Material.GOLD_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
        ));
        register(new TieredItem(5, TierCategory.HELMET, ItemBuilder.create(Material.GOLD_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
        ));
        register(new TieredItem(6, TierCategory.HELMET, ItemBuilder.create(Material.CHAINMAIL_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
        ));
        register(new TieredItem(7, TierCategory.HELMET, ItemBuilder.create(Material.IRON_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
        ));
        register(new TieredItem(8, TierCategory.HELMET, ItemBuilder.create(Material.IRON_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6)
        ));
        register(new TieredItem(9, TierCategory.HELMET, ItemBuilder.create(Material.IRON_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
        ));
        register(new TieredItem(10, TierCategory.HELMET, ItemBuilder.create(Material.IRON_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 8)
        ));
        register(new TieredItem(11, TierCategory.HELMET, ItemBuilder.create(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6)
        ));
        register(new TieredItem(12, TierCategory.HELMET, ItemBuilder.create(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
        ));
        register(new TieredItem(13, TierCategory.HELMET, ItemBuilder.create(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 8)
        ));
        register(new TieredItem(14, TierCategory.HELMET, ItemBuilder.create(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 9)
        ));
        register(new TieredItem(15, TierCategory.HELMET, ItemBuilder.create(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10)
        ));
        register(new TieredItem(16, TierCategory.HELMET, ItemBuilder.create(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 11)
        ));
        register(new TieredItem(17, TierCategory.HELMET, ItemBuilder.create(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 12)
        ));
        register(new TieredItem(18, TierCategory.HELMET, ItemBuilder.create(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 13)
        ));
        register(new TieredItem(19, TierCategory.HELMET, ItemBuilder.create(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 14)
        ));
        register(new TieredItem(20, TierCategory.HELMET, ItemBuilder.create(Material.DIAMOND_HELMET)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15)
        ));

        register(new TieredItem(1, TierCategory.CHESTPLATE, ItemBuilder.create(Material.LEATHER_CHESTPLATE)
        ));
        register(new TieredItem(2, TierCategory.CHESTPLATE, ItemBuilder.create(Material.LEATHER_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        ));
        register(new TieredItem(3, TierCategory.CHESTPLATE, ItemBuilder.create(Material.LEATHER_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        ));
        register(new TieredItem(4, TierCategory.CHESTPLATE, ItemBuilder.create(Material.GOLD_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
        ));
        register(new TieredItem(5, TierCategory.CHESTPLATE, ItemBuilder.create(Material.GOLD_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
        ));
        register(new TieredItem(6, TierCategory.CHESTPLATE, ItemBuilder.create(Material.CHAINMAIL_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
        ));
        register(new TieredItem(7, TierCategory.CHESTPLATE, ItemBuilder.create(Material.IRON_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
        ));
        register(new TieredItem(8, TierCategory.CHESTPLATE, ItemBuilder.create(Material.IRON_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6)
        ));
        register(new TieredItem(9, TierCategory.CHESTPLATE, ItemBuilder.create(Material.IRON_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
        ));
        register(new TieredItem(10, TierCategory.CHESTPLATE, ItemBuilder.create(Material.IRON_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 8)
        ));
        register(new TieredItem(11, TierCategory.CHESTPLATE, ItemBuilder.create(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6)
        ));
        register(new TieredItem(12, TierCategory.CHESTPLATE, ItemBuilder.create(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
        ));
        register(new TieredItem(13, TierCategory.CHESTPLATE, ItemBuilder.create(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 8)
        ));
        register(new TieredItem(14, TierCategory.CHESTPLATE, ItemBuilder.create(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 9)
        ));
        register(new TieredItem(15, TierCategory.CHESTPLATE, ItemBuilder.create(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10)
        ));
        register(new TieredItem(16, TierCategory.CHESTPLATE, ItemBuilder.create(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 11)
        ));
        register(new TieredItem(17, TierCategory.CHESTPLATE, ItemBuilder.create(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 12)
        ));
        register(new TieredItem(18, TierCategory.CHESTPLATE, ItemBuilder.create(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 13)
        ));
        register(new TieredItem(19, TierCategory.CHESTPLATE, ItemBuilder.create(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 14)
        ));
        register(new TieredItem(20, TierCategory.CHESTPLATE, ItemBuilder.create(Material.DIAMOND_CHESTPLATE)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15)
        ));

        register(new TieredItem(1, TierCategory.LEGGINGS, ItemBuilder.create(Material.LEATHER_LEGGINGS)
        ));
        register(new TieredItem(2, TierCategory.LEGGINGS, ItemBuilder.create(Material.LEATHER_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        ));
        register(new TieredItem(3, TierCategory.LEGGINGS, ItemBuilder.create(Material.LEATHER_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        ));
        register(new TieredItem(4, TierCategory.LEGGINGS, ItemBuilder.create(Material.GOLD_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
        ));
        register(new TieredItem(5, TierCategory.LEGGINGS, ItemBuilder.create(Material.GOLD_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
        ));
        register(new TieredItem(6, TierCategory.LEGGINGS, ItemBuilder.create(Material.CHAINMAIL_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
        ));
        register(new TieredItem(7, TierCategory.LEGGINGS, ItemBuilder.create(Material.IRON_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
        ));
        register(new TieredItem(8, TierCategory.LEGGINGS, ItemBuilder.create(Material.IRON_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6)
        ));
        register(new TieredItem(9, TierCategory.LEGGINGS, ItemBuilder.create(Material.IRON_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
        ));
        register(new TieredItem(10, TierCategory.LEGGINGS, ItemBuilder.create(Material.IRON_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 8)
        ));
        register(new TieredItem(11, TierCategory.LEGGINGS, ItemBuilder.create(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6)
        ));
        register(new TieredItem(12, TierCategory.LEGGINGS, ItemBuilder.create(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
        ));
        register(new TieredItem(13, TierCategory.LEGGINGS, ItemBuilder.create(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 8)
        ));
        register(new TieredItem(14, TierCategory.LEGGINGS, ItemBuilder.create(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 9)
        ));
        register(new TieredItem(15, TierCategory.LEGGINGS, ItemBuilder.create(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10)
        ));
        register(new TieredItem(16, TierCategory.LEGGINGS, ItemBuilder.create(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 11)
        ));
        register(new TieredItem(17, TierCategory.LEGGINGS, ItemBuilder.create(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 12)
        ));
        register(new TieredItem(18, TierCategory.LEGGINGS, ItemBuilder.create(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 13)
        ));
        register(new TieredItem(19, TierCategory.LEGGINGS, ItemBuilder.create(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 14)
        ));
        register(new TieredItem(20, TierCategory.LEGGINGS, ItemBuilder.create(Material.DIAMOND_LEGGINGS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15)
        ));

        register(new TieredItem(1, TierCategory.BOOTS, ItemBuilder.create(Material.LEATHER_BOOTS)
        ));
        register(new TieredItem(2, TierCategory.BOOTS, ItemBuilder.create(Material.LEATHER_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        ));
        register(new TieredItem(3, TierCategory.BOOTS, ItemBuilder.create(Material.LEATHER_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        ));
        register(new TieredItem(4, TierCategory.BOOTS, ItemBuilder.create(Material.GOLD_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
        ));
        register(new TieredItem(5, TierCategory.BOOTS, ItemBuilder.create(Material.GOLD_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
        ));
        register(new TieredItem(6, TierCategory.BOOTS, ItemBuilder.create(Material.CHAINMAIL_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
        ));
        register(new TieredItem(7, TierCategory.BOOTS, ItemBuilder.create(Material.IRON_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
        ));
        register(new TieredItem(8, TierCategory.BOOTS, ItemBuilder.create(Material.IRON_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6)
        ));
        register(new TieredItem(9, TierCategory.BOOTS, ItemBuilder.create(Material.IRON_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
        ));
        register(new TieredItem(10, TierCategory.BOOTS, ItemBuilder.create(Material.IRON_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 8)
        ));
        register(new TieredItem(11, TierCategory.BOOTS, ItemBuilder.create(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6)
        ));
        register(new TieredItem(12, TierCategory.BOOTS, ItemBuilder.create(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 7)
        ));
        register(new TieredItem(13, TierCategory.BOOTS, ItemBuilder.create(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 8)
        ));
        register(new TieredItem(14, TierCategory.BOOTS, ItemBuilder.create(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 9)
        ));
        register(new TieredItem(15, TierCategory.BOOTS, ItemBuilder.create(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10)
        ));
        register(new TieredItem(16, TierCategory.BOOTS, ItemBuilder.create(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 11)
        ));
        register(new TieredItem(17, TierCategory.BOOTS, ItemBuilder.create(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 12)
        ));
        register(new TieredItem(18, TierCategory.BOOTS, ItemBuilder.create(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 13)
        ));
        register(new TieredItem(19, TierCategory.BOOTS, ItemBuilder.create(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 14)
        ));
        register(new TieredItem(20, TierCategory.BOOTS, ItemBuilder.create(Material.DIAMOND_BOOTS)
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 15)
        ));
    }

    public TieredItem getItem(String id) {
        return items.get(id);
    }

    private void register(TieredItem item) {
        items.put(item.getId(), item);
    }

    public static String tierColor(int tier) {
        if (tier < 2) return "&7";
        if (tier < 4) return "&a";
        if (tier < 6) return "&b";
        if (tier < 8) return "&5";
        if (tier < 10) return "&6";
        if (tier < 12) return "&c";
        if (tier < 14) return "&d";
        if (tier < 16) return "&f&l";
        if (tier < 18) return "&e&l";
        if (tier < 20) return "&4&l";
        return "&1&l";
    }
}
