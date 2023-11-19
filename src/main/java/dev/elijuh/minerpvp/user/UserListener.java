package dev.elijuh.minerpvp.user;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.animations.KillEffect;
import dev.elijuh.minerpvp.hooks.LuckPermsHook;
import dev.elijuh.minerpvp.items.backup.InventoryBackup;
import dev.elijuh.minerpvp.util.DocumentUtil;
import dev.elijuh.minerpvp.util.Text;
import dev.elijuh.minerpvp.util.timers.CombatTimer;
import dev.elijuh.minerpvp.util.timers.SpawnTimer;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.*;

public class UserListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent e) {
        e.getPlayer().setLevel(0);
        e.getPlayer().setExp(0);
    }

    @EventHandler
    public void on(PlayerDeathEvent e) {
        e.setDroppedExp(0);
        e.setKeepInventory(true);
        e.setKeepLevel(true);
        e.setDeathMessage(null);
        Bukkit.getScheduler().runTask(Core.i(), ()-> e.getEntity().spigot().respawn());
        onDeath(e.getEntity(), true);
    }

    private void onDeath(Player p, boolean respawning) {
        if (p.getTicksLived() < 2) {
            return;
        }
        Optional<User> op = Core.i().getUserManager().getUser(p.getUniqueId());
        if (!op.isPresent()) {
            if (!respawning) {
                onRespawn(p);
            }
            return;
        }
        User killed = op.get();

        new InventoryBackup(p).save();

        Optional<User> killerOp = lastAttacker.get(p.getUniqueId()) == null ? Optional.empty() : Core.i().getUserManager().getUser(lastAttacker.get(p.getUniqueId()).getUniqueId());

        if (killerOp.isPresent() && Core.i().getConfig().getStringList("stat-affecting-worlds").contains(p.getWorld().getName())) {
            User killer = killerOp.get();
            Player killerPlayer = lastAttacker.get(p.getUniqueId());
            //todo detect alt farming

            //todo levels

            KillEffect.apply(killer.getData().getString("killEffect"), p, killerPlayer);

            int killedStreak = DocumentUtil.getDotNotation(killed.getData(), "stats.killstreak", 0);

            DocumentUtil.setDotNotation(killed.getData(), "stats.deaths",
                    DocumentUtil.getDotNotation(killed.getData(), "stats.deaths", 0) + 1);
            DocumentUtil.setDotNotation(killed.getData(), "stats.killstreak", 0);

            DocumentUtil.setDotNotation(killer.getData(), "stats.kills",
                    DocumentUtil.getDotNotation(killer.getData(), "stats.kills", 0) + 1);

            DocumentUtil.setDotNotation(killer.getData(), "stats.killstreak",
                    DocumentUtil.getDotNotation(killer.getData(), "stats.killstreak", 0) + 1);

            if (killedStreak > 50) {
                Bukkit.broadcastMessage(Text.color("&6&lMinerPvP &8⏐ &c" + killerPlayer.getName() +
                        " &7has broken the killstreak of &c" + p.getName() + "! &8(&6&l" + killedStreak + "&8)"));
            }

            int streak = DocumentUtil.getDotNotation(killer.getData(), "stats.killstreak", 0);

            if (streak > 25 && streak % 25 == 0) {
                Bukkit.broadcastMessage(Text.color("&6&lMinerPvP &8⏐ &c" + killerPlayer.getName() +
                        " &7has reached a crazy killstreak! &8(&6&l" + streak + "&8)"));
            }
        }

        if (!respawning) {
            onRespawn(p);
        }
    }

    private void onRespawn(Player p) {
        p.teleport(SpawnTimer.getSpawn());
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(40);
        Bukkit.getScheduler().runTask(Core.i(), ()-> p.setFireTicks(0));
        p.setFallDistance(0);
        p.setTicksLived(1);
        new ArrayList<>(p.getActivePotionEffects()).forEach(effect -> p.removePotionEffect(effect.getType()));
        ((CraftPlayer) p).getHandle().getDataWatcher().watch(9, (byte) 0);
        CombatTimer.getTimers().remove(p.getUniqueId());
    }

    @EventHandler
    public void on(PlayerRespawnEvent e) {
        Bukkit.getScheduler().runTask(Core.i(), ()-> onRespawn(e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(PlayerMoveEvent e) {
        Location to = e.getTo().getBlock().getLocation();
        Location from = e.getFrom().getBlock().getLocation();
        if (to.distance(from) > 0) {
            SpawnTimer timer = SpawnTimer.getTimers().get(e.getPlayer().getUniqueId());
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(PlayerTeleportEvent e) {
        SpawnTimer timer = SpawnTimer.getTimers().get(e.getPlayer().getUniqueId());
        if (timer != null) {
            timer.cancel();
        }
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && p.getGameMode() != GameMode.CREATIVE) {
            Block block = e.getClickedBlock();
            if ((block.getType() == Material.TRAP_DOOR || (e.getItem() != null && e.getItem().getType() == Material.ANVIL))) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on(PrepareItemCraftEvent e) {
        e.getInventory().setResult(null);
    }

    @EventHandler
    public void on(PlayerExpChangeEvent e) {
        e.setAmount(0);
    }

    @EventHandler
    public void on(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void on(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player && e.getDismounted().hasMetadata("riding-dummy")) {
            e.getDismounted().remove();
        }
    }

    @EventHandler
    public void on(VotifierEvent e) {
        Vote vote = e.getVote();
        Bukkit.broadcastMessage(Text.color("&a&lVote &8» &7" + vote.getUsername() + " has voted on &a" + vote.getServiceName() + "&7!"));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cr give to " + vote.getUsername() + " vote 1");
    }

    @EventHandler
    public void on(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(AsyncPlayerChatEvent e) {
        Core.i().getUserManager().getUser(e.getPlayer().getUniqueId()).ifPresent(user ->
                e.setFormat(user.getLevelPrefix() + Text.color("&r ") + e.getFormat())
        );
    }

    @EventHandler
    public void on(FoodLevelChangeEvent e) {
        e.setFoodLevel(40);
    }

    @Getter
    private static final Map<UUID, Player> lastAttacker = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(EntityDamageEvent e) {
        if (e.getEntity().hasMetadata("riding-dummy") || e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        } else if (e.getEntity() instanceof Player) {
            if (((Player) e.getEntity()).getHealth() - e.getFinalDamage() <= 0) {
                e.setCancelled(true);
                onDeath((Player) e.getEntity(), false);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Integer combat = CombatTimer.getTimers().get(p.getUniqueId());
        if (combat != null) {
            onDeath(p, false);
            String prefix = LuckPermsHook.getUser(p).getCachedData().getMetaData().getPrefix();
            prefix = prefix == null ? "&7" : ChatColor.getLastColors(Text.color(prefix));
            Bukkit.broadcastMessage(Text.color(prefix + p.getName() + " &7has logged out during combat."));
        }
        lastAttacker.remove(p.getUniqueId());
    }
}
