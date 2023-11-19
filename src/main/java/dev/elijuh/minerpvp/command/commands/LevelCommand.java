package dev.elijuh.minerpvp.command.commands;

import com.google.common.collect.ImmutableList;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.command.MCommand;
import dev.elijuh.minerpvp.user.levels.menu.LevelMenu;
import org.bukkit.entity.Player;

import java.util.List;

public class LevelCommand extends MCommand {
    public LevelCommand() {
        super("level", ImmutableList.of("levels"), null);
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        Core.i().getUserManager().getUser(p.getUniqueId()).ifPresent(user -> p.openInventory(new LevelMenu(user, p).getInventory()));
    }
}
