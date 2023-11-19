package dev.elijuh.minerpvp.command.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import dev.elijuh.minerpvp.command.MCommand;
import dev.elijuh.minerpvp.items.backup.InventoryBackup;
import dev.elijuh.minerpvp.util.PlayerUtil;
import dev.elijuh.minerpvp.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class InventoryManagerCommand extends MCommand {

    public InventoryManagerCommand() {
        super("inventorymanager", ImmutableList.of("im", "invbackups"), "kitpvp.invbackups");
        setUsage(Text.color(
            "\n&7&m-------------------------------------------------" +
                "\n&r    &6&lInventory Backups &8⏐ &a&lCommands" +
                "\n \n&e/invbackups list <name> &8⏐ &7&oshows a list of a player's backups." +
                "\n&e/invbackups save <name> &8⏐ &7&osaves a player's current inv." +
                "\n&e/invbackups open <id> &8⏐ &7&oopen a container of a backup." +
                "\n&e/invbackups apply <id> [name] &8⏐ &7&oapply a backup to a player's current inventory." +
                "\n&e/invbackups purge <epoch-ms> &8⏐ &7&opurge all inventory entries before the specified timestamp." +
                "\n&7&m-------------------------------------------------"));
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage(getUsage());
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            if (!p.hasPermission("kitpvp.invbackups.list")) {
                p.sendMessage(Text.color("&cYou don't have permission to use this sub-command."));
                return;
            }
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target != null) {
                p.sendMessage(buildList(target.getUniqueId()));
            } else CompletableFuture.runAsync(() -> {
                GameProfile profile = PlayerUtil.getOfflineProfileWithoutMojang(args[1]);
                p.sendMessage(profile == null ? Text.color("&cPlayer doesn't exist.") : buildList(profile.getId()));
            });

        } else if (args[0].equalsIgnoreCase("apply")) {
            if (!p.hasPermission("kitpvp.invbackups.apply")) {
                p.sendMessage(Text.color("&cYou don't have permission to use this sub-command."));
                return;
            }
            int id;
            try {
                id = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(Text.color("&cInvalid integer: " + args[1]));
                return;
            }
            Player target = args.length > 2 ? Bukkit.getPlayerExact(args[2]) : p;
            if (target == null) {
                p.sendMessage(Text.color("&cCouldn't find online player: " + args[2]));
                return;
            }
            InventoryBackup backup = InventoryBackup.get(id);
            if (backup == null) {
                p.sendMessage(Text.color("&cBackup does not exist: " + id));
                return;
            }

            backup.apply(target);
        } else if (args[0].equalsIgnoreCase("save")) {
            if (!p.hasPermission("kitpvp.invbackups.save")) {
                p.sendMessage(Text.color("&cYou don't have permission to use this sub-command."));
                return;
            }
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target != null) {
                InventoryBackup backup = new InventoryBackup(target);
                backup.save();
                p.sendMessage(Text.color("&aCreated backup for: &e" + target.getName() + " &7(ID: &d" + backup.getId() + "&7)"));
            } else {
                p.sendMessage(Text.color("&cPlayer is not online."));
            }
        } else if (args[0].equalsIgnoreCase("open")) {
            if (!p.hasPermission("kitpvp.invbackups.open")) {
                p.sendMessage(Text.color("&cYou don't have permission to use this sub-command."));
                return;
            }
            int id;
            try {
                id = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(Text.color("&cInvalid integer: " + args[1]));
                return;
            }
            InventoryBackup backup = InventoryBackup.get(id);
            if (backup != null) {
                p.openInventory(backup.getInventory());
            } else {
                p.sendMessage(Text.color("&cBackup does not exist: " + id));
            }
        } else if (args[0].equalsIgnoreCase("purge")) {
            if (!p.hasPermission("kitpvp.invbackups.purge")) {
                p.sendMessage(Text.color("&cYou don't have permission to use this sub-command."));
                return;
            }
            long epochMs;
            try {
                epochMs = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(Text.color("&cInvalid long: " + args[1]));
                return;
            }
            p.sendMessage(Text.color("&6&lBackups &8┃ &7Attempting to purge all entries before &e"
                + epochMs + "&7...")
            );
            int removed = 0;
            File[] files = InventoryBackup.getFolder().listFiles();
            if (files == null) throw new IllegalStateException("files[] has returned null");
            for (File file : files) {
                if (file.lastModified() < epochMs) {
                    file.delete();
                    removed++;
                }
            }
            p.sendMessage(Text.color("&6&lBackups &8┃ &aSuccessfully purged &e&l"
                + NumberFormat.getInstance().format(removed) + "&r &aentries.")
            );
        } else {
            p.sendMessage(getUsage());
        }
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private String getDate(long lastModified) {
        if (lastModified > 0) {
            Date date = new Date(lastModified);
            return dateFormat.format(date) + " &8(&f" + dateFormat.getTimeZone().getDisplayName(
                dateFormat.getTimeZone().inDaylightTime(date), TimeZone.SHORT
            ) + "&8)";
        }
        return "N/A";
    }

    private String buildList(UUID uuid) {
        return Text.color("&6&lBackups &7for &a" + uuid + "\n&7Date Format: &6"
            + dateFormat.toPattern() + "\n&a&l» &d" + InventoryBackup.getIds(uuid).stream().map(id ->
                id + " &8┃ &7" + getDate(InventoryBackup.getFile(id).lastModified()))
            .collect(Collectors.joining("\n&a&l» &d")
            )
        );
    }
}
