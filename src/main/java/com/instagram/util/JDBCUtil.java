package com.instagram.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =
                     JDBCUtil.class.getClassLoader()
                             .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new RuntimeException(
                        "db.properties file not found"
                );
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load database configuration",
                    e
            );
        }
    }

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
    }
}