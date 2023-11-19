package dev.elijuh.minerpvp.items;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.minerpvp.mine.MineableType;
import dev.elijuh.minerpvp.util.ItemBuilder;
import dev.elijuh.minerpvp.util.Text;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Getter
public class TieredItem {
    private final int tier;
    private final TierCategory category;
    private final ItemStack item;

    public TieredItem(int tier, TierCategory category, ItemBuilder builder) {
        this.tier = tier;
        this.category = category;
        builder.unbreakable(true).name(
                "&7(Tier " + ItemManager.tierColor(tier) + Text.romanNumeral(tier) + "&7) &a" + category.getDisplay()
        );

        Map<Enchantment, Integer> enchants = builder.getMeta().getEnchants();
        if (!enchants.isEmpty()) {
            builder.flag(ItemFlag.HIDE_ENCHANTS).lore("&6&lUpgrades");
            enchants.forEach((enchant, level) -> builder
                    .lore("&6» &7" + Text.getEnchantName(enchant) + " &a" + Text.romanNumeral(level))
            );
        }

        if (category == TierCategory.PICKAXE) {
            List<MineableType> mineable = MineableType.getMineable(tier);

            if (!mineable.isEmpty()) {
                mineable.sort(Comparator.comparingInt(MineableType::getMinTier));
                builder.lore(" ").lore("&a&lMineable Ores");
                for (MineableType type : mineable) {
                    builder.lore("&a» &7" + type.getDisplay());
                }
            }
        }

        if (category == TierCategory.PICKAXE) {
            builder.flag(ItemFlag.HIDE_ATTRIBUTES);
        }

        if (tier == ItemManager.MAX_TIER) {
            builder.lore(" ")
                    .lore("&7(&a&lFully Upgraded&7)");
        }

        NBTItem nbt = new NBTItem(builder.build());
        nbt.setInteger("tier", tier);
        nbt.setString("category", category.name());
        this.item = nbt.getItem();
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public String getId() {
        return category.name() + tier;
    }
}
