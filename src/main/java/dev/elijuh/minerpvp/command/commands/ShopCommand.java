package dev.elijuh.minerpvp.command.commands;

import com.google.common.collect.ImmutableList;
import dev.elijuh.minerpvp.command.MCommand;
import dev.elijuh.minerpvp.items.menu.ShopMenu;
import org.bukkit.entity.Player;

import java.util.List;

public class ShopCommand extends MCommand {
    public ShopCommand() {
        super("shop", ImmutableList.of(), null);
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        p.openInventory(new ShopMenu().getInventory());
    }
}
