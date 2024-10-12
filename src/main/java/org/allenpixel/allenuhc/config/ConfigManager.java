package org.allenpixel.allenuhc.config;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.allenpixel.allenuhc.AllenUHC;

public class ConfigManager {

    private final AllenUHC plugin;
    private FileConfiguration config;

    public ConfigManager(AllenUHC plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public Location getLobbyLocation() {
        return new Location(
                plugin.getServer().getWorld(config.getString("lobby.world")),
                config.getDouble("lobby.x"),
                config.getDouble("lobby.y"),
                config.getDouble("lobby.z"),
                (float) config.getDouble("lobby.yaw"),
                (float) config.getDouble("lobby.pitch")
        );
    }

    public String getDiscordBotToken() {
        return config.getString("discord.token");
    }

    // 添加其他配置獲取方法...
}