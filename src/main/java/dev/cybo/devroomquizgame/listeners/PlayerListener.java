package dev.cybo.devroomquizgame.listeners;

import dev.cybo.devroomquizgame.DevRoomQuizGame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private final DevRoomQuizGame plugin;

    public PlayerListener(DevRoomQuizGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getMariaDB().executeQueryAsync("INSERT IGNORE INTO quiz_stats (player_name, score) VALUES ('" + event.getPlayer().getName() + "', 0);");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.getQuizManager().hasQuiz(player)) {
            event.setCancelled(true);
            plugin.getQuizManager().finishQuiz(player, event.getMessage().equalsIgnoreCase(plugin.getQuizManager().getQuizAnswer(player)));
        }
    }
}
