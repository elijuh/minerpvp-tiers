package dev.elijuh.minerpvp.util.feature;

import dev.elijuh.minerpvp.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Menu implements InventoryHolder {
    public static final ItemStack FILLER = ItemBuilder.create(Material.STAINED_GLASS_PANE).dura(15).name(" ").build();

    public static void init() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void on(InventoryClickEvent e) {
                InventoryHolder holder = e.getInventory().getHolder();
                if (holder instanceof Menu) {
                    ((Menu) holder).handle(e);
                }
            }

            @EventHandler
            public void on(InventoryCloseEvent e) {
                InventoryHolder holder = e.getInventory().getHolder();
                if (holder instanceof Menu) {
                    ((Menu) holder).handle(e);
                }
            }

            @EventHandler
            public void on(InventoryDragEvent e) {
                InventoryHolder holder = e.getInventory().getHolder();
                if (holder instanceof Menu) {
                    ((Menu) holder).handle(e);
                }
            }
        }, JavaPlugin.getProvidingPlugin(Menu.class));
    }

    public void fill() {
        for (int i = 0; i < getInventory().getSize(); i++) {
            getInventory().setItem(i, FILLER);
        }
    }

    public abstract void handle(InventoryClickEvent e);

    public void handle(InventoryCloseEvent e) {

    }

    public void handle(InventoryDragEvent e) {
        e.setCancelled(true);
    }
}
