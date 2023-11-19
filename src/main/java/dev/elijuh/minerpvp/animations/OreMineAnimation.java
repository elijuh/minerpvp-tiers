package dev.elijuh.minerpvp.animations;

import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.util.color.HSVColor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;

@RequiredArgsConstructor
public class OreMineAnimation extends BukkitRunnable {
    private final HSVColor color = HSVColor.fromRGB(255, 0, 0);
    private final Block b;
    private int tick;

    public OreMineAnimation(Location loc) {
        b = loc.getBlock();
    }

    public void start() {
        runTaskTimerAsynchronously(Core.i(), 1L, 1L);
    }

    private static final BlockFace[] faces = {
            BlockFace.UP, BlockFace.DOWN,
            BlockFace.NORTH, BlockFace.EAST,
            BlockFace.SOUTH, BlockFace.WEST
    };

    @Override
    public void run() {
        tick++;
        color.progressHue(0.05f);
        double sin = Math.sin(Math.PI / 10 * tick) * 0.25;
        double cos = Math.cos(Math.PI / 10 * tick) * 0.25;

        for (BlockFace face : faces) {
            if (b.getRelative(face).getType() == Material.AIR) {
                Location add = getAddition(face, sin, cos).toLocation(b.getWorld());
                new ParticleBuilder(ParticleEffect.REDSTONE).setLocation(b.getLocation().add(add)).setColor(new Color(color.toRGB())).display();
            }
        }


        if (tick == 20) cancel();
    }

    private Vector getAddition(BlockFace face, double sin, double cos) {
        if (face == BlockFace.UP) {
            return new Vector(0.5 + sin, 1.1, 0.5 + cos);
        }
        if (face == BlockFace.DOWN) {
            return new Vector(0.5 + sin, -0.1, 0.5 + cos);
        }
        if (face == BlockFace.NORTH) {
            return new Vector(0.5 + sin, 0.5 + cos, -0.1);
        }
        if (face == BlockFace.EAST) {
            return new Vector(1.1, 0.5 + cos, 0.5 + sin);
        }
        if (face == BlockFace.SOUTH) {
            return new Vector(0.5 + sin, 0.5 + cos, 1.1);
        }
        if (face == BlockFace.WEST) {
            return new Vector(-0.1, 0.5 + cos, 0.5 + sin);
        }
        return new Vector(0, 0, 0);
    }
}
