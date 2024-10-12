package org.allenpixel.allenuhc.game;

import org.allenpixel.allenuhc.AllenUHC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameManager {

    private final AllenUHC plugin;
    private final Map<UUID, PlayerStatus> playerStatus;
    private final List<UUID> queue;

    public GameManager(AllenUHC plugin) {
        this.plugin = plugin;
        this.playerStatus = new HashMap<>();
        this.queue = new ArrayList<>();
    }

    public void addPlayerToQueue(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (!queue.contains(playerUUID)) {
            queue.add(playerUUID);
            playerStatus.put(playerUUID, PlayerStatus.QUEUED);
            player.sendMessage("你已加入遊戲隊列。");
            checkQueueAndStartGame();
        } else {
            player.sendMessage("你已經在隊列中了。");
        }
    }

    private void checkQueueAndStartGame() {
        // 這裡實現檢查隊列和開始遊戲的邏輯
        // 如果隊列達到要求人數，則開始遊戲準備階段
    }

    public PlayerStatus getPlayerStatus(UUID playerUUID) {
        return playerStatus.getOrDefault(playerUUID, PlayerStatus.NONE);
    }

    // 添加其他遊戲管理方法...

    public enum PlayerStatus {
        NONE,
        QUEUED,
        IN_GAME
    }
}