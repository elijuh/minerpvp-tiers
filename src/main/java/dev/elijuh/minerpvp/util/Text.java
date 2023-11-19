package dev.elijuh.minerpvp.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

@UtilityClass
public class Text {

    public String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String rainbow(String s) {
        String[] colors = {"&c", "&6", "&e", "&a", "&b", "&d", "&5"};
        StringBuilder builder = new StringBuilder();

        int index = 0;
        for (char c : s.toCharArray()) {
            builder.append(colors[index++]);
            if (index >= colors.length) {
                index = 0;
            }
            builder.append(c);
        }

        return builder.toString();
    }

    public String romanNumeral(int i) {
        StringBuilder roman = new StringBuilder();

        while (i >= 1000) {
            roman.append("M");
            i -= 1000;
        }
        while (i >= 900) {
            roman.append("CM");
            i -= 900;
        }
        while (i >= 500) {
            roman.append("D");
            i -= 500;
        }
        while (i >= 400) {
            roman.append("CD");
            i -= 400;
        }
        while (i >= 100) {
            roman.append("C");
            i -= 100;
        }
        while (i >= 90) {
            roman.append("XC");
            i -= 90;
        }
        while (i >= 50) {
            roman.append("L");
            i -= 50;
        }
        while (i >= 40) {
            roman.append("XL");
            i -= 40;
        }
        while (i >= 10) {
            roman.append("X");
            i -= 10;
        }
        if (i == 9) {
            roman.append("IX");
            i -= 9;
        }
        while (i >= 5) {
            roman.append("V");
            i -= 5;
        }
        if (i == 4) {
            roman.append("IV");
            i -= 4;
        }
        while (i >= 1) {
            roman.append("I");
            i -= 1;
        }
        return roman.toString();
    }

    public String getEnchantName(Enchantment enchantment) {
        if (enchantment == Enchantment.DIG_SPEED) return "Mining Speed";
        if (enchantment == Enchantment.ARROW_DAMAGE) return "Arrow Damage";
        if (enchantment == Enchantment.ARROW_FIRE) return "Flaming Arrow";
        if (enchantment == Enchantment.ARROW_INFINITE) return "Infinite Arrows";
        if (enchantment == Enchantment.ARROW_KNOCKBACK) return "Punch";
        if (enchantment == Enchantment.DAMAGE_ALL) return "Melee Damage";
        if (enchantment == Enchantment.DAMAGE_ARTHROPODS) return "Bane of Arthropods";
        if (enchantment == Enchantment.DAMAGE_UNDEAD) return "Smite";
        if (enchantment == Enchantment.DEPTH_STRIDER) return "Water Walk Speed";
        if (enchantment == Enchantment.DURABILITY) return "Durable";
        if (enchantment == Enchantment.FIRE_ASPECT) return "Fire Blade";
        if (enchantment == Enchantment.KNOCKBACK) return "Knockback";
        if (enchantment == Enchantment.LOOT_BONUS_BLOCKS) return "Mining Fortune";
        if (enchantment == Enchantment.LOOT_BONUS_MOBS) return "Looting";
        if (enchantment == Enchantment.LUCK) return "Luck of the Sea";
        if (enchantment == Enchantment.LURE) return "Lure";
        if (enchantment == Enchantment.OXYGEN) return "Aqua Infinity";
        if (enchantment == Enchantment.PROTECTION_ENVIRONMENTAL) return "Protection";
        if (enchantment == Enchantment.PROTECTION_EXPLOSIONS) return "Blast Protection";
        if (enchantment == Enchantment.PROTECTION_FALL) return "Feather Falling";
        if (enchantment == Enchantment.PROTECTION_PROJECTILE) return "Projectile Protecion";
        if (enchantment == Enchantment.PROTECTION_FIRE) return "Fire Protection";
        if (enchantment == Enchantment.SILK_TOUCH) return "Silk Touch";
        if (enchantment == Enchantment.THORNS) return "Thorns";
        if (enchantment == Enchantment.WATER_WORKER) return "Respiration";
        return "Invalid";
    }

    public String compactNumber(double value) {
        BigNumber bigNumber = null;

        for (BigNumber b : BigNumber.values()) {
            if (value >= b.min) {
                bigNumber = b;
            } else {
                break;
            }
        }

        String number = String.valueOf(Math.floor(value * 1E+2) / 1E+2);

        if (bigNumber != null) {
            number = String.valueOf(Math.floor(value / bigNumber.min * 1E+1) / 1E+1);
        }

        while (number.contains(".") && (number.endsWith("0") || number.endsWith(".")) && number.length() > 1) {
            number = number.substring(0, number.length() - 1);
        }

        return bigNumber != null ? number + bigNumber.suffix : number;
    }

    @Getter
    @RequiredArgsConstructor
    private enum BigNumber {
        THOUSAND(1E+3, "k"),
        MILLION(1E+6, "M"),
        BILLION(1E+9, "B"),
        TRILLION(1E+12, "T"),
        QUADRILLION(1E+15, "q"),
        QUINTILLION(1E+18, "Q"),
        SEXTILLION(1E+21, "s"),
        SEPTILLION(1E+24, "S"),
        OCTILLION(1E+27, "O"),
        NONILLION(1E+30, "N"),
        DECILLION(1E+33, "D"),
        UNDECILLION(1E+36, "UD"),
        DUODECILLION(1E+39, "DD"),
        TREDECILLION(1E+42, "TD"),
        QUATTUORDECILLION(1E+45, "qD"),
        QUINDECILLION(1E+48, "QD"),
        SEXDECILLION(1E+51, "sD"),
        SEPDECILLION(1E+54, "SD"),
        OCTODECILLION(1E+57, "OD"),
        NOVEMDECILLION(1E+60, "ND"),
        VIGINTILLION(1E+63, "V");

        private final double min;
        private final String suffix;
    }
}
