package dev.elijuh.minerpvp.hooks;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.platform.PlayerAdapter;
import org.bukkit.entity.Player;

import java.util.Optional;

public class LuckPermsHook {
    @Getter
    private static final LuckPerms luckPerms = LuckPermsProvider.get();
    @Getter
    private static final UserManager userManager = luckPerms.getUserManager();
    @Getter
    private static final PlayerAdapter<Player> adapter = luckPerms.getPlayerAdapter(Player.class);

    public static User getUser(Player p) {
        return adapter.getUser(p);
    }

    public static Optional<Group> getPrimaryGroup(Player p) {
        String name = adapter.getUser(p).getPrimaryGroup();
        Group group = luckPerms.getGroupManager().getGroup(name);
        return Optional.ofNullable(group);
    }
}
