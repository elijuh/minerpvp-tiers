package dev.elijuh.minerpvp.command.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.command.MCommand;
import dev.elijuh.minerpvp.util.DocumentUtil;
import dev.elijuh.minerpvp.util.MathUtil;
import dev.elijuh.minerpvp.util.Text;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatsCommand extends MCommand {
    private final NumberFormat format = NumberFormat.getInstance(Locale.US);
    private final Collation collation = Collation.builder().locale("en").collationStrength(CollationStrength.PRIMARY).build();
    private final Bson projections = Projections.fields(
            Projections.excludeId(),
            Projections.include("coins", "stats.kills", "stats.deaths", "stats.killstreak"),
            Projections.computed("name", new Document("$ifNull", Lists.newArrayList("$coloredName", "$name"))),
            Projections.computed("kdr", new Document("$round", Lists.newArrayList(
                    new Document("$divide", Lists.newArrayList(
                            new Document("$ifNull", Lists.newArrayList("$stats.kills", 0)),
                            new Document("$ifNull", Lists.newArrayList("$stats.deaths", 1))
                    )), 2
            )))
    );

    private final Map<CommandSender, Long> lastUsed = new HashMap<>();

    public StatsCommand() {
        super("stats", ImmutableList.of(), null);
        setUsage(Text.color("&cUsage: /stats <player>"));
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return null;
    }

    @Override
    public void onExecute(Player p, String[] args) {
        onConsole(p, args);
    }

    @Override
    public void onConsole(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(getUsage());
            return;
        }
        long last = lastUsed.getOrDefault(sender, 0L);
        long timeLeft = 3000 - (System.currentTimeMillis() - last);
        if (timeLeft > 0) {
            sender.sendMessage(Text.color("&cPlease wait " + MathUtil.round(timeLeft / 1000.0, 1) + "s before using this command again."));
            return;
        }
        Document data = Core.i().getUserManager().getCollection().find(Filters.eq("name", args[0]))
                .projection(projections)
                .collation(collation).first();
        if (data == null) {
            sender.sendMessage(Text.color("&cPlayer doesn't exist."));
            return;
        }
        String msg = "&7&m-------------------------------------" +
                "\n&6⏐&7Stats for " + data.getString("name") +
                "\n&6⏐\n&6⏐&7Coins &8» &e" + format.format(data.get("coins", 0L)) + "⛁" +
                "\n&6⏐&7Kills &8» &f" + format.format(DocumentUtil.getDotNotation(data, "stats.kills", 0)) +
                "\n&6⏐&7Deaths &8» &f" + format.format(DocumentUtil.getDotNotation(data, "stats.deaths", 0)) +
                "\n&6⏐&7K/D Ratio &8» &f" + format.format(data.get("kdr", 0.0)) +
                "\n&6⏐&7Killstreak &8» &f" + format.format(DocumentUtil.getDotNotation(data, "stats.killstreak", 0)) +
                "\n&7&m-------------------------------------";

        sender.sendMessage(Text.color(msg));
        lastUsed.put(sender, System.currentTimeMillis());
    }
}
