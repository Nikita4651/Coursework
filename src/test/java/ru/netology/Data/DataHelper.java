package ru.netology.Data;


import com.github.javafaker.Faker;

import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static final Faker FAKER = new Faker(new Locale("en"));

    private DataHelper() {
    }

    public static AuthInfo getApprovedCard() {
        return new AuthInfo("1111222233334444", "123");
    }

    public static String generateDate(int shiftDays) {
        return LocalDate.now()
                .plusDays(shiftDays)
                .format(DateTimeFormatter.ofPattern(MM_yy));
    }

        public static String getValidCardHolderName() {
            return "Ivan Ivanov";
        }

        @Value
        public static class AuthInfo {
            String login;   // номер карты
            String password; // CVC
        }

}
