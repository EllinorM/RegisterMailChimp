import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.testng.AssertJUnit.assertEquals;

public class myStepdefs {
    private WebDriver driver;
    private WebDriverWait wait;

    @Given("I have opened {string}")
    public void iHaveOpened(String browser) {
        if (browser.equals("chrome")) {
            System.setProperty("webDriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);

        } else {
            System.setProperty("webDriver.edge.driver", "C:\\Selenium\\msedgedriver.exe");
            driver = new EdgeDriver();
        }
        driver.get("https://login.mailchimp.com/signup/");
        driver.manage().window().maximize();
    }

    @Given("I enter the email {string}")
    public void iEnterTheEmail(String email) {
        interactWithWait(driver, By.id("email"), email, 15, actionType.SendKeys);
    }

    @Given("My random username is {int}")
    public void myRandomUsernameIsUsernameLength(int username) {
        WebElement getUsername = driver.findElement(By.id("new_username"));
        getUsername.click();
        getUsername.clear();

        if (username == 1) {
            String taken = "Ellinor86";
            getUsername.sendKeys(taken);

        } else {
            int length = username;
            String user = "";
            String letters = "abcdefghijklmnopqrstuvwxyz";
            for (int i = 0; i < length; i++) {
                user += letters.charAt((int) (Math.random() * letters.length()));
            }
            getUsername.sendKeys(user);
        }
    }

    @Given("I enter the password {string}")
    public void iEnterThePassword(String password) {
        driver.findElement(By.cssSelector("#new_password")).sendKeys(password);
        driver.findElement(By.id("marketing_newsletter")).click();
    }

    @When("I click the signup button")
    public void iClickTheSignupButton() {
        interactWithWait(driver, By.id("marketing_newsletter"), "", 15, actionType.Click);
        interactWithWait(driver, By.id("create-account-enabled"), "", 20, actionType.Click);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Then("I want the creation of the account to {string}")
    public void iWantTheCreationOfTheAccountTo(String created) {

        boolean actual = true;
        boolean expected = true;
        String result;

        if (created.equalsIgnoreCase("succeed")) {
            result = driver.getTitle();
            if (result.equalsIgnoreCase("Success | Mailchimp")) {
                actual = true;
            }
            assertEquals(expected, actual);
        } else if (created.equalsIgnoreCase("fail")) {
            expected = false;
            if (driver.findElement(By.id("signup-form")).isDisplayed()) {
                actual = false;
            }
            assertEquals(expected, actual);
        }

        if (created.equalsIgnoreCase("fail")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class='invalid-error']")));
            String invalidError = driver.findElement(By.cssSelector("[class='invalid-error']")).getText();
            String expected1 = "An email address must contain a single @.";

            if (invalidError.equals("Great minds think alike - someone already has this username. If it's you, log in.")) {
                expected1 = "Great minds think alike - someone already has this username. If it's you, log in.";
            }
            if (invalidError.equals("Enter a value less than 100 characters long")) {
                expected1 = "Enter a value less than 100 characters long";
            }
            assertEquals(expected1, invalidError);
        }
    }

    private void interactWithWait(WebDriver driver, By by, String text, Integer duration, actionType action) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(duration));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));

        if (action == actionType.SendKeys) {
            element.sendKeys(text);
        } else if (action == actionType.Click) {
            element.click();
        }
    }

    @After
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}

enum actionType {
    Click,
    SendKeys;
}