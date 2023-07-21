package dev.cybo.devroomquizgame.manager;

import dev.cybo.devroomquizgame.DevRoomQuizGame;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class QuizManager {

    private final DevRoomQuizGame plugin;
    private final Map<String, String> quizCache;
    private final Map<String, BukkitTask> runnableMap;

    public QuizManager(DevRoomQuizGame plugin) {
        this.plugin = plugin;
        this.quizCache = new HashMap<>();
        this.runnableMap = new HashMap<>();
    }

    public void startQuiz(Player player, String quiz) {
        quizCache.put(player.getName(), quiz);

        player.sendMessage("");
        player.sendMessage("§a§lQUIZ:");
        player.sendMessage("§f" + quiz.split(";")[0]);
        player.sendMessage("§7§oYou have 20 seconds to answer the question.");
        player.sendMessage("");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);


        runnableMap.put(player.getName(), plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (quizCache.containsKey(player.getName())) {
                player.sendMessage("§cYou ran out of time! The answer was: §f" + getQuizAnswer(player));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                quizCache.remove(player.getName());
                runnableMap.remove(player.getName());
            }
        }, 20 * 20L));
    }

    public void finishQuiz(Player player, boolean success) {
        String quizData = quizCache.remove(player.getName());
        if (quizData != null) {
            BukkitTask task = runnableMap.remove(player.getName());
            if (task != null) {
                task.cancel();
            }
            if (success) {
                player.sendMessage("§aYou answered correctly!");
                player.sendMessage("§a+ 1 score");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                plugin.getMariaDB().executeQueryAsync("UPDATE quiz_stats SET score = score + 1 WHERE player_name = '" + player.getName() + "';");
            } else {
                player.sendMessage("§cYou answered incorrectly!");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            }
        }
    }

    public boolean hasQuiz(Player player) {
        return quizCache.containsKey(player.getName());
    }

    public String getQuiz(Player player) {
        String quizData = quizCache.get(player.getName());
        if (quizData != null) {
            return quizData.split(";")[0];
        }
        return null;
    }

    public String getQuizAnswer(Player player) {
        String quizData = quizCache.get(player.getName());
        if (quizData != null) {
            return quizData.split(";")[1];
        }
        return null;
    }

}
