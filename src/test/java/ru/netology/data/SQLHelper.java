package ru.netology.data;


import java.sql.*;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;

import org.apache.commons.dbutils.handlers.ScalarHandler;


public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();



        private static final String URL = "jdbc:mysql://localhost:3306/shop_db?useSSL=false&allowPublicKeyRetrieval=true";
        private static final String USER = "app";
        private static final String PASS = "pass";


    private SQLHelper() {}
        @SneakyThrows
    private static Connection getConnection() {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    @SneakyThrows
    public static void cleanDatabase() {
        String deleteOrder = "DELETE FROM order_entity;";
        String deletePayment = "DELETE FROM payment_entity;";
        String deleteCredit = "DELETE FROM credit_request_entity;";

        try (Connection conn = getConnection()) {

            QUERY_RUNNER.execute(conn, deleteOrder);
            QUERY_RUNNER.execute(conn, deletePayment);
            QUERY_RUNNER.execute(conn, deleteCredit);
        }
    }
    // метод для получения статуса последней дебетовой оплаты (APPROVED / DECLINED)
    @SneakyThrows
    public static String getPaymentStatus() {
        String sql = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        try (Connection conn = getConnection()) {
            String status = QUERY_RUNNER.query(conn, sql, new ScalarHandler<>());
            return status != null ? status : ""; // Возвращаем пустую строку вместо null для защиты от NPE в тестах
        }
    }

  /*  @SneakyThrows
    public static String getCreditPaymentStatus() {
        String sql = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        try (Connection conn = getConnection()) {
            Object result = QUERY_RUNNER.query(conn, sql, new ScalarHandler<>());
            // Если результат null (не должно быть для COUNT), вернём 0
           /* String status = QUERY_RUNNER.query(conn, sql, new ScalarHandler<>());
            return status != null ? status : ""; // Возвращаем пустую строку вместо null для защиты от NPE в тестах
        }
    }*/

    // Метод для подсчета количества (возвращает int)
    public static int getCreditRequestCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM credit_request_entity";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1); // Возвращаем число
            }
            return 0;
        }
    }

    // Метод для получения статуса (возвращает String)
    public static String getCreditPaymentStatus() throws SQLException {
        String sql = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String status = rs.getString("status");
                return status != null ? status : "";
            }
            return "";
        }
    }


    // метод проверки количества записей (для негативных тестов, должно возвращать 0)
    @SneakyThrows
    public static long getPaymentRecordsCount() {
        String sql = "SELECT COUNT(*) FROM payment_entity;";
        try (Connection conn = getConnection()) {
            Number count = QUERY_RUNNER.query(conn, sql, new ScalarHandler<>());
            return count != null ? count.longValue() : 0L; // Безопасное приведение любого числового типа БД к long
        }
    }
}

