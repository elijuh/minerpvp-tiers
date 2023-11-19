package dev.elijuh.minerpvp.user.levels.rewards.impl;

import com.google.common.collect.ImmutableMap;
import dev.elijuh.minerpvp.user.User;
import dev.elijuh.minerpvp.user.levels.rewards.Reward;
import dev.elijuh.minerpvp.util.DocumentUtil;
import lombok.Getter;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

@Getter
public class IncrementingReward<T extends Number> implements Reward {
    private static final NumberFormat NF = NumberFormat.getInstance(Locale.US);
    private static final Map<String, String> COMMON_DISPLAYS = new ImmutableMap.Builder<String, String>()
            .put("coins", "&a%s⛁")
            .put("experience", "&5%s Experience")
            .build();

    private final String key;
    private final T value;
    private final String display;

    public IncrementingReward(String key, T value) {
        this(key, value, COMMON_DISPLAYS.getOrDefault(key, "&cInvalid Reward"));
    }

    public IncrementingReward(String key, T value, String display) {
        this.key = key;
        this.value = value;
        this.display = String.format(display, NF.format(value));
    }

    @Override
    public void give(User user) {
        DocumentUtil.incrementDotNotation(user.getData(), key, value);
    }
}
