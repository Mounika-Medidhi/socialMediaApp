package com.instagram.util;

import java.sql.Connection;

public class JDBCConnectionTest {

    public static void main(String[] args) {

        try {

            Connection connection =
                    JDBCUtil.getConnection();

            System.out.println(
                    "Database connected successfully!"
            );

            connection.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}