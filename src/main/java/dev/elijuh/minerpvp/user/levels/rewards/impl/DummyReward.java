package dev.elijuh.minerpvp.user.levels.rewards.impl;

import dev.elijuh.minerpvp.user.User;
import dev.elijuh.minerpvp.user.levels.rewards.Reward;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DummyReward implements Reward {
    private final String display;

    @Override
    public void give(User user) {
        //do nothing
    }
}
