package dev.elijuh.minerpvp.util.timers;

import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.util.Text;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnTimer extends BukkitRunnable {
    @Getter
    private static final Map<UUID, SpawnTimer> timers = new HashMap<>();
    private static final int TIME = 5;

    @Setter
    @Getter
    private static Location spawn = Core.i().getStorage().getLocation("spawn");

    private final Player p;
    private int count = 0;

    public SpawnTimer(Player p) {
        this.p = p;
        p.sendMessage(Text.color("&7Teleporting to spawn in &a" + TIME + "&7s (&cDon't move!&7)"));
        timers.put(p.getUniqueId(), this);
    }

    @Override
    public void run() {
        if (++count >= TIME) {
            if (p.isOnline()) {
                p.teleport(spawn);
                p.sendMessage(Text.color("&aYou have been teleported to spawn."));
            }
            super.cancel();
            timers.remove(p.getUniqueId());
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        p.sendMessage(Text.color("&8[&4&l!&8] &7Teleport cancelled."));
        timers.remove(p.getUniqueId());
    }
}
