package ru.netology.Page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.Data.DataHelper;


import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {

    private SelenideElement findInputByLabel(String labelText) {
        return $$(".input__top")
                .filter(exactText(labelText))
                .first()
                .closest(".input")
                .$("input.input__control");
    }

    public PaymentPage fillForm(DataHelper.AuthInfo cardInfo, String holder, String month, String year, String cvc) {
        var cardNumberField = findInputByLabel("Номер карты");
        var monthField      = findInputByLabel("Месяц");
        var yearField       = findInputByLabel("Год");
        var ownerField      = findInputByLabel("Владелец");
        var cvcField        = findInputByLabel("CVC/CVV");

        cardNumberField.setValue(cardInfo.getLogin());
        ownerField.val(holder);
        monthField.val(month);
        yearField.val(year);
        cvcField.val(cvc);

        return this;
    }

    public PaymentPage fillWithApprovedData() {
        var card = DataHelper.getApprovedCard();
        var holderName = DataHelper.getValidCardHolderName();

        return fillForm(
                card,
                holderName,
                "08",
                "26",
                card.getPassword()
        );
    }

    public void submit() {
        // Вариант 1: по data-testid
        $(byText("Продолжить")).click();
        // Если нет такого атрибута — раскомментируй строку ниже и закомментируй выше:
        // $(byText("Продолжить")).click();
    }

    public String getSuccessMessage() {

        return $(".notification.notification_status_ok").getText();
    }
}