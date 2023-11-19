package dev.elijuh.minerpvp.items.backup;

import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.util.YamlFile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kitteh.cardboardbox.CardboardBox;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class InventoryBackup {
    @Getter
    private static final File folder = new File(Core.i().getDataFolder(), "inv-backups");
    private static final YamlFile dataFile = new YamlFile(new File(Core.i().getDataFolder(), "backup-data.yml"));
    static {
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Getter
    private final int id;
    private final UUID uuid;
    private final ItemStack[] contents = new ItemStack[40];

    public InventoryBackup(Player p) {
        this.id = dataFile.get("last-id", -1) + 1;
        this.uuid = p.getUniqueId();
        System.arraycopy(p.getInventory().getContents(), 0, contents, 0, 36);
        System.arraycopy(p.getInventory().getArmorContents(), 0, contents, 36, 4);
        dataFile.set("last-id", this.id);
        dataFile.save();
    }

    private InventoryBackup(int id, File file) {
        this.id = id;

        try (InputStream stream = Files.newInputStream(file.toPath());
             DataInputStream in = new DataInputStream(stream)) {
            this.uuid = new UUID(in.readLong(), in.readLong());
            for (int i = 0; i < contents.length; i++) {
                int len = in.readInt();
                byte[] itemBytes = new byte[len];
                in.readFully(itemBytes);
                contents[i] = CardboardBox.deserializeItem(itemBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InventoryBackup get(int id) {
        File file = getFile(id);
        if (file.exists()) {
            return new InventoryBackup(id, file);
        }
        return null;
    }

    public static List<Integer> getIds(UUID uuid) {
        List<Integer> ids = dataFile.getConfig().getIntegerList(uuid.toString());
        return ids == null ? new ArrayList<>() : ids;
    }

    public static File getFile(int id) {
        return new File(folder, id + ".backup");
    }

    public void save() {
        File file = getFile(id);
        try (OutputStream stream = Files.newOutputStream(file.toPath());
             DataOutputStream out = new DataOutputStream(stream)) {
            out.writeLong(uuid.getMostSignificantBits());
            out.writeLong(uuid.getLeastSignificantBits());
            for (ItemStack content : contents) {
                byte[] item = CardboardBox.serializeItem(content);
                out.writeInt(item.length);
                out.write(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Integer> ids = new HashSet<>(getIds(uuid));
        ids.add(id);
        //purge oldest entries
        while (ids.size() > 20) {
            ids.stream().mapToInt(i -> i).min().ifPresent(oldestId -> {
                ids.remove(oldestId);
                delete(oldestId);
            });
        }

        dataFile.set(uuid.toString(), new ArrayList<>(ids));
        dataFile.save();
    }

    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 45, "Backup: #" + id);
        for (int i = 0; i < contents.length; i++) {
            inv.setItem(i, contents[i]);
        }
        return inv;
    }

    public void apply(Player p) {
        ItemStack[] newContents = new ItemStack[36];
        ItemStack[] newArmorContents = new ItemStack[4];
        System.arraycopy(contents, 0, newContents, 0, 36);
        System.arraycopy(contents, 36, newArmorContents, 0, 4);
        p.getInventory().setContents(newContents);
        p.getInventory().setArmorContents(newArmorContents);
    }

    private static void delete(int id) {
        File file = getFile(id);
        if (file.exists()) {
            file.delete();
        }
    }
}
