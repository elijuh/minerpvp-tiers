package dev.elijuh.minerpvp.mine;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.collect.ImmutableMap;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.animations.OreMineAnimation;
import dev.elijuh.minerpvp.items.SellableItem;
import dev.elijuh.minerpvp.items.TierCategory;
import dev.elijuh.minerpvp.user.levels.LevelMath;
import dev.elijuh.minerpvp.util.ItemBuilder;
import dev.elijuh.minerpvp.util.PlayerUtil;
import dev.elijuh.minerpvp.util.Text;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MineListener implements Listener {
    private final NumberFormat format = NumberFormat.getInstance(Locale.US);
    private final Map<Material, RegeneratingOre> regens = new HashMap<>();

    public MineListener() {
        registerOre(new RegeneratingOre(Material.COAL_ORE, ImmutableMap.of(Material.COAL_ORE, 10, Material.COAL_BLOCK, 1), 100,
                SellableItem.COAL));
        registerOre(new RegeneratingOre(Material.COAL_BLOCK, ImmutableMap.of(Material.COAL_ORE, 1), 0,
                SellableItem.COAL_BLOCK));
        registerOre(new RegeneratingOre(Material.IRON_ORE, ImmutableMap.of(Material.IRON_ORE, 10, Material.IRON_BLOCK, 1), 100,
                SellableItem.IRON_INGOT));
        registerOre(new RegeneratingOre(Material.IRON_BLOCK, ImmutableMap.of(Material.IRON_ORE, 1), 0,
                SellableItem.IRON_BLOCK));
        registerOre(new RegeneratingOre(Material.GOLD_ORE, ImmutableMap.of(Material.GOLD_ORE, 10, Material.GOLD_BLOCK, 1), 100,
                SellableItem.GOLD_INGOT));
        registerOre(new RegeneratingOre(Material.GOLD_BLOCK, ImmutableMap.of(Material.GOLD_ORE, 1), 0,
                SellableItem.GOLD_BLOCK));
        registerOre(new RegeneratingOre(Material.LAPIS_ORE, ImmutableMap.of(Material.LAPIS_ORE, 10, Material.LAPIS_BLOCK, 1), 100,
                SellableItem.LAPIS));
        registerOre(new RegeneratingOre(Material.LAPIS_BLOCK, ImmutableMap.of(Material.LAPIS_ORE, 1), 0,
                SellableItem.LAPIS_BLOCK));
        registerOre(new RegeneratingOre(Material.GLOWING_REDSTONE_ORE, ImmutableMap.of(Material.REDSTONE_ORE, 10, Material.REDSTONE_BLOCK, 1), 100,
                SellableItem.REDSTONE));
        registerOre(new RegeneratingOre(Material.REDSTONE_BLOCK, ImmutableMap.of(Material.REDSTONE_ORE, 1), 0,
                SellableItem.REDSTONE_BLOCK));
        registerOre(new RegeneratingOre(Material.EMERALD_ORE, ImmutableMap.of(Material.EMERALD_ORE, 10, Material.EMERALD_BLOCK, 1), 100,
                SellableItem.EMERALD));
        registerOre(new RegeneratingOre(Material.EMERALD_BLOCK, ImmutableMap.of(Material.EMERALD_ORE, 1), 0,
                SellableItem.EMERALD_BLOCK));
        registerOre(new RegeneratingOre(Material.DIAMOND_ORE, ImmutableMap.of(Material.DIAMOND_ORE, 10, Material.DIAMOND_BLOCK, 1), 100,
                SellableItem.DIAMOND));
        registerOre(new RegeneratingOre(Material.DIAMOND_BLOCK, ImmutableMap.of(Material.DIAMOND_ORE, 1), 0,
                SellableItem.DIAMOND_BLOCK));
    }

    private void registerOre(RegeneratingOre ore) {
        regens.put(ore.getMaterial(), ore);
    }

    @Getter
    private static final Map<Block, Material> notReplaced = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() != GameMode.SURVIVAL) return;
        e.setCancelled(true);
        RegeneratingOre ore = regens.get(e.getBlock().getType());
        if (ore == null) return;
        ItemStack held = p.getItemInHand();
        if (held == null || held.getType() == Material.AIR || held.getAmount() == 0) return;
        NBTItem nbt = new NBTItem(held);
        Integer tier = nbt.getInteger("tier");
        String category = nbt.getString("category");
        if (tier == null || category == null || !category.equals(TierCategory.PICKAXE.name())) return;
        if (!MineableType.canTierMine(tier, e.getBlock().getType())) {
            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            p.sendMessage(Text.color("&cYour pickaxe isn't high enough tier to mine this!"));
            return;
        }
        SellableItem sellable = ore.getDrop();
        if (sellable == null) return;

        int fortune = held.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        int drops = 1 + (fortune > 0 ? ThreadLocalRandom.current().nextInt(fortune) : 0);
        NBTItem nbtDrop = new NBTItem(ItemBuilder.create(sellable.getMaterial())
                .amount(drops)
                .dura(sellable.getData())
                .name("&7Mining Drop: " + sellable.getDisplay())
                .lore("&7Worth &e" + sellable.getWorth() + "⛁")
                .enchant(Enchantment.LUCK, 1)
                .flag(ItemFlag.HIDE_ENCHANTS)
                .build()
        );
        nbtDrop.setString("sell_id", sellable.getId());
        nbtDrop.setInteger("worth", sellable.getWorth());
        ItemStack item = nbtDrop.getItem();
        HashMap<Integer, ItemStack> left = p.getInventory().addItem(item);
        e.getBlock().getWorld().spawn(e.getBlock().getLocation(), ExperienceOrb.class).setExperience(2);
        if (!left.isEmpty()) {
            PlayerUtil.sendTitle(p, EnumWrappers.TitleAction.TITLE, "&cInventory Full!", 0, 2000, 1000);
            p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1f, 2f);
        } else {
            p.playSound(p.getLocation(), Sound.LAVA_POP, 0.5f, 2f);
            new OreMineAnimation(e.getBlock().getLocation()).start();
        }
        notReplaced.put(e.getBlock(), e.getBlock().getType());
        e.getBlock().setType(Material.BEDROCK);
        int addExp = ore.getDrop().getWorth();
        Core.i().getUserManager().getUser(p.getUniqueId()).ifPresent(user -> {
            user.getData().put("blocksMined", user.getData().get("blocksMined", 0) + 1);
            user.addExp(addExp);
            long exp = user.getExp();
            long leftOver = LevelMath.getLeftOverExp(exp);
            PlayerUtil.sendActionBar(p, "&7+ &d" + addExp + " Exp &8(&d" +
                    format.format(leftOver) + "&7/&d" + format.format(LevelMath.BASE) + "&8)");
        });
        Bukkit.getScheduler().runTaskLater(Core.i(), ()-> {
            e.getBlock().setType(ore.random());
            notReplaced.remove(e.getBlock());
        }, ore.getTicks());
    }
}
