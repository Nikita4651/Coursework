package ru.netology.test;


import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static ru.netology.data.DataHelper.*;



import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;


import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentPage;
import ru.netology.page.StartPage;


public class TravelPaymentTest {


    private PaymentPage paymentPage;




    @BeforeEach
    void setup() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        open("http://localhost:8080");


    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    @SneakyThrows
    void clearDb() {
        SQLHelper.cleanDatabase();

    }
@AfterEach
        void Ob() {
    Selenide.refresh();
}

    @Test
    @DisplayName("TS-1.Входные валидные данные в раздел \"Купить\"")
    void shouldSuccessfullyPayWithBuyButton() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var CardInfo = DataHelper.getApprovedCard();
        paymentPage.fillForm(CardInfo);
        paymentPage.getNotificationOk();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());


    }

    @SneakyThrows
    @Test
    @DisplayName("TS-2.Входные валидные данные в раздел \"Купить в кредит\"")
    void shouldSuccessfullyPayWithBuyOnCreditButton() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForCredit();
        var CardInfo = DataHelper.getApprovedCard();
        paymentPage.fillForm(CardInfo);
        paymentPage.getNotificationOk();
        assertEquals("APPROVED", SQLHelper.getCreditPaymentStatus());


    }


    @Test
    @DisplayName("TS-3.входные невалидные данные в разделе \"Купить\" пустые поля:")
    void shouldShowInvalidFormatErrorsWhenAllFieldsAreEmpty() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo("", "", "", "", "");
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Номер карты", "Неверный формат");
        paymentPage.checkFieldError("Месяц", "Неверный формат");
        paymentPage.checkFieldError("Год", "Неверный формат");
        paymentPage.checkFieldError("Владелец", "Поле обязательно для заполнения");
        paymentPage.checkFieldError("CVC/CVV", "Неверный формат");




    }

    @Test
    @DisplayName("TS-4.входные невалидные данные в разделе \"Купить\" с пустым полем карты")
    void shouldShowInvalidFormatErrorWhenCardFieldIsEmpty() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(""
                , getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Номер карты", "Неверный формат");



    }

    @Test
    @DisplayName("TS-5.входные невалидные данные в разделе \"Купить\" поля карты")
    void shouldFailPaymentWhenCardNumberIsInvalid() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var CardInfo = DataHelper.getDeclinedCard();
        paymentPage.fillForm(CardInfo);
        paymentPage.getNotificationError();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());

    }

    @Test
    @DisplayName("TS-6.входные невалидные данные в разделе \"Купить\" с заполнением не полным номером карты")
    void shouldShowInvalidFormatErrorWhenCardNumberIsIncomplete() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();

        var cardInfo = new DataHelper.CardInfo(DataHelper
                .getNumbers(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Номер карты", "Неверный формат");


    }

    @Test
    @DisplayName("TS-7.входные невалидные данные в разделе \"Купить\" с использованием спецсимволов в поле карты")
    void shouldShowInvalidFormatErrorWhenCardFieldContainsSpecialCharacters() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(DataHelper
                .getSymbol(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Номер карты", "Неверный формат");


    }


    @Test
    @DisplayName("TS-8.входные невалидные данные в разделе \"Купить\" с использованием латинских букв в поле карты")
    void shouldShowInvalidFormatErrorWhenCardFieldContainsLatinLetters() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(DataHelper
                .getValidOwner(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Номер карты", "Неверный формат");


    }


    @Test
    @DisplayName("TS-9.входные невалидные данные в разделе \"Купить\" с использованием кириллицы в поле карты")
    void shouldShowInvalidFormatErrorWhenCardFieldContainsCyrillicCharacters() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(DataHelper
                .getCyrillic(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Номер карты", "Неверный формат");


    }


    @Test
    @DisplayName("TS-10.входные невалидные данные в разделе \"Купить\" с пустым полем месяца")
    void shouldShowInvalidFormatErrorWhenMonthFieldIsEmpty() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), "", getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Месяц", "Неверный формат");


    }


    @Test
    @DisplayName("TS-11.входные невалидные данные в разделе \"Купить\" с предыдущим месяцем в поле месяца")
    void shouldAllowPaymentWithPastMonth() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getMinusMonth(), getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.getNotificationOk();

/** тут баг */
    }

    @Test
    @DisplayName("TS-12.входные невалидные данные в разделе \"Купить\" со следующим месяцем в поле месяца")
    void shouldShowExpirationErrorWhenFutureMonthIsEntered() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getPlusMonth(), getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Месяц", "Неверный формат");

/** тут баг */
    }


    @Test
    @DisplayName("TS-13.входные невалидные данные в разделе \"Купить\" со спецсимволами в поле месяца")
    void shouldPreventInputOfSpecialCharactersInMonthField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getSymbol(), getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Месяц", "Неверный формат");


    }


    @Test
    @DisplayName("TS-14.входные невалидные данные в разделе \"Купить\" с латиницой в поле месяца")
    void shouldPreventInputOfLatinLettersInMonthField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidOwner(), getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Месяц", "Неверный формат");


    }


    @Test
    @DisplayName("TS-15.входные невалидные данные в разделе \"Купить\" с кирилиццой в поле месяца")
    void shouldPreventInputOfCyrillicCharactersInMonthField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getCyrillic(), getValidYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Месяц", "Неверный формат");


    }


    @Test
    @DisplayName("TS-16.входные невалидные данные в разделе \"Купить\" поле года оставить пустым")
    void shouldShowInvalidFormatErrorWhenYearFieldIsEmpty() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), "", getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Год", "Неверный формат");


    }


    @Test
    @DisplayName("TS-17.входные невалидные данные в разделе \"Купить\" ввод прошедшего года в поле год")
    void shouldShowExpirationErrorWhenPastYearIsEntered() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getMinusYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Год", "Истёк срок действия карты");


    }

    @Test
    @DisplayName("TS-18.входные невалидные данные в разделе \"Купить\" ввести следующий год в поле года")
    void shouldShowInvalidFormatErrorWhenFutureYearIsEntered() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getPlusYear(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Год", "Неверный формат");

/** тут баг */
    }


    @Test
    @DisplayName("TS-19.входные невалидные данные в разделе \"Купить\" со спецсимолами в поле года")
    void shouldPreventInputOfSpecialCharactersInYearField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getSymbol(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Год", "Неверный формат");


    }


    @Test
    @DisplayName("TS-20.входные невалидные данные в разделе \"Купить\" ввести латиницу в поле года")
    void shouldPreventInputOfLatinLettersInYearField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getValidOwner(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Год", "Неверный формат");


    }


    @Test
    @DisplayName("TS-21.входные невалидные данные в разделе \"Купить\" ввести кириллицу в поле года")
    void shouldPreventInputOfCyrillicCharactersInYearField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getCyrillic(), getValidOwner(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Год", "Неверный формат");


    }


    @Test
    @DisplayName("TS-22.входные невалидные данные в разделе \"Купить\" поле владелец оставить пустым")
    void shouldShowInvalidFormatErrorWhenOwnerFieldIsEmpty() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getValidYear(), "", getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Владелец", "Поле обязательно для заполнения");


    }


    @Test
    @DisplayName("TS-23.входные невалидные данные в разделе \"Купить\" ввести кириллицу в поле владелеца")
    void shouldShowInvalidFormatErrorWhenOwnerFieldContainsCyrillic() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getValidYear(), getCyrillic(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Владелец", "Неверный формат");

/** тут баг */
    }


    @Test
    @DisplayName("TS-24.входные невалидные данные в разделе \"Купить\" ввести в поле цифры в поле владельца")
    void shouldShowInvalidFormatErrorWhenOwnerFieldContainsDigits() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getValidYear(), getNumbers(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Владелец", "Неверный формат");

/** тут баг */
    }


    @Test
    @DisplayName("TS-25.входные невалидные данные в разделе \"Купить\" ввести в поле спецсиволы в поле владелеца")
    void shouldShowInvalidFormatErrorWhenOwnerFieldContainsSpecialCharacters() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getValidYear(), getSymbol(), getValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("Владелец", "Неверный формат");

