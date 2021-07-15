import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

public class SampleTest {
    protected static WebDriver driver;
    private final Logger logger = LogManager.getLogger(SampleTest.class);
    private static final String dnsAddress = "https://www.dns-shop.ru/";

    String env = System.getProperty("browser", "chrome");
    String option = System.getProperty("option","normal");

    @BeforeEach
    public void setUp() {
        logger.info("env = " + env);
        driver = WebDriverFactory.getDriver(env.toLowerCase(),option.toLowerCase());
        logger.info("Драйвер запущен");
    }

    @Test
    public void openPage() {
        driver.get(dnsAddress);
        if (env.equals("firefox")) {
            driver.manage().window().fullscreen();
        }
        logger.info("Открыта страница DNS - " + dnsAddress);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        WebElement elementChooseACity = driver.findElement(By.xpath("//a[@class='btn btn-additional']"));
        elementChooseACity.click();

        logger.info("Заголовок страницы: " + driver.getTitle());
        logger.info("Текущий URL: " + driver.getCurrentUrl());

        WebElement elementHomeAppliance = driver.findElement(By.xpath("//a[@class='ui-link menu-desktop__root-title']"));
        elementHomeAppliance.click();

        List<WebElement> elementsSubcategory = driver.findElements(By.xpath("//a[@class='subcategory__item ui-link ui-link_blue']"));
        for (WebElement element : elementsSubcategory) {
            logger.info("Категории: " + element.getText());
        }
        logger.info("------------------------------------------------------------------");

        GettingCookies.creationOfCookies(driver);

        GettingCookies.getCookieOutput(driver);

        waitingForAPage(10000);
    }

    @Test
    public void addingCookies(){
        driver.get(dnsAddress);
        if (env.equals("firefox")) {
            driver.manage().window().fullscreen();
        }
        logger.info("Открыта страница DNS - " + dnsAddress);

        GettingCookies.creationOfCookies(driver);

        waitingForAPage(5000);
    }

    @Test
    public void cookieOutput(){
        driver.get(dnsAddress);
        if (env.equals("firefox")) {
            driver.manage().window().fullscreen();
        }
        logger.info("Открыта страница DNS - " + dnsAddress);

        GettingCookies.getCookieOutput(driver);

        waitingForAPage(5000);
    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер остановлен");
        }
    }



    public void waitingForAPage(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
