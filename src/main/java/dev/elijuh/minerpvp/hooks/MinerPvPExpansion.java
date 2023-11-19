package dev.elijuh.minerpvp.hooks;

import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.user.User;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MinerPvPExpansion extends PlaceholderExpansion {
    private static MinerPvPExpansion instance;

    public MinerPvPExpansion() {
        instance = this;
    }

    public static void unload() {
        if (instance != null) {
            instance.unregister();
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return "minerpvp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "elijuh";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        User user = Core.i().getUserManager().getUsers().get(player.getUniqueId());
        if (user == null) return null;
        if (params.equalsIgnoreCase("coins")) {
            return Long.toString(user.getData().get("coins", 0L));
        }
        if (params.equalsIgnoreCase("blocks_mined")) {
            return Integer.toString(user.getData().get("blocksMined", 0));
        }
        return null;
    }
}
