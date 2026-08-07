package ru.netology.Data;

import com.google.common.base.Verify;
import lombok.Value;

public class DataHelper {
    private DataHelper() {

    }

    public static VerificationCode getVerificationCode() {
        return new VerificationCode("12345");
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("1111 2222 3333 4444");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("5555 6666 7777 8888");
    }

    public static int generateValidAmount(int balance) {
        return Math.abs(balance) / 20;
    }

    public static int generateInvalidAmount(int balance) {
        return Math.abs(balance) + 1;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    @Value
    public static class CardInfo {
        String cardNumber;
        String testId;
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }
}
