package dev.cybo.devroomquizgame;

import dev.cybo.devroomquizgame.commands.QuizCommand;
import dev.cybo.devroomquizgame.commands.QuizStatsCommand;
import dev.cybo.devroomquizgame.database.MariaDB;
import dev.cybo.devroomquizgame.listeners.PlayerListener;
import dev.cybo.devroomquizgame.manager.QuizManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DevRoomQuizGame extends JavaPlugin {

    private MariaDB mariaDB;
    private QuizManager quizManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        mariaDB = new MariaDB(
                getConfig().getString("database.host"),
                getConfig().getInt("database.port"),
                getConfig().getString("database.username"),
                getConfig().getString("database.password"),
                getConfig().getString("database.database")
        );

        quizManager = new QuizManager(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("quiz").setExecutor(new QuizCommand(this));
        getCommand("quizstats").setExecutor(new QuizStatsCommand(this));

    }

    public MariaDB getMariaDB() {
        return mariaDB;
    }

    public QuizManager getQuizManager() {
        return quizManager;
    }
}
