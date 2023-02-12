package test;

import data.DataHelper;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import data.DataHelper;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

import org.junit.jupiter.api.Assertions;


public class MoneyTransferTestPositive {

    @BeforeEach
    void setupTest() {
        val LoginPage = open("http://localhost:9999", LoginPage.class);
    }


    @Test
    void shouldTransferMoney() {
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var cardNumber1 = DataHelper.getCardNumber1();
        var cardNumber2 = DataHelper.getCardNumber2();
        var balanceCardNumber1 = dashboardPage.getCardBalance(cardNumber1.getIndex());
        var balanceCardNumber2 = dashboardPage.getCardBalance(cardNumber2.getIndex());
        var transferAmount = DataHelper.generateValidAmount(balanceCardNumber2);
        var expectedBalanceCardNumber1 = balanceCardNumber1 + transferAmount;
        var expectedBalanceCardNumber2 = balanceCardNumber2 - transferAmount;
        var replenishmentPage = dashboardPage.transferTo(cardNumber1.getIndex());
        dashboardPage = replenishmentPage.validTransfer(String.valueOf(transferAmount), cardNumber2);
        var actualBalanceCardNumber1 = dashboardPage.getCardBalance(cardNumber1.getIndex());
        var actualBalanceCardNumber2 = dashboardPage.getCardBalance(cardNumber2.getIndex());
        Assertions.assertEquals(expectedBalanceCardNumber1, actualBalanceCardNumber1);
        Assertions.assertEquals(expectedBalanceCardNumber2, actualBalanceCardNumber2);
    }

    @Test
    void shouldErrorAmount() {
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = LoginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var cardNumber1 = DataHelper.getCardNumber1();
        var cardNumber2 = DataHelper.getCardNumber2();
        var balanceCardNumber1 = dashboardPage.getCardBalance(cardNumber1.getIndex());
        var balanceCardNumber2 = dashboardPage.getCardBalance(cardNumber2.getIndex());
        var transferAmount = DataHelper.generateInvalidAmount(balanceCardNumber1);
        var replenishmentPage = dashboardPage.transferTo(cardNumber2.getIndex());
        replenishmentPage.transferAmount(String.valueOf(transferAmount), cardNumber1);
        replenishmentPage.findErrorMessage("Ошибка! На Вашем счёте недостаточно средств для перевода!");
        Assertions.assertEquals(dashboardPage.getCardBalance(cardNumber1.getIndex()), balanceCardNumber1);
        Assertions.assertEquals(dashboardPage.getCardBalance(cardNumber2.getIndex()), balanceCardNumber2);
    }

}