package ru.netology.test;


import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;


import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentPage;
import ru.netology.page.StartPage;

import java.sql.SQLException;


public class TravelPaymentTest {


    private PaymentPage paymentPage;


    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {

        open("http://localhost:8080");


    }

    @AfterEach
    @SneakyThrows
    void clearDb() {
        SQLHelper.cleanDatabase();
    }


    @Test
    @DisplayName("TS-1.Входные валидные данные в раздел \"Купить\"")
    void shouldSuccessfullyPayWithBuyButton() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();

        var CardInfo = DataHelper.getApprovedCard();

        PaymentPage.fillForm(CardInfo);

        PaymentPage.getNotificationOk();
        String status = SQLHelper.getPaymentStatus();
        assertEquals("APPROVED", status);


    }

    @Test
    @DisplayName("TS-2.Входные валидные данные в раздел \"Купить в кредит\"")
    void paymentUsingTheBuyOnCreditButtonShouldBeSuccessful() throws SQLException {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForCredit();

        var CardInfo = DataHelper.getApprovedCreditCard();

        PaymentPage.fillForm(CardInfo);

        PaymentPage.getNotificationOk();
       // String status = SQLHelper.getCreditPaymentStatus();
      //  assertEquals("APPROVED", status);



        // 1. Проверяем, что запись была ДО клика (для отладки)
        int countBefore = SQLHelper.getCreditRequestCount();

        // Даем базе время на запись (иногда нужно, особенно в Docker)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 2. Проверяем, что запись появилась ПОСЛЕ клика
        int countAfter = SQLHelper.getCreditRequestCount();

        // ГЛАВНЫЙ АССЕРТ: если countAfter == countBefore, значит, кнопка не сработала
        assertTrue(countAfter > countBefore,
                "Заявка на кредит не создалась в БД! Было записей: " + countBefore + ", стало: " + countAfter);

        // 3. Теперь безопасно проверяем статус
        String status = SQLHelper.getCreditPaymentStatus();
        assertEquals("APPROVED", status,
                "Статус заявки на кредит должен быть APPROVED, а был: '" + status + "'");

    }

}
