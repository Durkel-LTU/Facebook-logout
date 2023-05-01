import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;

public class Logback {

    private static final Logger logger = LoggerFactory.getLogger(Logback.class);


    /**
     * A set up method for Facebook login.
     * Sets up a chrome driver and directs the driver to designated website, disables drivers
     * settings and accepts cookies on website. Used on every startup of driver.
     * Returns true if setup is correct, otherwise false.
     */
    public static boolean setUp() {
        boolean state = false;


        return state;
    }

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting Logback");

        logger.debug("Debug message: Error ");

        logger.warn("This is a warning message");
        logger.error("This is an error message", new RuntimeException("Something went wrong"));

        logger.info("Exiting Logback");

        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\durim\\Downloads\\chromedriver\\chromedriver.exe");

        // Create an instance of ChromeOptions and add the desired options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");

        // Create a new instance of the ChromeDriver
        WebDriver driver = new ChromeDriver(options);

        // Set window size, so it shows desktop view of the website and not the mobile view.
        driver.manage().window().setSize(new Dimension(1500, 900));

        // Navigate to the Facebook login page
        driver.get("https://www.facebook.com/");

        if (driver.getCurrentUrl().contains("https://www.facebook.com/")) {
            logger.debug("URL is correct");
            // Find the button element by its data-testid attribute and click it
            WebElement button = driver.findElement(By.xpath("//button[@title='Allow all cookies']"));
            button.click();


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
            WebElement loginButton = driver.findElement(By.name("login"));
            loginButton.click();

            Thread.sleep(4000);

            // Wait for the dashboard page to load

            WebElement input = driver.findElement(By.xpath("//input[@placeholder='Search Facebook']"));
            Thread.sleep(2000);
            input.sendKeys("BlackPink\n");
            Thread.sleep(5000);


            // Click the round profile picture in the right corner
            WebElement profilePic = driver.findElement(By.xpath("//*[@aria-label='Your profile']"));
            profilePic.click();

            // Find the logout element and logout

            WebElement logoutButton = driver.findElement(By.xpath("//span[text()='Log Out']"));
            logoutButton.click();

            // Click on the "log out" button
            if(logoutButton.isDisplayed()) {
                logger.debug("Logout successful."); // Log debug message using logger
            } else {
                logger.error("Logout unsuccessful."); // Log error message using logger
            }
        } else {
            logger.error("URL in chrome driver is not set correctly!");
        }

        // Quit the driver to close the browser
        driver.quit();

    }

}
