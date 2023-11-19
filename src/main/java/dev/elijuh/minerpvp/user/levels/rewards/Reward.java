package dev.elijuh.minerpvp.user.levels.rewards;

import dev.elijuh.minerpvp.user.User;

public interface Reward {

    String getDisplay();

    void give(User user);
}
