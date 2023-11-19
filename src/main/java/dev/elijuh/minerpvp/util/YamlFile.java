package dev.elijuh.minerpvp.util;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class YamlFile {
    private final YamlConfiguration config = new YamlConfiguration();
    private final File file;

    public YamlFile(File file) {
        this.file = file;
        create();
        reload();
    }

    public void setLocation(String path, Location location) {
        String s = location.getWorld().getName() + ","
                + location.getX() + ","
                + location.getY() + ","
                + location.getZ() + ","
                + location.getYaw() + ","
                + location.getPitch();
        set(path, s);
    }

    public Location getLocation(String path) {
        String[] s = config.getString(path).split(",");
        return new Location(
                Bukkit.getWorld(s[0]),
                Double.parseDouble(s[1]),
                Double.parseDouble(s[2]),
                Double.parseDouble(s[3]),
                Float.parseFloat(s[4]),
                Float.parseFloat(s[5])
        );
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path, T def) {
        return (T) config.get(path, def);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) config.get(path);
    }

    public void create() {
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                Bukkit.getLogger().warning("Failed to create directories: " + file.getPath());
            }
        }
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    Bukkit.getLogger().warning("Failed to create yml file: " + file.getPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reload() {
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDefault(String path, Object value) {
        if (!config.options().copyDefaults()) {
            config.options().copyDefaults(true);
        }
        config.addDefault(path, value);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }
}