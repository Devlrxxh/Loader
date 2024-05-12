package me.lrxh.client;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class PluginLoader extends JavaPlugin {
    private InjectedPlugin injectedPlugin;
    private PluginLoader instance;

    public static Plugin receivePluginPacket(Socket socket) throws IOException {
        try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
            String mainClass = inputStream.readUTF();

            int length = inputStream.readInt();

            byte[] bytes = new byte[length];
            inputStream.readFully(bytes);

            return new Plugin(mainClass, bytes);
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        load();
    }

    @Override
    public void onDisable() {
        injectedPlugin.onDisable();
    }

    public void load() {
        try {
            Plugin plugin = receivePluginPacket(new Socket("localhost", 7321));
            System.out.println(plugin.getMainClass());

            ByteClassLoader classLoader = new ByteClassLoader(plugin.getBytes());
            this.injectedPlugin = classLoader.loadClass(plugin.getMainClass(), false)
                    .asSubclass(InjectedPlugin.class).newInstance();

            injectedPlugin.onEnable(get());
        } catch (Exception e) {
            getLogger().severe("Failed to load plugin. " + e.getMessage());
        }
    }

    public JavaPlugin get() {
        return instance == null ? new PluginLoader() : instance;
    }
}
