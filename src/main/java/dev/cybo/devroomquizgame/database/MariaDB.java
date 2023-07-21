package dev.cybo.devroomquizgame.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class MariaDB {

    private final HikariDataSource hikariDataSource;


    public MariaDB(String host, int port, String username, String password, String database) {

        hikariDataSource = new HikariDataSource();

        hikariDataSource.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
        hikariDataSource.setConnectionTimeout(5000);
        hikariDataSource.setMaxLifetime(20000000);
        hikariDataSource.setMaximumPoolSize(5);
        hikariDataSource.setMinimumIdle(5);
        hikariDataSource.setPoolName("QuizGame-MariaDB");
        hikariDataSource.addDataSourceProperty("url", "jdbc:mariadb://" + host + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&user=" + username + "&password=" + password);

        executeQueryAsync("CREATE TABLE IF NOT EXISTS quiz_stats (player_name VARCHAR(16), score INT);");

    }


    public CompletableFuture<List<DatabaseRow>> executeQueryAsync(String query) {
        return CompletableFuture.supplyAsync(() -> {
            List<DatabaseRow> rows = new ArrayList<>();
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                if (statement.execute()) {
                    try (ResultSet results = statement.getResultSet()) {
                        while (results.next()) {
                            DatabaseRow row = new DatabaseRow();
                            ResultSetMetaData resultSetMetaData = results.getMetaData();
                            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                                row.addCell(resultSetMetaData.getColumnName(i), results.getObject(i));
                            }
                            rows.add(row);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return rows;
        }).exceptionallyAsync((e) -> {
            e.printStackTrace();
            return null;
        });
    }


    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}