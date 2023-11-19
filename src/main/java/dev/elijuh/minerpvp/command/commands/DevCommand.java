package dev.elijuh.minerpvp.command.commands;

import com.google.common.collect.ImmutableList;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.animations.KillEffect;
import dev.elijuh.minerpvp.command.MCommand;
import dev.elijuh.minerpvp.items.TierCategory;
import dev.elijuh.minerpvp.items.TieredItem;
import dev.elijuh.minerpvp.items.menu.ItemUpgradeMenu;
import dev.elijuh.minerpvp.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DevCommand extends MCommand {

    public DevCommand() {
        super("dev", ImmutableList.of(), "minerpvp.dev");
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("allitems")) {
            TierCategory category;
            try {
                category = TierCategory.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                p.sendMessage(Text.color("&cInvalid category: " + args[1]));
                return;
            }
            List<TieredItem> items = Core.i().getItemManager().getItems().values().stream().sorted(Comparator.comparingInt(TieredItem::getTier)).collect(Collectors.toList());
            for (TieredItem item : items) {
                p.getInventory().addItem(item.getItem());
            }
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
            p.sendMessage(Text.color("&aYou have been given all tiered items of category: &e" + category.getDisplay()));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("upgrade")) {
            Core.i().getUserManager().getUser(p.getUniqueId()).ifPresent(user ->
                    p.openInventory(new ItemUpgradeMenu(user).getInventory())
            );
        } else if (args.length == 2 && args[0].equalsIgnoreCase("setloc")) {
            Core.i().getStorage().setLocation(args[1], p.getLocation());
            Core.i().getStorage().save();
        } else if (args.length == 2 && args[0].equalsIgnoreCase("killeffect")) {
            KillEffect.apply(args[1], p, p);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("ride")) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target != null) {
                if (target == p) {
                    p.sendMessage(Text.color("&cYou can't ride yourself."));
                    return;
                }
                Bat dummy = target.getWorld().spawn(target.getLocation(), Bat.class);
                //dummy.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 0x7fffffff, 0, false, false));
                target.setPassenger(dummy);
                dummy.setPassenger(p);
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("addexp")) {
            long exp;
            try {
                exp = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(Text.color("&c") + "Invalid number: " + args[1]);
                return;
            }
            if (exp < 0) {
                p.sendMessage(Text.color("&c") + "exp can't be less than 0.");
            } else {
                Core.i().getUserManager().getUser(p.getUniqueId()).ifPresent(user -> user.addExp(exp));
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("setexp")) {
            long exp;
            try {
                exp = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(Text.color("&c") + "Invalid number: " + args[1]);
                return;
            }
            Core.i().getUserManager().getUser(p.getUniqueId()).ifPresent(user -> user.getData().put("exp", exp));
        }
    }
}
