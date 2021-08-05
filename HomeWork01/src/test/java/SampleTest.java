import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Sleeper;

import java.time.Duration;
import java.util.List;

public class SampleTest {
    protected static WebDriver driver;
    private final Logger logger = LogManager.getLogger(SampleTest.class);
    private static final String dnsAddress = "https://www.dns-shop.ru/";

    String browser = System.getProperty("browser", "chrome");
    String option = System.getProperty("option", "normal");

    @BeforeEach
    public void setUp() {
        logger.info("Браузер = " + browser);
        logger.info("Стратегия загрузки страници - " + option);
        driver = WebDriverFactory.getDriver(browser.toLowerCase(), option.toLowerCase());
        logger.info("Драйвер запущен");
    }

    @Test
    public void openPage() {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        driver.get(dnsAddress);
        logger.info("Открыта страница DNS - " + dnsAddress);

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

        waitingForAPage(10);
    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер остановлен");
        }
    }


    public void waitingForAPage(int seconds) {
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
