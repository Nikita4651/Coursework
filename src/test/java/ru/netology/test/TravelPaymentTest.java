package ru.netology.test;



import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.Data.DataHelper.generateDate;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.netology.Data.DataHelper;

import ru.netology.Data.SQLHelper;

import ru.netology.Page.PaymentPage;
import ru.netology.Page.StartPage;




public class TravelPaymentTest {
   // LoginPage loginPage;
   // DataHelper.AuthInfo authInfo = DataHelper.getApprovedCard();


    @BeforeEach
    void setup() {
        //open("http://localhost:8080");
        open("http://localhost:8080");


    }

    @AfterEach
    @SneakyThrows
    void clearDb() {
        SQLHelper.cleanDatabase();
    }


 /*   @Test
    @DisplayName("P1 | Дебетовая карта: Успешная оплата во вкладке 'Купить'")
    void shouldMakeDebitPaymentSuccessfully() throws SQLException {
        open("/");

//var loginPage = LoginPage.selectBuyTab();
      //  var card = DataHelper.getApprovedCard();
      //  var holderName = DataHelper.generateValidCardHolderName();

        // 1. Переходим на форму оплаты через LoginPage
        DashboardPage dashboardPage = new LoginPage().openPaymentForm();


        //      $(byText("Купить в кредит")).click();
       // var authInfo = DataHelper.getApprovedCard();

        // $$("[maxlength='19']").first().setValue("1111222233334444");
        $$("[placeholder='08']").first().setValue("08");

        $$(".input-group__input-case").get(1).$("input.input__control").setValue("26");
        $$(".input-group__input-case").get(2).$("input.input__control").setValue("OSEPCHUK NIKITA");
        $$(".input-group__input-case").get(3).$("input.input__control").setValue("111");
      //  $$(".input-group__input-case").get(3).$("input.input__control").setValue(authInfo.getPassword());
        $(byText("Продолжить")).click();
        $(".notification.notification_status_ok")
                .shouldBe(visible, exactText("Успешно Операция одобрена Банком."));

    }  */

    @Test
    @DisplayName("P1 | Купить: успешная оплата картой")
    void shouldSuccessfullyPayWithBuyButton() {
        var card = DataHelper.getApprovedCard();
        var monthToAdd = 4;
        var firstMeetingDate = generateDate(monthToAdd);
        var yearToAddForSecondMeeting = 3;
        var secondMeetingDate = generateDate(yearToAddForSecondMeeting);
        var holderName = DataHelper.getValidCardHolderName();

        PaymentPage paymentPage = new StartPage().openPaymentFormForBuy();
        paymentPage.fillWithApprovedData();
        paymentPage.submit();

        String message = paymentPage.getSuccessMessage();
        System.out.println("Сообщение: " + message);
        assertTrue(message.contains("Операция одобрена"), "Ожидалось сообщение об успехе");
    }

    }
