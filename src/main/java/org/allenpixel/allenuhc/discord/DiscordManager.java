package org.allenpixel.allenuhc.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.allenpixel.allenuhc.AllenUHC;
import org.allenpixel.allenuhc.database.DatabaseManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DiscordManager extends ListenerAdapter {

    private final AllenUHC plugin;
    private JDA jda;
    private final Map<String, UUID> verificationCodes;

    public DiscordManager(AllenUHC plugin) {
        this.plugin = plugin;
        this.verificationCodes = new HashMap<>();
    }

    public void initialize() {
        String token = plugin.getConfigManager().getDiscordBotToken();
        try {
            jda = JDABuilder.createDefault(token)
                    .addEventListeners(this)
                    .build();
            jda.awaitReady();
            plugin.getLogger().info("Discord bot 已成功連接。");
        } catch (Exception e) {
            plugin.getLogger().severe("無法連接到 Discord: " + e.getMessage());
        }
    }

    public String generateVerificationCode(UUID playerUUID) {
        String code = generateRandomCode();
        verificationCodes.put(code, playerUUID);
        return code;
    }

    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromType(net.dv8tion.jda.api.entities.channel.ChannelType.PRIVATE)) return;

        String message = event.getMessage().getContentRaw();
        if (verificationCodes.containsKey(message)) {
            UUID playerUUID = verificationCodes.get(message);
            String discordId = event.getAuthor().getId();

            DatabaseManager dbManager = plugin.getDatabaseManager();
            if (dbManager.linkDiscordToMinecraft(playerUUID, discordId)) {
                event.getChannel().sendMessage("驗證成功！你的 Discord 帳號已與 Minecraft 帳號綁定。").queue();
                verificationCodes.remove(message);

                // 通知在線的玩家驗證成功
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    org.bukkit.entity.Player player = plugin.getServer().getPlayer(playerUUID);
                    if (player != null && player.isOnline()) {
                        player.sendMessage("§a你的 Discord 帳號驗證成功！");
                    }
                });
            } else {
                event.getChannel().sendMessage("驗證失敗，請稍後再試。").queue();
            }
        }
    }

    public void sendDiscordMessage(String discordId, String message) {
        jda.retrieveUserById(discordId).queue(user -> {
            if (user != null) {
                user.openPrivateChannel().queue(channel ->
                        channel.sendMessage(message).queue());
            }
        });
    }

    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
        }
    }
}