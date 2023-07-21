package dev.cybo.devroomquizgame.commands;

import dev.cybo.devroomquizgame.DevRoomQuizGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class QuizCommand implements CommandExecutor {

    private final DevRoomQuizGame plugin;

    public QuizCommand(DevRoomQuizGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return false;
        }
        if (args.length == 0) {
            player.sendMessage("§b[§b§lQuiz§b] §f" + ChatColor.WHITE + "Specify a category:");
            for (String categoryName : plugin.getConfig().getConfigurationSection("categories").getKeys(false)) {
                player.sendMessage(ChatColor.GRAY + " - " + ChatColor.BLUE + categoryName);
            }
        } else if (args.length == 1) {
            String category = args[0];
            List<String> quizList = plugin.getConfig().getStringList("categories." + category);

            if (!quizList.isEmpty()) {
                String selectedQuiz = quizList.get(ThreadLocalRandom.current().nextInt(quizList.size()));
                plugin.getQuizManager().startQuiz(player, selectedQuiz);
            } else {
                player.sendMessage("§b[§b§lQuiz§b] §cCategory not found.");
            }
        } else {
            player.sendMessage("§b[§b§lQuiz§b] §cToo many arguments.");
        }
        return true;
    }
}