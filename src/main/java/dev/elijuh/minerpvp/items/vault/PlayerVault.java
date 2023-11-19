package dev.elijuh.minerpvp.items.vault;

import dev.elijuh.minerpvp.Core;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.kitteh.cardboardbox.CardboardBox;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

public class PlayerVault {
    private static final File folder = new File(Core.i().getDataFolder(), "vaults");
    static {
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Getter
    private final int id, size;
    private final UUID uuid;
    @Getter
    private final ItemStack[] contents;

    public PlayerVault(int id, int rows, UUID uuid) {
        this.id = id;
        this.size = rows * 9;
        this.uuid = uuid;
        contents = new ItemStack[size];

        File file = new File(folder, uuid.toString().replace("-", "") + "-" + id + ".vault");
        if (!file.exists()) return;

        try (InputStream stream = Files.newInputStream(file.toPath());
             DataInputStream in = new DataInputStream(stream)) {
            for (int i = 0; i < size; i++) {
                if (in.available() == 0) break;
                int len = in.readInt();
                byte[] itemBytes = new byte[len];
                in.readFully(itemBytes);
                contents[i] = CardboardBox.deserializeItem(itemBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exists() {
        return new File(folder, uuid.toString().replace("-", "") + "-" + id + ".vault").exists();
    }

    public void save() {
        File file = new File(folder, uuid.toString().replace("-", "") + "-" + id + ".vault");
        try (OutputStream stream = Files.newOutputStream(file.toPath());
             DataOutputStream out = new DataOutputStream(stream)) {
            for (ItemStack content : contents) {
                byte[] item = CardboardBox.serializeItem(content);
                out.writeInt(item.length);
                out.write(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Inventory getInventory() {
        return new PlayerVaultMenu(this).getInventory();
    }
}
