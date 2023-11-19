package dev.elijuh.minerpvp.util.timers;

import dev.elijuh.minerpvp.user.UserListener;
import dev.elijuh.minerpvp.util.Text;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;

public class CombatTimer implements Listener {
    @Getter
    private static final Map<UUID, Integer> timers = new HashMap<>();
    private static final int TIME = 20;

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
                p.sendMessage(Text.color("&aYou are no longer in combat."));
            }
        });
        toRemove.forEach(uuid -> {
            timers.remove(uuid);
            UserListener.getLastAttacker().remove(uuid);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player ||
                (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player)) ||
                !(e.getEntity() instanceof Player)) return;
        Player hit = (Player) e.getEntity();
        Player damager = (Player) (e.getDamager() instanceof Player ? e.getDamager() : ((Projectile) e.getDamager()).getShooter());

        //todo find a better way to define spawn region
        if (hit.getLocation().getY() >= 150) {
            e.setCancelled(true);
            return;
        }
        if (hit.equals(damager)) return;

        if (!timers.containsKey(hit.getUniqueId())) {
            hit.sendMessage(Text.color("&7You are now in combat with &c" + damager.getName()));
        }
        if (!timers.containsKey(damager.getUniqueId())) {
            damager.sendMessage(Text.color("&7You are now in combat with &c" + hit.getName()));
        }
        timers.put(hit.getUniqueId(), TIME + 1);
        timers.put(damager.getUniqueId(), TIME + 1);

        UserListener.getLastAttacker().put(hit.getUniqueId(), damager);

        SpawnTimer spawnTimer = SpawnTimer.getTimers().get(hit.getUniqueId());
        if (spawnTimer != null) spawnTimer.cancel();

        spawnTimer = SpawnTimer.getTimers().get(damager.getUniqueId());
        if (spawnTimer != null) spawnTimer.cancel();
    }
}
