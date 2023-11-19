package dev.elijuh.minerpvp.command.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mongodb.client.model.*;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.command.MCommand;
import dev.elijuh.minerpvp.user.User;
import dev.elijuh.minerpvp.util.Text;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CoinsCommand extends MCommand {
    private final Collation collation = Collation.builder().locale("en").collationStrength(CollationStrength.PRIMARY).build();
    private final Bson projections = Projections.fields(
            Projections.excludeId(),
            Projections.computed("coloredName", new Document("$ifNull", Lists.newArrayList("$coloredName", "$name"))),
            Projections.include("coins")
    );

    public CoinsCommand() {
        super("coins", ImmutableList.of("bal", "balance"), null);
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return null;
    }

    @Override
    public void onExecute(Player p, String[] args) {
        if (args.length == 0) {
            args = new String[]{p.getName()};
        }
        onConsole(p, args);
    }

    @Override
    public void onConsole(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Optional<User> user = Core.i().getUserManager().getUser(args[0]);
            if (user.isPresent()) {
                sender.sendMessage(Text.color("&6&lCoins &8» " + user.get().getData().getString("coloredName")
                        + " &7has &e" + NumberFormat.getInstance().format(user.get().getCoins()) + "⛁"));
            } else {
                CompletableFuture.runAsync(()-> {
                    Document data = Core.i().getUserManager().getCollection()
                            .find(Filters.eq("name", args[0]))
                            .collation(collation)
                            .projection(projections)
                            .first();

                    if (data == null) {
                        sender.sendMessage(Text.color("&cPlayer does not exist."));
                    } else {
                        sender.sendMessage(Text.color("&6&lCoins &8» " + data.getString("coloredName")
                                + " &7has &e" + NumberFormat.getInstance().format(data.get("coins", 0L)) + "⛁"));
                    }
                });
            }
        } else if (args.length == 3 && args[1].equalsIgnoreCase("give") && sender.hasPermission("minerpvp.coins.give")) {
            long amount;
            try {
                amount = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Text.color("Invalid amount: " + args[2]));
                return;
            }
            Optional<User> user = Core.i().getUserManager().getUser(args[0]);
            if (user.isPresent()) {
                user.get().addCoins(amount);
                sender.sendMessage(Text.color("&6&lCoins &8» " + user.get().getData().getString("coloredName")
                        + " &7now has &e" + NumberFormat.getInstance().format(user.get().getCoins()) + "⛁"));
                user.get().getPlayer().ifPresent(p -> p.sendMessage(Text.color("&a&lCoins &8» &e" + NumberFormat.getInstance().format(amount) + "⛁ &ahas been given to you.")));
            } else {
                CompletableFuture.runAsync(()-> {
                    Document data = Core.i().getUserManager().getCollection()
                            .findOneAndUpdate(Filters.eq("name", args[0]), new Document("$inc", new Document("coins", amount)),
                                    new FindOneAndUpdateOptions().collation(collation));

                    if (data == null) {
                        sender.sendMessage(Text.color("&cPlayer does not exist."));
                    } else {
                        sender.sendMessage(Text.color("&6&lCoins &8» " + data.getString("coloredName")
                                + " &7now has &e" + NumberFormat.getInstance().format(data.get("coins", 0L) + amount) + "⛁"));
                    }
                });
            }
        } else if (args.length == 3 && args[1].equalsIgnoreCase("take") && sender.hasPermission("minerpvp.coins.take")) {
            long amount;
            try {
                amount = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Text.color("Invalid amount: " + args[2]));
                return;
            }
            Optional<User> user = Core.i().getUserManager().getUser(args[0]);
            if (user.isPresent()) {
                user.get().addCoins(-amount);
                sender.sendMessage(Text.color("&6&lCoins &8» " + user.get().getData().getString("coloredName")
                        + " &7now has &e" + NumberFormat.getInstance().format(user.get().getCoins()) + "⛁"));
            } else {
                CompletableFuture.runAsync(()-> {
                    Document data = Core.i().getUserManager().getCollection()
                            .findOneAndUpdate(Filters.eq("name", args[0]), new Document("$inc", new Document("coins", -amount)),
                                    new FindOneAndUpdateOptions().collation(collation));

                    if (data == null) {
                        sender.sendMessage(Text.color("&cPlayer does not exist."));
                    } else {
                        sender.sendMessage(Text.color("&6&lCoins &8» " + data.getString("coloredName")
                                + " &7now has &e" + NumberFormat.getInstance().format(data.get("coins", 0L) - amount) + "⛁"));
                    }
                });
            }
        } else if (args.length == 3 && args[1].equalsIgnoreCase("set") && sender.hasPermission("minerpvp.coins.set")) {
            long amount;
            try {
                amount = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Text.color("Invalid amount: " + args[2]));
                return;
            }
            Optional<User> user = Core.i().getUserManager().getUser(args[0]);
            if (user.isPresent()) {
                user.get().getData().put("coins", amount);
                sender.sendMessage(Text.color("&6&lCoins &8» " + user.get().getData().getString("coloredName")
                        + " &7now has &e" + NumberFormat.getInstance().format(user.get().getCoins()) + "⛁"));
            } else {
                CompletableFuture.runAsync(()-> {
                    Document data = Core.i().getUserManager().getCollection()
                            .findOneAndUpdate(Filters.eq("name", args[0]), new Document("$set", new Document("coins", amount)),
                                    new FindOneAndUpdateOptions().collation(collation));

                    if (data == null) {
                        sender.sendMessage(Text.color("&cPlayer does not exist."));
                    } else {
                        sender.sendMessage(Text.color("&6&lCoins &8» " + data.getString("coloredName")
                                + " &7now has &e" + NumberFormat.getInstance().format(data.get("coins", 0L) + (amount - data.get("coins", 0L))) + "⛁"));
                    }
                });
            }
        }
    }
}
