package dev.elijuh.minerpvp.items.vault;

import dev.elijuh.minerpvp.util.feature.Menu;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class PlayerVaultMenu extends Menu {
    private final PlayerVault vault;
    private final Inventory inv;

    public PlayerVaultMenu(PlayerVault vault) {
        this.vault = vault;
        this.inv = Bukkit.createInventory(this, vault.getSize(), "Vault #" + vault.getId());
        for (int i = 0; i < vault.getContents().length; i++) {
            inv.setItem(i, vault.getContents()[i]);
        }
    }

    @Override
    public void handle(InventoryClickEvent e) {
        //blank
    }

    @Override
    public void handle(InventoryDragEvent e) {
        //blank
    }

    @Override
    public void handle(InventoryCloseEvent e) {
        for (int i = 0; i < vault.getContents().length; i++) {
            vault.getContents()[i] = inv.getItem(i);
        }
        vault.save();
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
