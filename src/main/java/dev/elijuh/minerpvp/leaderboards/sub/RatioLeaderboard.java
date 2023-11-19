package dev.elijuh.minerpvp.leaderboards.sub;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.leaderboards.Leaderboard;
import dev.elijuh.minerpvp.util.DocumentUtil;
import dev.elijuh.minerpvp.util.Text;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Location;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RatioLeaderboard extends Leaderboard {

    private final List<Bson> aggregates;

    public RatioLeaderboard(String title, String numerator, String denominator, Location location) {
        super(title, null, location);

        Bson projections = Aggregates.project(Projections.fields(
                Projections.excludeId(),
                Projections.computed("name", new Document("$ifNull", Lists.newArrayList("$coloredName", "$name"))),
                Projections.computed("ratio", new Document("$round", Lists.newArrayList(
                        new Document("$divide", Lists.newArrayList(
                                new Document("$ifNull", Lists.newArrayList("$" + numerator, 0)),
                                new Document("$ifNull", Lists.newArrayList("$" + denominator, 1))
                        )), 2
                )))
        ));

        aggregates = Lists.newArrayList(projections,
                Aggregates.sort(Sorts.descending("ratio")),
                Aggregates.limit(10)
        );
    }

    @Override
    public void refresh() {
        HologramPage page = getHologram().getPage(0);
        NumberFormat n = NumberFormat.getInstance(Locale.US);
        MongoCursor<Document> documents = Core.i().getUserManager().getCollection().aggregate(aggregates).iterator();

        for (int i = 0; i < 10; i++) {
            String content;
            if (documents.hasNext()) {
                Document document = documents.next();
                String name = document.getString("name");
                Object value = DocumentUtil.getDotNotation(document, "ratio", 0);
                content = Text.color("&6&l#" + (i + 1) + "&r " + name + " &8» &a" + n.format(value));
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
