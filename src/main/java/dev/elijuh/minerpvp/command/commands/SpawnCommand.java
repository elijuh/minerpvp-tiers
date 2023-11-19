package dev.elijuh.minerpvp.command.commands;

import com.google.common.collect.ImmutableList;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.command.MCommand;
import dev.elijuh.minerpvp.util.Text;
import dev.elijuh.minerpvp.util.timers.CombatTimer;
import dev.elijuh.minerpvp.util.timers.SpawnTimer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnCommand extends MCommand {

    public SpawnCommand() {
        super("spawn", ImmutableList.of(), null);
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return null;
    }

    @Override
    public void onExecute(Player p, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("set") && p.hasPermission("core.setspawn")) {
            Core.i().getStorage().setLocation("spawn", p.getLocation());
            Core.i().getStorage().save();
            SpawnTimer.setSpawn(p.getLocation());
            p.sendMessage(Text.color("&aSpawn has been set to your location."));
            return;
        } else if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
            p.teleport(SpawnTimer.getSpawn());
            p.sendMessage(Text.color("&aYou have been teleported to spawn."));
            return;
        }

        Integer combat = CombatTimer.getTimers().get(p.getUniqueId());
        if (combat != null) {
            p.sendMessage(Text.color("&c") + "You cannot use /spawn in combat.");
        } else {
            new SpawnTimer(p).runTaskTimer(Core.i(), 20L, 20L);
        }
    }
}
