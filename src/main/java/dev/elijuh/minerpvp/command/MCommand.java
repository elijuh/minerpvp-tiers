package dev.elijuh.minerpvp.command;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.List;

@Getter
public abstract class MCommand extends Command {

    private final String name, permission;

    public MCommand(String name, List<String> aliases, String permission) {
        super(name);
        super.setAliases(aliases);

        this.name = name;
        this.permission = permission;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            onConsole(sender, args);
            return false;
        }

        Player p = (Player) sender;

        if (permission != null && !p.hasPermission(permission)) {
            p.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return false;
        }

        try {
            onExecute(p, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (getPermission() != null && !sender.hasPermission(getPermission())) {
                return ImmutableList.of();
            }

            List<String> tabCompletion = onTabComplete(p, args);
            if (tabCompletion == null) {
                List<String> list = Lists.newArrayList();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (StringUtil.startsWithIgnoreCase(player.getName(), args[args.length - 1]) && p.canSee(player)) {
                        list.add(player.getName());
                    }
                }
                return list;
            }
            return tabCompletion;

        } else {
            return ImmutableList.of();
        }
    }

    @SuppressWarnings("unused")
    public void onConsole(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.RED + "You must be a player to execute this command.");
    }

    public abstract List<String> onTabComplete(Player p, String[] args);

    public abstract void onExecute(Player p, String[] args);
}
