package ru.netology.Data;



import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {}

    @SneakyThrows
    private static Connection getConn() {
        return DriverManager.getConnection(
                System.getProperty("db.url", "jdbc:mysql://localhost:3306/aqa_shop_test?serverTimezone=UTC"),
                System.getProperty("db.user", "root"),
                System.getProperty("db.password", "")
        );
    }

    public static PaymentRecord getLastPayment() throws SQLException {
        var sql = "SELECT card_number_masked, status, payment_method FROM payments ORDER BY created DESC LIMIT 1";
        try (var conn = getConn()) {
            return QUERY_RUNNER.query(conn, sql, new BeanHandler<>(PaymentRecord.class));
        }
    }

    @SneakyThrows
    public static void cleanDatabase() {
        try (var conn = getConn()) {
            QUERY_RUNNER.execute(conn, "DELETE FROM payments");
        }
    }

    // Структура записи должна совпадать с полями в БД
    public static class PaymentRecord {
        String card_number_masked;
        String status;
        String payment_method;
    }
}
