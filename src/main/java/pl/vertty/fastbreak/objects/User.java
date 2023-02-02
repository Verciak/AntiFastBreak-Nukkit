package pl.vertty.fastbreak.objects;

import cn.nukkit.Player;
import pl.vertty.fastbreak.managers.UserManager;

public class User extends UserManager {

    private final String name;
    private long speedminePerSecondTime;
    public int speedminePerSecond;

    public User(Player player){
        name = player.getName();
        speedminePerSecondTime = 0L;
        speedminePerSecond = 0;
        add(this);
    }

    public boolean speedmineLimit(final boolean isHalf) {
        if (this.speedminePerSecondTime < System.currentTimeMillis()) {
            this.speedminePerSecondTime = System.currentTimeMillis() + (isHalf ? 500L : 1000L);
            this.speedminePerSecond = 0;
        }
        return ++this.speedminePerSecond >= (isHalf ? 4 : 10);
    }

    public String getName() {
        return name;
    }
}
