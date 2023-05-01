import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Dimension;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchTest {

    private static WebDriver driver;

    /**
     * Sets up the chrome web driver and logs in on user account, to get access to the search function.
     */
    @BeforeClass
    public static void setUp() throws InterruptedException {
        // Set the path to the ChromeDriver executable
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

        // Read the Facebook credentials from a JSON file
        File jsonFile = new File("C:\\temp\\facebook.json");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonFile);

            // Find the email and password input fields and enter your credentials
            driver.findElement(By.id("email")).sendKeys(jsonNode.get("facebookCredentials").get("email").asText());
            driver.findElement(By.id("pass")).sendKeys(jsonNode.get("facebookCredentials").get("password").asText());

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Click the "Log In" button to submit the form
        driver.findElement(By.name("login")).click();

        // Wait for the dashboard page to load
        Thread.sleep(4000);
    }

    /**
     * Test unit for search function.
     * @throws InterruptedException - Throws exception if thread is interrupted when sleeping.
     */
    @Test
    public void testSearchFunction() throws InterruptedException {
        // Find the search input field and enter a query
        WebElement input = driver.findElement(By.xpath("//input[@placeholder='Search Facebook']"));

        input.sendKeys("Eminem\n");
        // Wait for the search results to load
        Thread.sleep(8000);

        // Assert that the page title contains the search query
        assertEquals("Eminem - Search Results | Facebook", driver.getTitle());

        // Wait for results to show before window closes
        Thread.sleep(4000);

        // Quit the driver to close the browser
        driver.quit();
    }

}
