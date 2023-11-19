package dev.elijuh.minerpvp.user.levels;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LevelMath {
    public final long BASE = 25000L;
    public final float LEVEL_MULTIPLIER = 0.35F;
    public final int MAX_LEVEL = 500;

    public int getLevel(long exp) {
        int left = 1, right = MAX_LEVEL;
        while (left < right) {
            int mid = left + (right - left + 1) / 2;
            if (getTotalExpForLevel(mid) <= exp) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    public long getExpUntilNextLevel(long exp) {
        return getTotalExpForLevel(getLevel(exp) + 1) - exp;
    }

    public long getLeftOverExp(long exp) {
        return exp - getTotalExpForLevel(getLevel(exp));
    }

    public long getTotalExpForLevel(int level) {
        long totalExp = 0;
        for (int i = 1; i <= level; i++) {
            totalExp += (long) (BASE + (i * LEVEL_MULTIPLIER * BASE));
        }
        return totalExp;
    }
}

