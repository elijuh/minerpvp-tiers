package dev.elijuh.minerpvp.user.levels;

import dev.elijuh.minerpvp.user.User;
import dev.elijuh.minerpvp.util.Text;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum LevelIcon {
    DEFAULT("★", "&eDefault Icon", LevelPrestige.STONE),
    IRON("✙", "&fIron Prestige Icon", LevelPrestige.IRON),
    GOLD("❤", "&6Gold Prestige Icon", LevelPrestige.GOLD),
    DIAMOND("☠", "&bDiamond Prestige Icon", LevelPrestige.DIAMOND),
    EMERALD("✦", "&2Emerald Prestige Icon", LevelPrestige.EMERALD),
    SAPPHIRE("✌", "&3Sapphire Prestige Icon", LevelPrestige.SAPPHIRE),
    RUBY("❦", "&4Ruby Prestige Icon", LevelPrestige.RUBY),
    COAL("✵", "&8Coal Prestige Icon", LevelPrestige.COAL),
    OPAL("❣", "&9Opal Prestige Icon", LevelPrestige.OPAL),
    AMETHYST("☯", "&5Amethyst Prestige Icon", LevelPrestige.AMETHYST),
    EARTH("۩", LevelPrestige.EARTH.color("Earth Prestige Icon"), LevelPrestige.EARTH),
    WATER("۞", LevelPrestige.WATER.color("Water Prestige Icon"), LevelPrestige.WATER),
    FIRE("☢", LevelPrestige.FIRE.color("Fire Prestige Icon"), LevelPrestige.FIRE),
    SHADOW("☣", LevelPrestige.SHADOW.color("Shadow Prestige Icon"), LevelPrestige.SHADOW),
    CRYSTAL("✿", LevelPrestige.CRYSTAL.color("Crystal Prestige Icon"), LevelPrestige.CRYSTAL),
    RAINBOW("ಠ_ಠ", Text.rainbow("Rainbow Prestige Icon"), LevelPrestige.RAINBOW);

    private final String icon;
    private final String displayName;
    private final LevelPrestige prestige;

    public boolean has(User user) {
        if (prestige != null) {
            if (LevelMath.getLevel(user.getExp()) >= prestige.getMinLevel()) {
                return true;
            }
        }
        return user.getData().get("icons") instanceof List && user.getData().getList("icons", String.class).contains(name().toLowerCase());
    }

    public static LevelIcon get(String name) {
        name = name.toUpperCase();
        for (LevelIcon icon : values()) {
            if (icon.name().equals(name)) {
                return icon;
            }
        }
        return null;
    }

    public static LevelIcon getByPrestige(LevelPrestige prestige) {
        for (LevelIcon icon : values()) {
            if (icon.prestige == prestige) {
                return icon;
            }
        }
        return null;
    }
}
