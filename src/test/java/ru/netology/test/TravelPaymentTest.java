package ru.netology.test;


import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.getPaymentRecordsCount;
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
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());


    }

    @SneakyThrows
    @Test
    @DisplayName("TS-2.Входные валидные данные в раздел \"Купить в кредит\"")
    void paymentUsingTheBuyOnCreditButtonShouldBeSuccessful()  {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForCredit();

        var CardInfo = DataHelper.getApprovedCreditCard();

        PaymentPage.fillForm(CardInfo);

        PaymentPage.getNotificationOk();

          assertEquals("APPROVED", SQLHelper.getCreditRequestCount());


    }

    @Test
    @DisplayName("TS-1.Входные валидные данные в раздел \"Купить\"")
    void shouldSuccessfullyPayWithBuyButton1() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var CardInfo = DataHelper.getDeclinedCard();
        PaymentPage.fillForm(CardInfo);
        PaymentPage.getNotificationError();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());

    }

    @Test
    @DisplayName("TS-1.Входные валидные данные в раздел \"Купить\"")
    void shouldSuccessfullyPayWithBuyButton2() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo("", "", "", "", "");
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Номер карты", "Неверный формат");
        PaymentPage.checkFieldError("Месяц", "Неверный формат");
        PaymentPage.checkFieldError("Год", "Неверный формат");
        PaymentPage.checkFieldError("Владелец", "Поле обязательно для заполнения");
        PaymentPage.checkFieldError("CVC/CVV", "Неверный формат");

        assertEquals(0, getPaymentRecordsCount());


    }

    @Test
    @DisplayName("TS-1.Входные валидные данные в раздел \"Купить\"")
    void shouldSuccessfullyPayWithBuyButton3() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo("", getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Номер карты", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());


    }

    @Test
    @DisplayName("TS-1.Входные валидные данные в раздел \"Купить\"")
    void shouldSuccessfullyPayWithBuyButton4() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo("", getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Номер карты", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());


    }


}