/** тут баг */
    }


    @Test
    @DisplayName("TS-26.входные невалидные данные в разделе \"Купить\" оставить  поле пустым cvc")
    void shouldShowInvalidFormatErrorWhenCvcFieldIsEmpty() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), "");
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("CVC/CVV", "Неверный формат");


    }


    @Test
    @DisplayName("TS-27.входные невалидные данные в разделе \"Купить\" ввести в поле cvc меньше 3 значного кода")
    void shouldShowInvalidFormatErrorWhenCvcIsLessThanThreeDigits() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getInValidCvc());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("CVC/CVV", "Неверный формат");


    }


    @Test
    @DisplayName("TS-28.входные невалидные данные в разделе \"Купить\" в поле cvc ввод спецсимволов")
    void shouldPreventInputOfSpecialCharactersInCvcField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getSymbol());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("CVC/CVV", "Неверный формат");


    }


    @Test
    @DisplayName("TS-29.входные невалидные данные в разделе \"Купить\" в поле cvc ввод латинских букв")
    void shouldPreventInputOfLatinLettersInCvcField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidOwner());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("CVC/CVV", "Неверный формат");


    }


    @Test
    @DisplayName("TS-30.входные невалидные данные в разделе \"Купить\" в поле cvc ввод кириллицу")
    void shouldPreventInputOfCyrillicCharactersInCvcField() {
        StartPage startPage = new StartPage();
        startPage.openPaymentFormForBuy();
        var cardInfo = new DataHelper.CardInfo(getApprovedCard()
                .getCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getCyrillic());
        paymentPage.fillForm(cardInfo);
        paymentPage.checkFieldError("CVC/CVV", "Неверный формат");


    }


}
