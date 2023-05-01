import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertTrue;

public class LoginTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        // Set up the driver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\durim\\Downloads\\chromedriver\\chromedriver.exe");

        // Create an instance of ChromeOptions and add the desired options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");

        // Create a new instance of the ChromeDriver
        driver = new ChromeDriver(options);

        // Set window size, so it shows desktop view of the website and not the mobile view.
        driver.manage().window().setSize(new Dimension(1500, 900));

        // Navigate to the Facebook login page
        driver.get("https://www.facebook.com/");

        // Find the button element by its data-testid attribute and click it
        driver.findElement(By.xpath("//button[@title='Allow all cookies']")).click();
    }

    /**
     * Test login credentials on facebook from local file.
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void testCorrectLogin() throws InterruptedException, IOException {

        // Read credentials from a JSON file
        File jsonFile = new File("C:\\temp\\facebook.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonFile);

        String email = jsonNode.get("facebookCredentials").get("email").asText();
        String password = jsonNode.get("facebookCredentials").get("password").asText();

        // Find the email and password input fields and enter your credentials
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.clear();
        emailField.sendKeys(email);
        WebElement passwordField = driver.findElement(By.id("pass"));
        passwordField.clear();
        passwordField.sendKeys(password);

        // Click the "Log In" button to submit the form
        WebElement loginButton = driver.findElement(By.name("login"));
        loginButton.click();

        Thread.sleep(4000);

        // Check if user is redirected to the correct page after successful login
        assertTrue(driver.getTitle().contains("Facebook"));

        // Check if user is logged in successfully by finding "your profile" label in website.
        // Having your profile indicates that the login has been successful.
        WebElement profilePic = driver.findElement(By.xpath("//*[@aria-label='Your profile']"));
        assertTrue(profilePic.isDisplayed());

    }

    /**
     * Test login by entering invalid credentials.
     *
     * @throws InterruptedException - If thread is interrupted while sleeping, throw exception
     * @throws IOException - If file does not exist, throw exception
     */
    @Test
    public void testIncorrectLogin() throws InterruptedException, IOException {
        // Navigate to the Facebook login page
        driver.get("https://www.facebook.com/");

        // Accept cookies
        WebElement cookieButton = driver.findElement(By.xpath("//button[@title='Allow all cookies']"));
        cookieButton.click();

        String email = "invalidemail@example.com";
        String password = "invalidpassword";

        // Find the email and password input fields and enter invalid credentials
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.clear();
        emailField.sendKeys(email);
        WebElement passwordField = driver.findElement(By.id("pass"));
        passwordField.clear();
        passwordField.sendKeys(password);

        // Click the "Log In" button to submit the form
        WebElement loginButton = driver.findElement(By.name("login"));
        loginButton.click();

        Thread.sleep(2000);

        // Check if user is redirected to the correct page after successful login
        assertTrue(driver.getTitle().contains("Log into Facebook"));
    }

    /**
     * Tests are done, we can close the driver.
     */
    @After
    public void cleanUp() {
        driver.quit();
    }

}
