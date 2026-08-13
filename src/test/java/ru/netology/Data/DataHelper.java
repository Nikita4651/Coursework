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

    public static CardInfo getApprovedCard() {
        return new CardInfo("1111222233334444", getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());

    }

    public static CardInfo getApprovedCreditCard() {
        return new CardInfo("5555666677778888", getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());

    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo("5555666677778888", getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
    }

    public static String getValidMonth() {
        return LocalDate.now()
                .format(DateTimeFormatter.ofPattern("MM"));
    }
    public static String getPlusMonth() {
        return LocalDate.now()
                .plusMonths(4)
                .format(DateTimeFormatter.ofPattern("MM"));
    }
    public static String getMinusMonth() {
        return LocalDate.now()
                .minusMonths(5)
                .format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getValidYear() {
        return LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getPlusYear() {
        return LocalDate.now()
                .plusYears(5)
                .format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getMinusYear() {
        return LocalDate.now()
                .minusMonths(2)
                .format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getValidOwner() {
        return "Ivan Ivanov";
    }

    public static String getCyrillic() {
        return "Иванов";
    }

    public static String getValidCvc() {
        return "111";
    }

    public static String getInValidCvc() {
        return "12";
    }

    public static String getNumbers() {
        return "1111 2222 3333";
    }

    public static String getSymbol() {
        return "@#!%";
    }




    @Value
    public static class CardInfo {
        String cardNumber;// номер карты
        String month;//
        String year;
        String owner;
        String cvc; // CVC
    }


}
