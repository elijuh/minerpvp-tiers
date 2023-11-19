package dev.elijuh.minerpvp.leaderboards;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.util.DocumentUtil;
import dev.elijuh.minerpvp.util.Text;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Location;

import java.text.NumberFormat;
import java.util.Locale;

@Getter
public class Leaderboard {
    private static int counter = 0;
    private final String field;
    private final Location location;
    @Getter
    private final Hologram hologram;

    private final HologramPage page;

    public Leaderboard(String title, String field, Location location) {
        this.field = field;
        this.location = location;
        this.hologram = DHAPI.createHologram("lb-" + counter++, location);
        page = hologram.getPage(0);
        page.addLine(new HologramLine(page, page.getNextLineLocation(), Text.color(title)));
    }

    public void refresh() {
        NumberFormat n = NumberFormat.getInstance(Locale.US);
        Bson projections = Projections.fields(
                Projections.include(field),
                Projections.excludeId(),
                Projections.computed("name", new Document("$ifNull", Lists.newArrayList("$coloredName", "$name")))
        );
        MongoCursor<Document> documents = Core.i().getUserManager().getCollection().find().projection(projections).sort(Sorts.descending(field)).limit(10).iterator();
        for (int i = 0; i < 10; i++) {
            String content;
            if (documents.hasNext()) {
                Document document = documents.next();
                String name = document.getString("name");
                Object value = DocumentUtil.getDotNotation(document, field, 0);
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
