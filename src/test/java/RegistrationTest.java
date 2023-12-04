import api.Login;
import api.User;
import api.UserSteps;
import config.WebDriverFactory;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pageobject.LoginPage;
import pageobject.MainPage;
import pageobject.RegistrationPage;

public class RegistrationTest {
    private WebDriver driver;
    private User user;
    private RegistrationPage registrationPage;

    @Before
    public void setUp() {
        String browserName = System.getProperty("browserName");
        driver = WebDriverFactory.get(browserName);
        user = new User(RANDOM_EMAIL, RANDOM_PASSWORD, RANDOM_NAME);
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.open();
    }

    @Test
    @DisplayName("Проверка регистрации со страницы регистрации")
    public void checkRegistrationFromRegistrationPageSuccess() {
        registrationPage.registerUser(user);
        registrationPage.clickRegister();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmailAndPassword(user);
        loginPage.clickLoginButton();
        MainPage mainPage = new MainPage(driver);
        Assert.assertTrue(mainPage.isOrderButtonDisplayed());
    }

    @Test
    @DisplayName("Проверка регистриции со страницы авторизации")
    public void checkRegistrationFromLoginPageSuccess() {
        loginPage.clickRegisterButton();
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.registerUser(user);
        registrationPage.clickRegister();
        loginPage.enterEmailAndPassword(user);
        loginPage.clickLoginButton();
        MainPage mainPage = new MainPage(driver);
        Assert.assertTrue(mainPage.isOrderButtonDisplayed());
    }

    @Test
    @DisplayName("Проверка регистрации с неправильным паролем")
    public void checkRegistrationWithWrongPassError() {
        registrationPage.registerUser(user);
        Assert.assertTrue(registrationPage.isWrongPasswordDisplayed());
    }
    @After
    public void tearDown() {
        UserSteps userSteps = new UserSteps();
        Login credentials = new Login(user.getEmail(), user.getPassword());
        ValidatableResponse responseLogin = userSteps.login(credentials);
        String accessToken = userSteps.getAccessToken(responseLogin);
        userSteps.deletingUsersAfterTests(accessToken);
        driver.quit();
    }

}

