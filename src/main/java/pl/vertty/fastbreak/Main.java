package pl.vertty.fastbreak;

import cn.nukkit.plugin.PluginBase;
import pl.vertty.fastbreak.listener.SpeedmineModule;
import pl.vertty.fastbreak.listener.UserCreateListener;

public class Main extends PluginBase {

    private static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().alert("§3§lSaving config.yml");
        this.saveDefaultConfig();
        getLogger().alert("§3§lLoading events....");
        getServer().getPluginManager().registerEvents(new SpeedmineModule(), this);
        getServer().getPluginManager().registerEvents(new UserCreateListener(), this);
        getLogger().alert("§3§lEvents loaded");
        PluginMetrics.metricsStart();
        getLogger().alert("§3§lThe plugin works properly, in case of problems, please visit here: xyz");
    }

    public static Main getPlugin() {
        return plugin;
    }
}