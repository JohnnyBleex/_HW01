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
import java.util.Set;

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
    public void openPageDnsAndFinedSmartPhone() {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        driver.get(dnsAddress);
        logger.info("Открыта страница DNS - " + dnsAddress);

        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));

        // Подтвердить город
        WebElement elementChooseACity = driver.findElement(By.xpath("//a[@class='btn btn-additional']"));
        elementChooseACity.click();

        // Навестись на смартфоны и гаджеты что бы открылось меню
        WebElement smartphonesAndGadgets = driver.findElement(By.xpath("//div/a[text()='Смартфоны и гаджеты']"));
        actions.moveToElement(smartphonesAndGadgets).perform();

        // Нажать на ссылку смартфоны
        By smartphonesXpath = By.xpath("//div/a[text()='Смартфоны']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(smartphonesXpath));
        WebElement smartphones = driver.findElement(smartphonesXpath);
        smartphones.click();

        // Сделать скриншот
        getScreenShot("screens\\1_ScreenPageSmartphones.png");

        // Пролистать страницу вниз
        js.executeScript("window.scrollBy(0,1000);");

        // Выбрать в филтре производитель Samsung
        By samsungCheckBoxXpath = By.xpath("//span[text()='Samsung  ']");
        WebElement samsungCheckBox = driver.findElement(samsungCheckBoxXpath);
        wait.until(ExpectedConditions.visibilityOfElementLocated(samsungCheckBoxXpath));
        samsungCheckBox.click();

        // Пролистать страницу вниз
        js.executeScript("window.scrollBy(0,500);");

        // Открыть выподайщее меню объем оперативной памяти
        By ramSizeFilterXpath = By.xpath("//a/span[text()='Объем оперативной памяти']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(ramSizeFilterXpath));
        WebElement ramSizeFilter = driver.findElement(ramSizeFilterXpath);
        ramSizeFilter.click();

        // Выбрать в филтре оперативной памяти 8 гб
        By ramSizeCheckBoxXpath = By.xpath("//span[text()='8 Гб  ']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(ramSizeCheckBoxXpath));
        WebElement ramSizeCheckBox = driver.findElement(ramSizeCheckBoxXpath);
        ramSizeCheckBox.click();

        // Пролистать страницу вниз
        js.executeScript("window.scrollBy(0,500);");

        // Нажать кнопку применить
        WebElement confirmButton = driver.findElement(By.xpath("//button[@data-role='filters-submit']"));
        confirmButton.click();

        // Открыть выподающее меню сортировки
        By sortElementXpath = By.xpath("//a/span[text()='Сначала недорогие']");
        wait.until(ExpectedConditions.elementToBeClickable(sortElementXpath));
        WebElement sortElement = driver.findElement(sortElementXpath);
        actions.moveToElement(sortElement)
                .click()
                .perform();

        // Выбрать сортировку по цене сначало дорогие
        By sortDearFirstXpath = By.xpath("//span[text()='Сначала дорогие']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(sortDearFirstXpath));
        WebElement sortDearFirst = driver.findElement(sortDearFirstXpath);
        sortDearFirst.click();

        By firstElementXpath = By.xpath("//div[@class='products-list__content']/div[2]/div[1]/a");
        wait.until(ExpectedConditions.elementToBeClickable(firstElementXpath));

        // Сделать скриншот
        getScreenShot("screens\\2_ScreenPageSmartphonesSortDearFirst.png");

        Set<String> oldWindowSet = driver.getWindowHandles();

        WebElement firstElement = driver.findElement(firstElementXpath);
        actions.keyDown(Keys.SHIFT)
                .click(firstElement)
                .keyUp(Keys.SHIFT)
                .perform();

        Set<String> newWindowSet = driver.getWindowHandles();
        newWindowSet.removeAll(oldWindowSet);
        String newWindow = newWindowSet.iterator().next();
        driver.switchTo().window(newWindow);

        getScreenShot("screens\\3_ScreenNewPage.png");

        waitingForAPage(5);

    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер остановлен");
        }
    }

    public void getScreenShot(String path) {
        try {
            Screenshot screenshot = new AShot().takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File(path));
            logger.info("Скриншот сохранен в папку screens");
        } catch (IOException exception) {
            exception.printStackTrace();
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
