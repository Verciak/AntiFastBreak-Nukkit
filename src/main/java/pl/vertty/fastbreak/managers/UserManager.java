package pl.vertty.fastbreak.managers;

import cn.nukkit.Player;
import com.google.common.collect.Sets;
import pl.vertty.fastbreak.objects.User;

import java.util.Set;

public class UserManager {

    private static final Set<User> userMap = Sets.newConcurrentHashSet();

    public static User getUser(Player name){
        return userMap.stream().filter(account -> account.getName().equals(name.getName())).findFirst().orElse(null);
    }
    protected void add(User account){
        userMap.add(account);
    }

}
