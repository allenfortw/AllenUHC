package org.allenpixel.allenuhc.database;

import org.allenpixel.allenuhc.AllenUHC;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {

    private final AllenUHC plugin;
    private Connection connection;

    public DatabaseManager(AllenUHC plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/player.db");
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().severe("無法初始化數據庫: " + e.getMessage());
        }
    }

    private void createTables() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "uuid TEXT PRIMARY KEY," +
                    "discord_id TEXT," +
                    "verification_code TEXT" +
                    ")");
        } catch (SQLException e) {
            plugin.getLogger().severe("無法創建數據表: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("無法關閉數據庫連接: " + e.getMessage());
        }
    }
    public boolean linkDiscordToMinecraft(UUID playerUUID, String discordId) {
        String sql = "INSERT OR REPLACE INTO players (uuid, discord_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID.toString());
            pstmt.setString(2, discordId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            plugin.getLogger().severe("無法綁定 Discord 和 Minecraft 帳號: " + e.getMessage());
            return false;
        }
    }

    public String getDiscordId(UUID playerUUID) {
        String sql = "SELECT discord_id FROM players WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("discord_id");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("無法獲取玩家的 Discord ID: " + e.getMessage());
        }
        return null;
    }

    // 添加其他數據庫操作方法...
}