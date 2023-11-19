package dev.elijuh.minerpvp.util.timers;

import dev.elijuh.minerpvp.user.UserListener;
import dev.elijuh.minerpvp.util.Text;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PearlTimer implements Listener {
    @Getter
    private static final Map<UUID, Integer> timers = new HashMap<>();
    private static final int TIME = 15;

    public static void update() {
        Set<UUID> toRemove = new HashSet<>();
        timers.forEach((uuid, timeLeft) -> {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) {
                toRemove.add(uuid);
                return;
            }
            timers.put(uuid, --timeLeft);
            if (timeLeft < 1) {
                toRemove.add(uuid);
                p.sendMessage(Text.color("&aYour ender pearl is no longer on cooldown."));
            }
        });
        toRemove.forEach(uuid -> {
            timers.remove(uuid);
            UserListener.getLastAttacker().remove(uuid);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getItemInHand();
        if (item == null || item.getType() != Material.ENDER_PEARL || !e.getAction().name().startsWith("RIGHT")) return;

        Integer timer = timers.get(e.getPlayer().getUniqueId());
        if (timer != null) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Text.color("&7You are still on ender pearl cooldown for &c" + timer + "s"));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof EnderPearl) || !(e.getEntity().getShooter() instanceof Player)) return;

        timers.put(((Player) e.getEntity().getShooter()).getUniqueId(), TIME + 1);
    }
}
