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
    void shouldSuccessfullyPayWithBuyOnCreditButton()  {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForCredit();

        var CardInfo = DataHelper.getApprovedCreditCard();

        PaymentPage.fillForm(CardInfo);

        PaymentPage.getNotificationOk();

          assertEquals("APPROVED", SQLHelper.getCreditRequestCount());


    }



    @Test
    @DisplayName("TS-3.входные невалидные данные в разделе \"Купить\" пустые поля:")
    void shouldShowInvalidFormatErrorsWhenAllFieldsAreEmpty() {
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
    @DisplayName("TS-4.входные невалидные данные в разделе \"Купить\" с пустым полем карты")
    void shouldShowInvalidFormatErrorWhenCardFieldIsEmpty() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo("", getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Номер карты", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());


    }

    @Test
    @DisplayName("TS-5.входные невалидные данные в разделе \"Купить\" поля карты")
    void shouldFailPaymentWhenCardNumberIsInvalid() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var CardInfo = DataHelper.getDeclinedCard();
        PaymentPage.fillForm(CardInfo);
        PaymentPage.getNotificationError();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());

    }

    @Test
    @DisplayName("TS-6.входные невалидные данные в разделе \"Купить\" с заполнением не полным номером карты")
    void shouldShowInvalidFormatErrorWhenCardNumberIsIncomplete() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();

        var cardInfo = new DataHelper.CardInfo(DataHelper.getNumbers(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Номер карты", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }

    @Test
    @DisplayName("TS-7.входные невалидные данные в разделе \"Купить\" с использованием спецсимволов в поле карты")
    void shouldShowInvalidFormatErrorWhenCardFieldContainsSpecialCharacters() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(DataHelper.getSymbol(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Номер карты", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }



    @Test
    @DisplayName("TS-8.входные невалидные данные в разделе \"Купить\" с использованием латинских букв в поле карты")
    void shouldShowInvalidFormatErrorWhenCardFieldContainsLatinLetters() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(DataHelper.getValidOwner(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Номер карты", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }


    @Test
    @DisplayName("TS-9.входные невалидные данные в разделе \"Купить\" с использованием кириллицы в поле карты")
    void shouldShowInvalidFormatErrorWhenCardFieldContainsCyrillicCharacters() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(DataHelper.getCyrillic(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Номер карты", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }


    @Test
    @DisplayName("TS-10.входные невалидные данные в разделе \"Купить\" с пустым полем месяца")
    void shouldShowInvalidFormatErrorWhenMonthFieldIsEmpty() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), "", getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Месяц", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }


    @Test
    @DisplayName("TS-11.входные невалидные данные в разделе \"Купить\" с предыдущим месяцем в поле месяца")
    void shouldAllowPaymentWithPastMonth() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getMinusMonth(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.getNotificationOk();
        assertEquals(0, getPaymentRecordsCount());
/** тут баг */
    }

    @Test
    @DisplayName("TS-12.входные невалидные данные в разделе \"Купить\" со следующим месяцем в поле месяца")
    void shouldShowExpirationErrorWhenFutureMonthIsEntered() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getPlusMonth(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Месяц", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());
/** тут баг */
    }


    @Test
    @DisplayName("TS-13.входные невалидные данные в разделе \"Купить\" со спецсимволами в поле месяца")
    void shouldPreventInputOfSpecialCharactersInMonthField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getSymbol(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Месяц", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }


    @Test
    @DisplayName("TS-14.входные невалидные данные в разделе \"Купить\" с латиницой в поле месяца")
    void shouldPreventInputOfLatinLettersInMonthField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getValidOwner(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Месяц", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }


    @Test
    @DisplayName("TS-15.входные невалидные данные в разделе \"Купить\" с кирилиццой в поле месяца")
    void shouldPreventInputOfCyrillicCharactersInMonthField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getCyrillic(), getValidYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Месяц", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }


    @Test
    @DisplayName("TS-16.входные невалидные данные в разделе \"Купить\" поле года оставить пустым")
    void shouldShowInvalidFormatErrorWhenYearFieldIsEmpty() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getValidMonth(), "", getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Год", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }


    @Test
    @DisplayName("TS-17.входные невалидные данные в разделе \"Купить\" ввод прошедшего года в поле год")
    void shouldShowExpirationErrorWhenPastYearIsEntered() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getValidMonth(), getMinusYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Год", "Истёк срок действия карты");
        assertEquals(0, getPaymentRecordsCount());

    }

    @Test
    @DisplayName("TS-18.входные невалидные данные в разделе \"Купить\" ввести следующий год в поле года")
    void shouldShowInvalidFormatErrorWhenFutureYearIsEntered() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getValidMonth(), getPlusYear(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Год", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());
/** тут баг */
    }


    @Test
    @DisplayName("TS-19.входные невалидные данные в разделе \"Купить\" со спецсимолами в поле года")
    void shouldPreventInputOfSpecialCharactersInYearField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getValidMonth(), getSymbol(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Год", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }


    @Test
    @DisplayName("TS-20.входные невалидные данные в разделе \"Купить\" ввести латиницу в поле года")
    void shouldPreventInputOfLatinLettersInYearField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getValidMonth(), getValidOwner(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Год", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }


    @Test
    @DisplayName("TS-21.входные невалидные данные в разделе \"Купить\" ввести кириллицу в поле года")
    void shouldPreventInputOfCyrillicCharactersInYearField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard().getCardNumber(), getValidMonth(), getCyrillic(), getValidOwner(), getValidCvc());
        PaymentPage.fillForm(cardInfo);
        PaymentPage.checkFieldError("Год", "Неверный формат");
        assertEquals(0, getPaymentRecordsCount());

    }






}
