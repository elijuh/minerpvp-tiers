package dev.elijuh.minerpvp.user.levels;

import dev.elijuh.minerpvp.user.levels.rewards.Reward;
import dev.elijuh.minerpvp.user.levels.rewards.impl.DummyReward;
import dev.elijuh.minerpvp.user.levels.rewards.impl.IncrementingReward;
import dev.elijuh.minerpvp.util.ItemBuilder;
import dev.elijuh.minerpvp.util.SkullBuilder;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum LevelPrestige {
    STONE("Stone Prestige", 1, new char[]{'7'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/154818233c811873e85f5a4ea4429b75f23b6ae0ea6f5fc0f7bb420d7c471")),
    IRON("Iron Prestige", 5, new char[]{'f'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/46a5da5a4d2b8213b2e98e2c5be5bd89a5c791492ca7c46c174d9a1376f0503d")),
    GOLD("Gold Prestige", 10, new char[]{'6'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/97f57e7aa8de86591bb0bc52cba30a49d931bfabbd47bbc80bdd662251392161")),
    DIAMOND("Diamond Prestige", 15, new char[]{'b'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/a00b26a42df13c769942b01727e0a4205bbd56c61c5fbd25ce35f3d7478c73b8")),
    EMERALD("Emerald Prestige", 20, new char[]{'2'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/86addbd5dedad40999473be4a7f48f6236a79a0dce971b5dbd7372014ae394d")),
    SAPPHIRE("Sapphire Prestige", 25, new char[]{'3'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/e8c511b961b2dca012f3a5f5b466078a0ec2380a76d519ea364a7d6dc28e1bb")),
    RUBY("Ruby Prestige", 30, new char[]{'4'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/f83236639607036c1ba391c2b46a9c7b0efd760c8bfa2996a6055582b4da5")),
    COAL("Coal Prestige", 35, new char[]{'8'}, SkullBuilder.create().texture("https://textures.minecraft.net/texture/ae8e670a79b616334bc1fb9139132fb35c57dda6975af9ce48333e94aa6c9573")),
    OPAL("Opal Prestige", 40, new char[]{'9'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/19801721c771c3c4898cb7d248321d89a9087a92d3dd58163cbd4e12ed75b549")),
    AMETHYST("Amethyst Prestige", 45, new char[]{'5'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/a79f8c92776d642d119f9e92360b1e5b971e66e61428a3e1b311d8b6185e6")),
    EARTH("Earth Prestige", 50, new char[]{'2', 'a'}, SkullBuilder.create().texture("https://textures.minecraft.net/texture/11b3188fd44902f72602bd7c2141f5a70673a411adb3d81862c69e536166b")),
    WATER("Water Prestige", 60, new char[]{'3', 'b'}, ItemBuilder.create(Material.WATER_BUCKET)),
    FIRE("Fire Prestige", 70, new char[]{'6', 'e'}, ItemBuilder.create(Material.FLINT_AND_STEEL)),
    SHADOW("Shadow Prestige", 80, new char[]{'f', '8', '7'}, SkullBuilder.create().texture("https://textures.minecraft.net/texture/704baabf7ef854825aae1992e4a75ff7286ed1654d8f1a08952e7b8669cf692d")),
    CRYSTAL("Crystal Prestige", 90, new char[]{'5', 'd'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/8412af68edc3ead7e3aa99e9238166359fee618904ececfac3aabe4b27753ed3")),
    RAINBOW("Rainbow Prestige", 100, new char[]{'c', '6', 'e', 'a', 'b', 'd', '5'}, SkullBuilder.create().texture("http://textures.minecraft.net/texture/7be7545297dfd6266bbaa2051825e8879cbfa42c7e7e24e50796f27ca6a18"));
    private final String displayName;
    private final int minLevel;
    private final char[] colors;
    private final ItemBuilder icon;

    LevelPrestige(String displayName, int minLevel, char[] colors, ItemBuilder icon) {
        this.displayName = displayName;
        this.minLevel = minLevel;
        this.colors = colors;
        this.icon = icon;
    }

    public ItemBuilder getIcon() {
        return icon.clone();
    }

    public String getPrefix(int level, String icon) {
        String bracketPrefix = this.ordinal() >= EARTH.ordinal() ? "§k" : "";

        return color(bracketPrefix + "[" + level + icon + bracketPrefix + "]");
    }

    public String color(String s) {
        StringBuilder builder = new StringBuilder();

        char lastColor = 'r';
        boolean followingSection = false;
        int index = 0;
        int skips = 0;
        for (char c : s.toCharArray()) {
            if (skips > 0) {
                builder.append(c);
                if (followingSection) {
                    lastColor = c;
                    followingSection = false;
                }
                skips--;
                continue;
            }
            char color = colors[index++];
            if (index >= colors.length) index = 0;

            if (color != lastColor || c == '§') {
                builder.append('§').append(color);
                lastColor = color;
            }
            if (c == '§') {
                skips += 2;
                builder.append(c);
                followingSection = true;
            } else {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    public static LevelPrestige getPrestige(long level) {
        LevelPrestige value = STONE;
        for (LevelPrestige prestige : values()) {
            if (level >= prestige.minLevel && prestige.minLevel > value.minLevel) {
                value = prestige;
            }
        }

        return value;
    }

    private static final Reward LEVEL_UP_REWARD = new IncrementingReward<>("coins", 2500L);

    public static List<Reward> getRewards(int level) {
        List<Reward> rewards = new ArrayList<>();
        rewards.add(LEVEL_UP_REWARD);
        for (LevelPrestige prestige : values()) {
            if (prestige.minLevel == level) {
                rewards.add(new DummyReward(prestige.color(prestige.displayName)));
                LevelIcon iconReward = LevelIcon.getByPrestige(prestige);
                if (iconReward != null) {
                    rewards.add(new DummyReward(iconReward.getDisplayName()));
                }
                break;
            }
        }
        return rewards;
    }
}
