package dev.elijuh.minerpvp.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Sound;

@UtilityClass
public class NoteUtil {

    public void playSound(Location location, Sound sound, int note) {
        location.getWorld().playSound(location, sound, 1f, (float) Math.pow(2, (-12 + note) / 12f));
    }
}
