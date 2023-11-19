package dev.elijuh.minerpvp.items;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
public class SellableItem {
    private static final Map<String, SellableItem> items = new HashMap<>();

    public static final SellableItem
            COAL = new SellableItem("&8Coal", 1, Material.COAL, (byte)0),
            COAL_BLOCK = new SellableItem("&8Coal Block", 9, Material.COAL_BLOCK, (byte)0),
            IRON_INGOT = new SellableItem("&fIron Ingot", 2, Material.IRON_INGOT, (byte)0),
            IRON_BLOCK = new SellableItem("&fIron Block", 18, Material.IRON_BLOCK, (byte)0),
            GOLD_INGOT = new SellableItem("&6Gold Ingot", 3, Material.GOLD_INGOT, (byte)0),
            GOLD_BLOCK = new SellableItem("&6Gold Block", 27, Material.GOLD_BLOCK, (byte)0),
            REDSTONE = new SellableItem("&cRedstone", 4, Material.REDSTONE, (byte)0),
            REDSTONE_BLOCK = new SellableItem("&cRedstone Block", 36, Material.REDSTONE_BLOCK, (byte)0),
            LAPIS = new SellableItem("&9Lapis", 4, Material.INK_SACK, (byte)4),
            LAPIS_BLOCK = new SellableItem("&9Lapis Block", 36, Material.LAPIS_BLOCK, (byte)0),
            EMERALD = new SellableItem("&aEmerald", 5, Material.EMERALD, (byte)0),
            EMERALD_BLOCK = new SellableItem("&aEmerald Block", 45, Material.EMERALD_BLOCK, (byte)0),
            DIAMOND = new SellableItem("&bDiamond", 6, Material.DIAMOND, (byte)0),
            DIAMOND_BLOCK = new SellableItem("&bDiamond Block", 54, Material.DIAMOND_BLOCK, (byte)0);

    private final String id, display;
    private final int worth;
    private final Material material;
    private final byte data;

    public SellableItem(String display, int worth, Material material, byte data) {
        this.id = material.name() + ":" + data;
        this.display = display;
        this.worth = worth;
        this.material = material;
        this.data = data;
        items.put(id, this);
    }

    public static SellableItem getByBlock(Block b) {
        return items.get(b.getType().name() + ":" + b.getData());
    }

    public static SellableItem getById(String id) {
        if (id == null) return null;
        return items.get(id);
    }

    public static Collection<SellableItem> all() {
        return items.values();
    }
}
