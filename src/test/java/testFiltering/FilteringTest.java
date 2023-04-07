package testFiltering;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.bytebuddy.asm.Advice;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class FilteringTest {
    private final String BASE_URL = "https://devexpress.github.io/devextreme-reactive/react/grid/docs/guides/filtering/";
    private final Long IMPLICITLY_WAIT_SECONDS = 10L;
    private final Long ONE_SECOND_DELAY = 1000L;
    private WebDriver driver;

    // Overload
    private void presentationSleep() {
        presentationSleep(1);
    }

    private void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY); // For Presentation ONLY
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    @BeforeSuite
//    public void beforeSuite() {
//        WebDriverManager.chromedriver().setup();
//    }

    @BeforeClass
    public void beforeClass() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        driver.manage().window().maximize();
        driver.get(BASE_URL);
        presentationSleep();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
          if (driver != null) {
            driver.quit();
        }
    }

 //   @BeforeMethod
//    public void beforeMethod() {
//        driver.get(BASE_URL);
//        presentationSleep();
//
//    }

    @Test
    public void testFiltering() {
        WebElement iUnderstand = driver.findElement(By.xpath("//button[contains(text(),'I Understand')]"));
        iUnderstand.click();
        WebElement iframeElement = driver.findElement(By.xpath("//h3[@id='uncontrolled-mode']//following-sibling::div[1]//iframe"));
        driver.switchTo().frame(iframeElement);

        // Вводимо значення у фільтр
        WebElement filterInput = driver.findElement(By.xpath("(//div[@class='TableFilterCell-flexContainer']/div/input)[3]"));
        Actions action = new Actions(driver);
        action.moveToElement(filterInput).perform();
        presentationSleep(2);
        filterInput.sendKeys("L");
        filterInput.sendKeys(Keys.ENTER);

        // Перевіряємо наявність міст Las Vegas та London
        WebElement lasVegasCell = driver.findElement(By.xpath("(//td[contains(text(), 'Las Vegas')])[1]"));
        Assert.assertTrue(lasVegasCell.isDisplayed(), "Las Vegas city is not displayed");

        WebElement lasVegasCell2 = driver.findElement(By.xpath("(//td[contains(text(), 'Las Vegas')])[2]"));
        Assert.assertTrue(lasVegasCell2.isDisplayed(), "Las Vegas city is not displayed");

        WebElement londonCell = driver.findElement(By.xpath("//td[contains(text(), 'London')]"));
        Assert.assertTrue(londonCell.isDisplayed(), "London city is not displayed");
    }

//    @AfterTest
//    public void tearDown() {
//        // Закриваємо вікно браузера
//        driver.quit();
//    }
}