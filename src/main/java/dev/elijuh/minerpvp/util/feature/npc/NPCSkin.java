package dev.elijuh.minerpvp.util.feature.npc;

import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NPCSkin {
    MINER(new WrappedSignedProperty("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY4ODUxOTIxMjk1NiwKICAicHJvZmlsZUlkIiA6ICJmYmVlMjQ1MTI4OTc0NDk1YjQyYjg3MWFhNDI4ZDQ2NSIsCiAgInByb2ZpbGVOYW1lIiA6ICJJcnJhdGlvbmFibGUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc5MTRhYTZlOGViZTg3ZWMwMDBmMmJiMjMxMTc4ZGU2ODZmNGFmMGU4YTJlMzg4ZGY1YzRjNTA5M2E3ZTA0NyIKICAgIH0KICB9Cn0=",
        "Xk3a8ivg4iBOmbltct7q+Ahz40qTz07mLIvp2LdQXFMUs2TkgZYgJsyPwwS18dtwQLi+bNAOrKQqaKB5/KvdUS/qGhPSy+JRjfZUVPiZrWOzymEgTz54jipL9cpcUxX/0pf3pvGSO9R9zipRQTi7P0Jotved1I8nDJlLP4LNAEwtowSrVA4rODMuEX2ErNI/ZcTDKNy6oHrrG12YikeZS6RAJnL6T0knWN8EIix0uDpBWjf5yDpf48dsUxK7dT/HRhdRqA+VTd73qC7V0Sh8X3b/J3ytHVxupPGILTJNgQqg59rAw+PicVYJBXRdZmyIxKFw782/+zCawJ9o3rBW1oMs0jKD4lu+vjqvvyIVKV6Ho+qwiLv3qss5IjiG9yBWja78vwHMs52ed5KANK695yV5YHZjgKJsHblk0ERf4RgU8datvbdJLHEam2UG2Et7EXSOLhKB2K0glfGKJXGvqdXrU/1eEgXQws1Uuw3n+Od2tYxx7a05PK+FhOzVM9L0/TXjCKiZ5l2j2NnN/72xJinoRdZ6TpConZImS+nO/HJ5wgaBer4jhVBc013rCLum+5C+OoIavYLzVRVOhq8ZfoQJ3SkayKcWgbZA1l4aYo9KURpIXlKbYh1FBIVQVc4qOA8lWLehvYLoARr3bMRpW+LUqGUJeBOdeSGiv9Bjb2M=")),
    PIRATE(new WrappedSignedProperty("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY4ODUyMjYwNzY3MSwKICAicHJvZmlsZUlkIiA6ICIyMzM0ZjU2NWQ4ZGU0MDNiOTdmOTcyMDU3NjFhY2EyZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIaUV4ZWMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjhkMzI1YzBkNjJmOWMwZDg0MjQxNGE4N2Y1MTYwMTU1ZGRlYjMzMmRkNGMwNmQ2NGNlYzYyYzgzZWQ0MGRkNCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
        "fXd6A4gFyS94dpxsKdRKCejzyRVYREcF5/m8MmFvSWhOXINefy/+lI6dsWmJyMcxUZoGMJarVYnaYSARrzOXOXUTUiC8om5D4zlXRIgUcMf1KEZ7C6qRD1UJweUFY3n9h89lLXp+3RHUuot+QyQjoZBu62J42oeYdRLqcAnO0fiLiSq98qRPZvqo4jDyWXJ3930Kd82qXNaNc1MiPTkzdXL36Dkh7OyFM3fHBzrWvFhF3dgQyCsbd1yqnGK5O2bc5ZRil5TwyGvNtLJ17+R6sqbtTvPSQzwIu0/I2y2OS1oT8FJa4NXVYWk16rCEhGndO/DdZN2+IMmbXeVaAI3R7V2PZaSStX1oaVJvAd0rZAh1ye5VUCnx83DnqsCKGrw8wqy0qDPnY4sC4uon8Yvuc8nK5IrsP9hXTUQnJoLQWMhQovZ07USM/nooYHLygX5RtBcHefBtMQwj9+QIMqrdpaescup7Dtjm3vvB1EQA6TYu2KzC0vYkETAQgt+cqsfJUr0QMLcQltIs8JZrzLH0kqk0mPQAazTf7PrGATv2YYj19dKjl9sNjnwqcE8asAARRo4lWaAWlA3kUxZy5nWggk9FzuWK7g+pHlEjx3jqBAhs/J/dIOO7LB3hi24aRYwMZz+HczMXcP3ZgcaV+PXOCTvrnTi09iQWMScpSDn4TAE=")),
    BLACKSMITH(new WrappedSignedProperty("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY4OTAyOTQ4MzQzNSwKICAicHJvZmlsZUlkIiA6ICI5MmMxMmQzZTE1OGM0OTgyYTVmMjcwMGQyZTZmMTFlZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJBYm92ZXJpYXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM0MmIyOGMwNTU0OGU0N2RjYTBiYzk4NDlhMDI3ZjE1ZDE1YjFkMGRiZTQ3OWI1NWRiN2E2MzExMTU2OWEzNiIKICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0MGMwZTAzZGQyNGExMWIxNWE4YjMzYzJhN2U5ZTMyYWJiMjA1MWIyNDgxZDBiYTdkZWZkNjM1Y2E3YTkzMyIKICAgIH0KICB9Cn0=",
        "e6DSrLmUoXu64pKo8/uc/asQtnppy56aePI6vcoOQhVJ/GIkJuSOolTwqooyWcqQYrweLCFasto5/YHMgC/L1YJWvzgmgc2lBTZbs+T8y4w8qc5NZcW7KeKsQHPzvcD3mLfiupZ4Fl4hP5nS3QmeIsYquq45YgO/FUVSu8OsPfMMlkcaKE+shlVqWOAJ1iB0bom7M1wfRR3jOlqOPAhqowyFeKjp8bUXhIhfCGqe8gM1ftpPH7MSsyOmXR9epiFJ6R66eirAWOy98gsxZ0LK6N7PGJPyDTpYV77RNpQ1peYVJuSevFLP5F6GL4dvTbmqg+3FPXboFrX0807tOfYGfDOsR2c8OPdw3bdHTTlTOCFg/Gn8gl27F7ooY7z0WlCfYTGdqYfL8gXUEcRij1IRS5hNtuIUPGwYVVTz4NDTmcvV7AM6VY8pT/3GvIzF4w11IJC5cDJnrtFGXAd06TZ2yc1cWlRqkMSDoYrYYjEH2pJKGbMPgJxLncT4h2Wi8hgfeTbFbUmRIRK+RN/7J5HYL+uNATlmvQnIv7oFEF5vu3FeNzYKte1oiFZiANv/wwJcfnp/V1Ijs8H9Uc33ZPOrMrIYd8d+RffJjO6WvbHcr9SEEZ0C5yjFWTx9xdXkH6blGhaUPW5pc39FWBMEa65i3Wd8o6WY2HyxPKUNzBBSmX8=")),
    MADDIE(new WrappedSignedProperty("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY4OTM2MzgzODI2MSwKICAicHJvZmlsZUlkIiA6ICJhMDhkNTQyYmViNGU0NjMxOGNhNTI1OWNmMzY5YmVkNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYWRpaXNvbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80MDMzOWZhZGIyYmE4OTgwYTBiODEyYTA5Njg3NmE5YzVmYmQyZjQ0NjdiMzQwZWQwZTJhMTZjOTQ3MGZjZDhhIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
        "JYuXpPMD4qVWzLiWXgkV3Uqek/zNj0ylOYNP2kpLXgHBFpCXTPIul30J0aSvdQIG9apI8HWojtYkaePLZPt8RNmyvGle15a5fCQsLfLGFXj7v6rqD4uJFqsCndeQ1Z7WDpB4MACAl62qRYGTujTNtQoLVSFaPgl3j8OMVo2p55OkAphPzNo3DX6SkOEs4O6TA0/IwYwIM8PBtqt9I8p/IueT4uGAGbMGgB5ee1whAHGO7/IHg5EVjjl8a38/BaKYNZjV5QQiN9N2GuN3bRwZzZO1gb/nq/W4MflUo6T2oQKab0pKHTzY5dACMKCD/KdzhseWQj0dEwo2GO1Nc0d7piltojCAzifZ7RdlTPwgBfxOttGYOBdLDnT/oRPWSDUtkj3dS6zhEa27dVhxlE6OGngb65QIETNXoHGx4x1V3b+Q4GDL6Wsik0zpk+ouMuLcsKX6ZO8eVMZt+7eg0Cd1CVNQiVahhQdgUfxk+G7T2VS3tbbZrCenU01kIkdr+CI2bqD/H7DLOSRfgEYkvmcZwWpYfJprL+yhsBHV+Eq6Ah+5HCGp5KF78TxJO5/VMjV3A5ixREXapYrdKBZ/HFWI8O8aTpK+JteCV0QZgWz0FOuCo/LlfOLeh9+AgWd0MHkEzmz+8f0ftlGvn4qPTB1tqmXYUhYeCxMrFbk+7FEAkhY=")),
    KNIGHT(new WrappedSignedProperty("textures", "ewogICJ0aW1lc3RhbXAiIDogMTcwMDA4NTUyOTk5MCwKICAicHJvZmlsZUlkIiA6ICIxNzg4MDUzZDEzOGI0YjFkYjc0NDA5MmI4YjE2NWE3YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYXRjaGVsbDEyMyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iY2Y1ZmFiZjgwZjYxZjg0MjRiMjllYTVmMjMyNjM0ZWExZjA4MjI4MTEyMWQ3NGQwMWJlNzhjNzNkZjJlODM2IgogICAgfQogIH0KfQ==",
        "Leo5+i8MV/QGTKSUaVD+T9KfUXyiiGZo2aIwO7V5G7nTah6wqdsGr+BKIc+u/KE0O+SdD1yS8/Aw1dDd/e9T45PMa2wZyU2efHyqFQDAjLPpPT4FbWsvr+6Iu7+R46fMlXeqq+aSG+XtLQAipk6JNpcjYjoVvfrMcNqFKx+DXSN7NTiBuj7nAjzXZerlmdbYs1g8izv5ljAY8m7o+iQn85TpLRnIsLqWud6lqz0EoPsz7TrloCEZzFveBrTzoEu5UWXHV6V5ilUeClyyJISMbzHnPT4LLfqxHfrE0f6qPY6pe6UHljrd1efscpagPqVQWy40wnjdkHBR/ErHgUXhGmOdpYRxa1DsILh8pSWfkeF1AXuejjc6w4gfcoNukrJTmMZBGenu2fJuGANbpJVkVVuRh++aTlAG77Y+X0WXAhsppXIB2+QQmNN5LLY9a3gQhZZCIfaDvo7zwziyr7BHtRM7gPEnSUvFcJSQBBQ+IE9Qb5EDZkZvN7QPLbrp+tjvPHaTtr9sgWr/GVB1tronsxun98ADEFtjIGXEKoeOWheQ2tUNqPiQa+IJ7jGsj4b19vuobU7809+2IwfpTox8kAPxqjB3LUg9JWN48pfpjeOnAGSx0GHXYMZi7cKk95UW2X2Vam0Wf60PKW5DUk0dFw9BbQuyJgCFQLqZe1X8epU="));

    private final WrappedSignedProperty textures;
    private final String id = "#" + name();

    public static NPCSkin fromId(String skinId) {
        for (NPCSkin skin : values()) {
            if (skin.id.equalsIgnoreCase(skinId)) {
                return skin;
            }
        }
        return null;
    }
}
