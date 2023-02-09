import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage {
    @FindBy(css = "[data-test-id=login] input")
    private SelenideElement loginField;
    @FindBy(css = "[data-test-id=password] input")
    private  SelenideElement passwordField;
    @FindBy(css = "[data-test-id=action-login] input")
    private SelenideElement loginButton;

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginInput.setValue(info.getLogin());
        passwordInput.setValue(info.getPassword());
        buttonActionLogin.click();
        return new VerificationPage();
    }
}
