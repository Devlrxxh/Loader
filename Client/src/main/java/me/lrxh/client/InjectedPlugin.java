package me.lrxh.client;

import org.bukkit.plugin.java.JavaPlugin;

public interface InjectedPlugin {

    void onEnable(JavaPlugin plugin);

    void onDisable();
}
