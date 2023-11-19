package dev.elijuh.minerpvp.animations;

import com.google.common.collect.ImmutableMap;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.util.ItemBuilder;
import dev.elijuh.minerpvp.util.NoteUtil;
import dev.elijuh.minerpvp.util.color.HSVColor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@Getter
@RequiredArgsConstructor
public enum KillEffect {
    NONE((killed, killer) -> {}),
    LIGHTNING((killed, killer) -> killed.getLocation().getWorld().strikeLightningEffect(killed.getLocation())),
    BLOOD_EXPLOSION((killed, killer) -> {
        Location loc = killed.getLocation();
        for (int i = 0; i < 10; i++) {
            loc.getWorld().playSound(loc, Sound.DIG_STONE, 2f, 1f);
        }
        loc.getWorld().playEffect(loc.clone().add(0, 1, 0), Effect.STEP_SOUND, 152);
    }),
    WATER_EXPLOSION((killed, killer) -> {
        Location loc = killed.getLocation();
        loc.getWorld().playSound(loc, Sound.SPLASH, 1f, 1f);
        loc.getWorld().playEffect(loc.clone().add(0, 1, 0), Effect.STEP_SOUND, 9);
    }),
    HEART_SPIRAL((killed, killer) -> new BukkitRunnable() {
        private final Location loc = killed.getLocation();
        private double theta = 0, y = 1.0;
        private int ticks;
        private final Map<Integer, Integer> notes = ImmutableMap.of(
                0, 6,
                4, 8,
                8, 10,
                12, 11,
                16, 13
        );

        @Override
        public void run() {
            if (ticks < 20) {
                double circle = Math.PI * 2;
                double x = y / 3 * Math.cos(theta % circle);
                double z = y / 3 * Math.sin(theta % circle);
                new ParticleBuilder(ParticleEffect.HEART).setLocation(loc.clone().add(x, y, z)).display();
                y += 0.1;
                theta += circle / 10.0;

                Integer note = notes.get(ticks++);
                if (note != null) {
                    NoteUtil.playSound(loc, Sound.NOTE_PLING, note);
                }
            }
        }
    }.runTaskTimerAsynchronously(Core.i(), 0L, 1L)),
    HEAD_ROCKET((killed, killer) -> {
        Location loc = killed.getLocation();
        ArmorStand head = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        head.setGravity(false);
        head.setVisible(false);
        ItemBuilder helmet = ItemBuilder.create(Material.SKULL_ITEM).dura(3);
        ((SkullMeta) helmet.getMeta()).setOwner(killed.getName());
        head.setHelmet(helmet.build());

        getStands().add(head);

        new BukkitRunnable() {
            private int ticks;
            private final HSVColor color = HSVColor.fromRGB(255, 0, 0);

            @Override
            public void run() {
                if (ticks == 0) {
                    head.getWorld().playSound(head.getLocation(), Sound.ZOMBIE_UNFECT, 1f, 0.5f);
                }
                if (ticks++ < 30) {
                    color.progressHue(0.05f);
                    head.teleport(head.getLocation().add(0, 0.2, 0));
                    new ParticleBuilder(ParticleEffect.REDSTONE, head.getLocation().add(0, 1, 0))
                            .setColor(new java.awt.Color(color.toRGB()))
                            .display();
                } else {
                    cancel();
                    for (int i = 0; i < 10; i++) {
                        head.getWorld().playSound(head.getLocation().add(0, 1, 0), Sound.ZOMBIE_UNFECT, 1f, 2f);
                    }
                    head.getWorld().playEffect(head.getLocation().add(0, 1, 0), Effect.STEP_SOUND, 152);
                    head.remove();
                    getStands().remove(head);
                }
            }
        }.runTaskTimer(Core.i(), 0L, 1L);
    }),
    FINAL_SMASH((killed, killer) -> {
        Location loc = killed.getLocation();
        ArmorStand body = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        body.setGravity(false);
        body.setVisible(false);
        ItemBuilder helmet = ItemBuilder.create(Material.SKULL_ITEM).dura(3);
        ((SkullMeta) helmet.getMeta()).setOwner(killed.getName());
        body.setHelmet(helmet.build());
        body.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        body.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        body.setBoots(new ItemStack(Material.LEATHER_BOOTS));

        getStands().add(body);

        Vector direction = killer.getLocation().getDirection().setY(0).multiply(0.5);

        new BukkitRunnable() {
            private int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    body.getWorld().playSound(body.getLocation(), Sound.BAT_TAKEOFF, 1f, 1f);
                }
                if (ticks++ < 30) {
                    Location l = body.getLocation().add(direction);
                    if (l.getYaw() > 170) {
                        l.setYaw(l.getYaw() - 350);
                    } else {
                        l.setYaw(l.getYaw() + 10);
                    }
                    body.teleport(l);
                    ParticleEffect.CLOUD.display(body.getLocation().add(0, 1, 0));
                } else {
                    cancel();
                    Firework fw = (Firework) body.getWorld().spawnEntity(body.getLocation().add(0, 1, 0), EntityType.FIREWORK);
                    FireworkMeta meta = fw.getFireworkMeta();
                    meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA, Color.BLUE).withFlicker().build());
                    fw.setFireworkMeta(meta);
                    Location location = body.getLocation();
                    Bukkit.getScheduler().runTaskLater(Core.i(), ()-> {
                        fw.detonate();
                        location.getWorld().playSound(location, Sound.BLAZE_HIT, 1f, 2f);
                    }, 2L);
                    body.remove();
                    getStands().remove(body);
                }
            }
        }.runTaskTimer(Core.i(), 0L, 1L);
    });

    @Getter
    private static final Set<ArmorStand> stands = new HashSet<>();

    static {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void on(PlayerInteractEntityEvent e) {
                Entity entity = e.getRightClicked();
                if (entity instanceof ArmorStand && stands.contains(entity)) {
                    e.setCancelled(true);
                }
            }

            @EventHandler
            public void on(EntityDamageEvent e) {
                Entity entity = e.getEntity();
                if (entity instanceof ArmorStand && stands.contains(entity)) {
                    e.setCancelled(true);
                }
            }
        }, JavaPlugin.getProvidingPlugin(KillEffect.class));
    }

    private final BiConsumer<Player, Player> consumer;

    public static void apply(String killEffect, Player killed, Player killer) {
        if (killEffect == null) return;
        try {
            KillEffect effect = KillEffect.valueOf(killEffect);
            effect.getConsumer().accept(killed, killer);
        } catch (IllegalArgumentException ignored) {
            Core.i().getLogger().warning("invalid kill effect for " + killer.getName() + ": " + killEffect);
        }
    }
}
