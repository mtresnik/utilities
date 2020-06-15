package com.resnik.util.serial.database;

import java.io.File;
import java.sql.*;

public class Database {

    private String name;

    public Database(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        File databaseDir = new File("src/out/database/");
        if(!databaseDir.exists()){
            databaseDir.mkdirs();
        }
        String databaseName = "language.db";
        File databaseFile = new File(databaseDir.getAbsolutePath(), databaseName);
        String url = "jdbc:mysql://localhost";

        // Defines username and password to connect to database server.
        String username = "root";
        String password = "";

        // SQL command to create a database in MySQL.
        String sql = "CREATE DATABASE IF NOT EXISTS DEMODB";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
