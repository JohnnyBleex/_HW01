import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
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

    @Test
    public void openPageDnsAndFinedSmartPhone(){
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        driver.get(dnsAddress);
        logger.info("Открыта страница DNS - " + dnsAddress);

        WebElement elementChooseACity = driver.findElement(By.xpath("//a[@class='btn btn-additional']"));
        elementChooseACity.click();

        WebElement smartphonesAndGadgets = driver.findElement(By.xpath("//div/a[text()='Смартфоны и гаджеты']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(smartphonesAndGadgets).perform();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        By smartphonesXpath = By.xpath("//div/a[text()='Смартфоны']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(smartphonesXpath));

        WebElement smartphones = driver.findElement(smartphonesXpath);
        smartphones.click();

        try {
            Screenshot screenshot = new AShot().takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("screens\\ASViewablePage.png"));
            logger.info("Скриншот сохранен в папку screens");
        }catch (IOException exception){
            exception.printStackTrace();
        }
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
