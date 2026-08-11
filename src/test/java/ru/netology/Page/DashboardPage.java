package ru.netology.Page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.Data.DataHelper;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private SelenideElement findInputByLabel(String labelText) {
        return $$(".input__top")
                .filter(exactText(labelText))
                .first()
                .closest(".input")
                .$("input.input__control");
    }


    private final SelenideElement cardNumberField = findInputByLabel("Номер карты");
    private final SelenideElement monthField      = findInputByLabel("Месяц").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    private final SelenideElement yearField       = findInputByLabel("Год").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    private final SelenideElement ownerField      = findInputByLabel("Владелец");
    private final SelenideElement cvcField        = findInputByLabel("CVC/CVV");

    public DashboardPage fillForm(DataHelper.AuthInfo info, String holder, String month, String year, String cvc) {
        SelenideElement cardNumber;
        cardNumberField.setValue(info.getLogin());
        ownerField.val(holder.toUpperCase());
        monthField.val(month);
        yearField.val(year);
        cvcField.val(cvc);
        return this;
    }

    public DashboardPage fillWithApprovedData() {
    var card = DataHelper.getApprovedCard();
        long shift;
        return fillForm(
        card,
        DataHelper.getValidCardHolderName(),
            LocalDate.now()
                    .plusDays(shift)
                    .format(DateTimeFormatter.ofPattern("MM.yyyy")),
        card.getPassword()
    );
    }

}
