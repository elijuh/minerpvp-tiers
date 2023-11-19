package dev.elijuh.minerpvp.mine;

import dev.elijuh.minerpvp.items.SellableItem;
import lombok.Getter;
import org.bukkit.Material;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class RegeneratingOre {
    private final Material material;
    private final Map<Integer, Material> regenChances;
    private final int ticks, maxIndex;
    private final SellableItem drop;

    public RegeneratingOre(Material material, Map<Material, Integer> regenChances, int ticks, SellableItem drop) {
        this.material = material;
        this.ticks = ticks;
        this.drop = drop;

        this.regenChances = new LinkedHashMap<>(regenChances.size());
        int index = 0;
        for (Map.Entry<Material, Integer> entry : regenChances.entrySet()) {
            this.regenChances.put(index, entry.getKey());
            index += entry.getValue();
        }
        maxIndex = index;
    }

    public Material random() {
        int index = ThreadLocalRandom.current().nextInt(maxIndex);

        return getForIndex(index);
    }

    private Material getForIndex(int index) {
        Material highest = null;
        for (int i : regenChances.keySet()) {
            if (index >= i) {
                highest = regenChances.get(i);
            }
        }
        return highest;
    }
}
