package dev.elijuh.minerpvp.user;

import com.mongodb.client.model.Filters;
import dev.elijuh.minerpvp.Core;
import dev.elijuh.minerpvp.user.levels.LevelIcon;
import dev.elijuh.minerpvp.user.levels.LevelMath;
import dev.elijuh.minerpvp.user.levels.LevelPrestige;
import dev.elijuh.minerpvp.user.levels.rewards.Reward;
import dev.elijuh.minerpvp.util.DocumentUtil;
import dev.elijuh.minerpvp.util.Text;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Getter
public class User {
    private final UUID uuid;
    @Setter
    private Player player;

    private final Document data,
            increments = new Document(),
            mongoSets = new Document(),
            mongoUnsets = new Document(),
            extraMongoOperations = new Document();

    public User(UUID uuid, String name) {
        this.uuid = uuid;
        data = Core.i().getUserManager().fetch(uuid, name);
        mongoSets.put("name", name);
    }

    private final Lock lock = new ReentrantReadWriteLock().writeLock();

    private static final String[] setKeys = {
            "exp", "coins", "blocksMined",
            "stats.kills", "stats.deaths",
            "stats.killstreak"
    };

    public long getCoins() {
        return data.get("coins", 0L);
    }

    public long getExp() {
        return data.get("exp", 0L);
    }

    public void addCoins(long amount) {
        data.put("coins", getCoins() + amount);
    }

    public void addExp(long amount) {
        int level1 = LevelMath.getLevel(getExp());
        data.put("exp", getExp() + amount);
        int level2 = LevelMath.getLevel(getExp());
        if (level1 < level2) {
            getPlayer().ifPresent(p -> {
                String icon = getLevelIcon().getIcon();
                for (int level = level1 + 1; level <= level2; level++) {
                    LevelPrestige before = LevelPrestige.getPrestige(level - 1);
                    LevelPrestige after = LevelPrestige.getPrestige(level);
                    List<Reward> rewards = LevelPrestige.getRewards(level);
                    p.sendMessage(Text.color(
                            "\n&8&m---------------------------------" +
                                    "\n&6&lLevel Up!" +
                                    "\n&r" + before.getPrefix(level - 1, icon) + "&r &8➤ &r" + after.getPrefix(level, icon) +
                                    "\n&r" +
                                    "\n&6&lRewards\n&a» " +
                                    rewards.stream().map(Reward::getDisplay).collect(Collectors.joining("\n&a» &r")) +
                                    "\n&8&m---------------------------------"
                    ));
                    rewards.forEach(reward -> reward.give(this));
                }
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            });
        }
    }

    public LevelIcon getLevelIcon() {
        LevelIcon icon = LevelIcon.get(data.get("icon", ""));
        return icon == null ? LevelIcon.DEFAULT : icon;
    }

    public void setLevelIcon(LevelIcon icon) {
        if (icon == null || icon == LevelIcon.DEFAULT) {
            data.remove("icon");
            mongoSets.remove("icon");
        } else {
            data.put("icon", icon.name());
            mongoSets.put("icon", icon.name());
        }
    }

    public String getLevelPrefix() {
        int level = LevelMath.getLevel(getExp());
        LevelPrestige prestige = LevelPrestige.getPrestige(level);
        return prestige.getPrefix(level, getLevelIcon().getIcon());
    }

    public void save() {
        try {
            lock.lock();
            for (String key : setKeys) {
                Object value = DocumentUtil.getDotNotation(data, key, Object.class);
                if (value != null) {
                    mongoSets.put(key, value);
                }
            }
            Document op = new Document("$inc", increments).append("$set", mongoSets).append("$unset", mongoUnsets);
            op.putAll(extraMongoOperations);
            Core.i().getUserManager().getCollection().updateOne(Filters.eq("_id", uuid.toString()), op);
            increments.clear();
            mongoSets.clear();
            mongoUnsets.clear();
            extraMongoOperations.clear();
        } finally {
            lock.unlock();
        }

    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }
}
