package dev.elijuh.minerpvp.command.commands;

import com.google.common.collect.ImmutableList;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.command.MCommand;
import dev.elijuh.minerpvp.items.TieredItem;
import dev.elijuh.minerpvp.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveTieredItemCommand extends MCommand {
    public GiveTieredItemCommand() {
        super("givetiereditem", ImmutableList.of(), "minerpvp.givetiereditem");
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return null;
    }

    @Override
    public void onExecute(Player p, String[] args) {
        onConsole(p, args);
    }

    @Override
    public void onConsole(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Text.color("&cUsage: /givetiereditem <player> <id>"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(Text.color("&cPlayer not online."));
            return;
        }
        TieredItem item = Core.i().getItemManager().getItem(args[1].toUpperCase());
        if (item != null) {
            target.getInventory().addItem(item.getItem()).forEach((i, itemStack) -> target.getWorld().dropItem(target.getLocation(), itemStack));
            sender.sendMessage(Text.color("&aGiven 1 &e" + item.getId() + " &ato &e" + target.getName() + "&a."));
        } else {
            sender.sendMessage(Text.color("&cInvalid item: " + args[1]));
        }
    }
}
