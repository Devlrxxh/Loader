package dev.lrxh.client;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public interface InjectedPlugin {

    JavaPlugin getPlugin();

    default Server getServer() {
        return getPlugin().getServer();
    }

    void onEnable(JavaPlugin plugin);

    void onDisable();

    default boolean isEnabled() {
        return getPlugin().isEnabled();
    }
}
