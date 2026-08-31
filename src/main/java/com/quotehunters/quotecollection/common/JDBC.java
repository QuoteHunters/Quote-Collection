package com.quotehunters.quotecollection.common;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JDBC {
    public static Connection getConnection() {
        Connection connection = null;
        Properties properties = new Properties();

        try {
            properties.load(new FileReader("src/main/java/com/quotehunters/quotecollection/config/connection-info.properties"));
            String driver = properties.getProperty("driver");
            String url = properties.getProperty("url");

            Class.forName(driver);

            connection = DriverManager.getConnection(url, properties);

            connection.setAutoCommit(false);

        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new IllegalStateException("데이터베이스 연결 중 오류가 발생했습니다.", e);
        }

        return connection;
    }

    public static void close(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("[오류] 데이터베이스 자원 정리 중 오류가 발생했습니다.");
        }
    }

    public static void close(Statement statement) {
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        } catch (SQLException e) {
            System.err.println("[오류] 데이터베이스 자원 정리 중 오류가 발생했습니다.");
        }
    }

    public static void close(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            System.err.println("[오류] 데이터베이스 자원 정리 중 오류가 발생했습니다.");
        }
    }

    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException e) {
            System.err.println("[오류] 데이터베이스 자원 정리 중 오류가 발생했습니다.");
        }
    }

    public static void commit(Connection connection) {
        if (connection == null) {
            throw new IllegalStateException("데이터 저장을 확정할 연결이 없습니다.");
        }

        try {
            if (connection.isClosed()) {
                throw new IllegalStateException("닫힌 연결로 데이터 저장을 확정할 수 없습니다.");
            }

            connection.commit();
        } catch (SQLException e) {
            throw new IllegalStateException("데이터 저장 확정 중 오류가 발생했습니다.", e);
        }
    }

    public static void rollback(Connection connection) {
        if (connection == null) {
            throw new IllegalStateException("데이터를 복구할 연결이 없습니다.");
        }

        try {
            if (connection.isClosed()) {
                throw new IllegalStateException("닫힌 연결로 데이터를 복구할 수 없습니다.");
            }

            connection.rollback();
        } catch (SQLException e) {
            throw new IllegalStateException("데이터 복구 중 오류가 발생했습니다.", e);
        }
    }
}
