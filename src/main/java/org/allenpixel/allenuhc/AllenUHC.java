package org.allenpixel.allenuhc;

import org.bukkit.plugin.java.JavaPlugin;
import org.allenpixel.allenuhc.config.ConfigManager;
import org.allenpixel.allenuhc.database.DatabaseManager;
import org.allenpixel.allenuhc.game.GameManager;

public class AllenUHC extends JavaPlugin {

    private static AllenUHC instance;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;

        // 初始化配置管理器
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // 初始化數據庫管理器
        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();

        // 初始化遊戲管理器
        gameManager = new GameManager(this);

        // 註冊命令
//        getCommand("uhc").setExecutor(new UHCCommand(this));
//        getCommand("uhcadmin").setExecutor(new AdminCommand(this));
//        getCommand("uhcarena").setExecutor(new ArenaCommand(this));

        // 註冊監聽器
//        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getLogger().info("AllenUHC 插件已啟用!");
    }

    @Override
    public void onDisable() {
        // 清理資源
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }

        getLogger().info("AllenUHC 插件已禁用!");
    }

    public static AllenUHC getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}