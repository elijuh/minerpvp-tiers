package dev.elijuh.minerpvp.mine;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum MineableType {
    COAL(1, ImmutableSet.of(Material.COAL_ORE, Material.COAL_BLOCK), "Coal"),
    IRON(5, ImmutableSet.of(Material.IRON_ORE, Material.IRON_BLOCK), "Iron"),
    GOLD(10, ImmutableSet.of(Material.GOLD_ORE, Material.GOLD_BLOCK), "Gold"),
    LAPIS(12, ImmutableSet.of(Material.LAPIS_ORE, Material.LAPIS_BLOCK), "Lapis"),
    REDSTONE(12, ImmutableSet.of(Material.GLOWING_REDSTONE_ORE, Material.REDSTONE_ORE, Material.REDSTONE_BLOCK), "Redstone"),
    EMERALD(15, ImmutableSet.of(Material.EMERALD_ORE, Material.EMERALD_BLOCK), "Emerald"),
    DIAMOND(17, ImmutableSet.of(Material.DIAMOND_ORE, Material.DIAMOND_BLOCK), "Diamond");

    private final int minTier;
    private final Set<Material> materials;
    private final String display;

    public static List<MineableType> getMineable(int tier) {
        List<MineableType> types = new ArrayList<>();
        for (MineableType type : values()) {
            if (tier >= type.minTier) {
                types.add(type);
            }
        }
        return types;
    }

    public static boolean canTierMine(int tier, Material material) {
        for (MineableType type : values()) {
            if (tier >= type.minTier && type.materials.contains(material)) {
                return true;
            }
        }
        return false;
    }

}
