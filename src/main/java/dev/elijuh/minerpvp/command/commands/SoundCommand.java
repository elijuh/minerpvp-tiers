package dev.elijuh.minerpvp.command.commands;

import com.google.common.collect.ImmutableList;
import dev.elijuh.minerpvp.command.MCommand;
import dev.elijuh.minerpvp.util.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SoundCommand extends MCommand {
    public SoundCommand() {
        super("sound", ImmutableList.of(), "minerpvp.admin");
        setUsage(Text.color("&cUsage: /sound <sound> <pitch>"));
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        if (args.length == 1) {
            return Arrays.stream(Sound.values())
                    .map(Enum::name)
                    .filter(s -> StringUtil.startsWithIgnoreCase(s, args[0]))
                    .collect(Collectors.toList());
        }
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        if (args.length == 1) args = new String[]{args[0], "1"};
        if (args.length == 2) {
            Sound sound;
            float pitch;
            try {
                sound = Sound.valueOf(args[0]);
                pitch = Float.parseFloat(args[1]);
            } catch (IllegalArgumentException e) {
                p.sendMessage(Text.color("&cInvalid arguments: &7" + args[0] + "&c, &7" + args[1]));
                return;
            }
            p.playSound(p.getLocation(), sound, 1f, pitch);
        } else {
            p.sendMessage(getUsage());
        }
    }
}
