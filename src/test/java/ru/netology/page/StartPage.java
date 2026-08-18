package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class StartPage {

    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement buyCreditButton = $(byText("Купить в кредит"));

    /**
     * Открывает форму для обычной покупки («Купить»)
     */
    public PaymentPage openPaymentFormForBuy() {
        buyButton.shouldBe(Condition.visible, Condition.enabled);
        buyButton.click();

        return new PaymentPage();

    }

    /**
     * Открывает форму для покупки в кредит («Купить в кредит»)
     */
    public PaymentPage openPaymentFormForCredit() {
        buyCreditButton.shouldBe(Condition.visible, Condition.enabled);
        buyCreditButton.click();
        return new PaymentPage();

    }

    // Вспомогательный метод: ждём, пока форма начнёт отрисовываться
    //  private void waitForForm() {
    //      $(".input__top").shouldBe(Condition.visible);
    // }
}