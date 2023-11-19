package dev.elijuh.minerpvp.leaderboards.sub;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.leaderboards.Leaderboard;
import dev.elijuh.minerpvp.user.levels.LevelIcon;
import dev.elijuh.minerpvp.user.levels.LevelMath;
import dev.elijuh.minerpvp.user.levels.LevelPrestige;
import dev.elijuh.minerpvp.util.Text;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Location;

public class LevelLeaderboard extends Leaderboard {
    private static final Bson projections = Projections.fields(
            Projections.excludeId(),
            Projections.include("exp", "icon"),
            Projections.computed("name", new Document("$ifNull", Lists.newArrayList("$coloredName", "$name")))
    );
    private static final Bson sort = Sorts.descending("exp");

    public LevelLeaderboard(Location location) {
        super("&6&lLevel Leaderboard", null, location);
    }

    @Override
    public void refresh() {
        HologramPage page = getHologram().getPage(0);


        MongoCursor<Document> documents = Core.i().getUserManager().getCollection().find().projection(projections).sort(sort).limit(10).iterator();
        for (int i = 0; i < 10; i++) {
            String content;
            if (documents.hasNext()) {
                Document document = documents.next();
                String name = document.getString("name");
                long value = document.get("exp", 0L);
                int level = LevelMath.getLevel(value);
                LevelPrestige prestige = LevelPrestige.getPrestige(level);
                LevelIcon icon = LevelIcon.valueOf(document.get("icon", LevelIcon.DEFAULT.name()));
                content = Text.color("&6&l#" + (i + 1) + "&r " + name + " &8» &r" + prestige.getPrefix(level, icon.getIcon()));
            } else {
                content = Text.color("&6&l#" + (i + 1) + "&r &7&oEmpty &8» &7N/A");
            }

            HologramLine line = page.getLine(i + 1);
            if (line != null) {
                line.setContent(content);
            } else {
                page.addLine(new HologramLine(page, page.getNextLineLocation(), content));
            }
        }
        documents.close();
    }
}
