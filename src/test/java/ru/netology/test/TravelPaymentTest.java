package ru.netology.test;


import static com.codeborne.selenide.Selenide.*;
;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import ru.netology.Data.DataHelper;


import ru.netology.Page.PaymentPage;
import ru.netology.Page.StartPage;




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

  /*  @AfterEach
    @SneakyThrows
    void clearDb() {
        SQLHelper.cleanDatabase();
    }*/


    @Test
    @DisplayName("P1 | Купить: успешная оплата картой")
    void shouldSuccessfullyPayWithBuyButton() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();

        var CardInfo = DataHelper.getApprovedCard();

        PaymentPage.fillForm(CardInfo);

        PaymentPage.getNotificationOk();


    }

}
