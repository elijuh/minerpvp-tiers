package dev.elijuh.minerpvp.command.commands;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.command.MCommand;
import dev.elijuh.minerpvp.hooks.LuckPermsHook;
import dev.elijuh.minerpvp.items.vault.PlayerVault;
import dev.elijuh.minerpvp.util.Text;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerVaultCommand extends MCommand {
    private final Collation collation = Collation.builder().locale("en").collationStrength(CollationStrength.PRIMARY).build();
    private final Bson projections = Projections.include("_id");

    public PlayerVaultCommand() {
        super("playervault", ImmutableList.of("vault", "pv"), null);
        setUsage(Text.color("&cUsage: /pv [number]"));
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        CompletableFuture.runAsync(() -> {
            UUID uuid = p.getUniqueId();
            int id = 1;
            if (args.length == 1) {
                try {
                    id = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    p.sendMessage("Invalid id: " + args[0]);
                    return;
                }
            } else if (args.length == 2) {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null) {
                    Document data = Core.i().getUserManager().getCollection().find(Filters.eq("name", args[0])).collation(collation).projection(projections).first();
                    if (data != null) {
                        uuid = UUID.fromString(data.getString("_id"));
                    } else {
                        p.sendMessage(Text.color("&cPlayer doesn't exist."));
                        return;
                    }
                } else {
                    uuid = target.getUniqueId();
                }
                try {
                    id = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    p.sendMessage("Invalid id: " + args[1]);
                    return;
                }
            } else if (args.length > 2) {
                p.sendMessage(getUsage());
                return;
            }

            if (id < 1) {
                p.sendMessage(Text.color("&cVault must be a positive number."));
                return;
            }

            if (uuid == p.getUniqueId()) {
                List<String> vaults = LuckPermsHook.getUser(p).getCachedData().getMetaData().getMeta().get("vaults");
                int maxVaults = vaults == null ? 0 : vaults.stream().mapToInt(Integer::parseInt).max().orElse(0);
                if (id > maxVaults) {
                    p.sendMessage(Text.color("&cYou don't have access to vault #" + id + "."));
                    return;
                }
                p.openInventory(new PlayerVault(id, 6, uuid).getInventory());
            } else {
                PlayerVault vault = new PlayerVault(id, 6, uuid);
                if (vault.exists()) {
                    p.openInventory(vault.getInventory());
                } else {
                    p.sendMessage(Text.color("&7Player does not have a vault #" + id + "."));
                }
            }
        });
    }
}
