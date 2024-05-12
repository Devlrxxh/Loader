package org.example;

import me.lrxh.client.InjectedPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main implements InjectedPlugin {
    private JavaPlugin plugin;

    public void onEnable(JavaPlugin plugin) {
        this.plugin = plugin;

        System.out.println("Plugin loaded!");
        plugin.getLogger().info("yay");

    }

    public void onDisable() {
        System.out.println();
    }
}
