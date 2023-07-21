package dev.cybo.devroomquizgame.commands;

import dev.cybo.devroomquizgame.DevRoomQuizGame;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuizStatsCommand implements CommandExecutor {

    private final DevRoomQuizGame plugin;

    public QuizStatsCommand(DevRoomQuizGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return false;
        }
        plugin.getMariaDB().executeQueryAsync("SELECT score FROM quiz_stats WHERE player_name = '" + player.getName() + "';").thenAccept(rows -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (rows.size() == 0) {
                    player.sendMessage("§b[§b§lQuiz§b] §fScore: §a0");
                } else {
                    player.sendMessage("§b[§b§lQuiz§b] §fScore: §a" + rows.get(0).getInt("score"));
                }
            });
        });
        return false;
    }
}
