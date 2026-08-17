package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;


import java.time.Duration;

import static com.codeborne.selenide.Condition.*;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class PaymentPage {


    private static SelenideElement findInputByLabel(String labelText) {
        return $$(".input__top")
                .filter(exactText(labelText))
                .first()
                .closest(".input")
                .$("input.input__control");
    }



    private static final SelenideElement cardNumberField = findInputByLabel("Номер карты");
    private static final SelenideElement monthField = findInputByLabel("Месяц");
    private static final SelenideElement yearField = findInputByLabel("Год");
    private static final SelenideElement ownerField = findInputByLabel("Владелец");
    private static final SelenideElement cvcField = findInputByLabel("CVC/CVV");

    private static final SelenideElement submit = $$("button").findBy(text("Продолжить"));
   // private static final SelenideElement submit = $(byText("Продолжить"));
    private static final SelenideElement notificationOk = $(".notification_status_ok");
    private static final SelenideElement notificationError = $(".notification_status_error");


    public static void fillForm(DataHelper.CardInfo cardInfo) {

        cardNumberField.setValue(cardInfo.getCardNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        ownerField.setValue(cardInfo.getOwner());
        cvcField.setValue(cardInfo.getCvc());
        //submit.click();

        submit.shouldBe(Condition.visible, Condition.enabled).click();


    }


    public static void getNotificationOk() {

        notificationOk.shouldBe(visible, Duration.ofSeconds(12))
                .shouldHave(text("Успешно"), text("Операция одобрена Банком."));
    }
    public static void getNotificationError() {

       notificationError.shouldBe(visible, Duration.ofSeconds(12))
                .shouldHave(text("Ошибка")); //, text("Ошибка! Банк отказал в проведении операции."));
    }

    public static void checkFieldError(String fieldName, String expectedText) {
        $$(".input__sub")
                .findBy(text(expectedText))
                .shouldBe(visible, Duration.ofSeconds(6));

    }

}
