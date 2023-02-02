package pl.vertty.fastbreak.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import pl.vertty.fastbreak.managers.UserManager;
import pl.vertty.fastbreak.objects.User;

public class UserCreateListener implements Listener {


    @EventHandler
    public void onUserCreate(PlayerLoginEvent event){
        Player player = event.getPlayer();
        if(UserManager.getUser(player) == null){
            new User(player);
        }
    }

}
