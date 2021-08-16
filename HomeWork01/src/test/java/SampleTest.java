import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.*;
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

        waitingForAPage(5);
    }

    @Test
    public void openPageDnsAndFinedSmartPhone() {
        SeleniumListener listener = new SeleniumListener();
        WebDriver eventDriver = new EventFiringDecorator(listener).decorate(driver);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        eventDriver.get(dnsAddress);
        logger.info("Открыта страница DNS - " + dnsAddress);

        Actions actions = new Actions(eventDriver);
        JavascriptExecutor js = (JavascriptExecutor) eventDriver;
        WebDriverWait wait = new WebDriverWait(eventDriver, Duration.ofSeconds(10));
        FluentWait<WebDriver> fluentWait = new FluentWait<>(eventDriver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(10))
                .ignoring(StaleElementReferenceException.class);

        // Подтвердить город
        WebElement elementChooseACity = eventDriver.findElement(By.xpath("//a[@class='btn btn-additional']"));
        elementChooseACity.click();

        // Навестись на смартфоны и гаджеты что бы открылось меню
        WebElement smartphonesAndGadgets = driver.findElement(By.xpath("//div/a[text()='Смартфоны и гаджеты']"));
        actions.moveToElement(smartphonesAndGadgets)
                .perform();

        // Нажать на ссылку смартфоны
        By smartphonesXpath = By.xpath("//div/a[text()='Смартфоны']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(smartphonesXpath));
        WebElement smartphones = eventDriver.findElement(smartphonesXpath);
        smartphones.click();

        // Сделать скриншот
        getScreenShot("screens\\1_ScreenPageSmartphones.png");

        // Пролистать страницу вниз до элемента
        WebElement elementBrand = driver.findElement(By.xpath("//div[@data-id='brand']"));
        js.executeScript("arguments[0].scrollIntoView(true);", elementBrand);
        // Выбрать в филтре производитель Samsung
        By samsungCheckBoxXpath = By.xpath("//span[text()='Samsung  ']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(samsungCheckBoxXpath));
        WebElement samsungCheckBox = eventDriver.findElement(samsungCheckBoxXpath);
        samsungCheckBox.click();

        // Открыть выподайщее меню объем оперативной памяти
        By ramSizeFilterXpath = By.xpath("//a/span[text()='Объем оперативной памяти']");
        WebElement ramSizeFilter = eventDriver.findElement(ramSizeFilterXpath);
        wait.until(ExpectedConditions.elementToBeClickable(ramSizeFilter));
        ramSizeFilter.click();

        // Пролистать вниз до элемента (Кнопка применить)
        WebElement confirmButton = eventDriver.findElement(By.xpath("//button[@data-role='filters-submit']"));
        js.executeScript("arguments[0].scrollIntoView(true);", confirmButton);

        // Выбрать в филтре оперативной памяти 8 гб
        By ramSizeCheckBoxXpath = By.xpath("//span[text()='8 Гб  ']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(ramSizeCheckBoxXpath));
        WebElement ramSizeCheckBox = eventDriver.findElement(ramSizeCheckBoxXpath);
        ramSizeCheckBox.click();

        // Нажать кнопку применить
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        confirmButton.click();

        // Открыть выподающее меню сортировки
        By sortElementXpath = By.xpath("//div[@data-id='order']");
        WebElement sortElement = eventDriver.findElement(sortElementXpath);
        js.executeScript("arguments[0].scrollIntoView(false);", sortElement); // Скрипт с котором работает хром, но не работает FireFox
        fluentWait.until(ExpectedConditions.visibilityOf(sortElement));
        sortElement.click();

        // Выбрать сортировку по цене "сначало дорогие"
        By sortDearFirstXpath = By.xpath("//span[text()='Сначала дорогие']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(sortDearFirstXpath));
        WebElement sortDearFirst = eventDriver.findElement(sortDearFirstXpath);
        sortDearFirst.click();

        // Подождать пока прогрузится список элементов
        By listOfItems = By.xpath("//div[@class='products-list__content']");
        fluentWait.until(ExpectedConditions.elementToBeClickable(listOfItems));

/*        // Найти список элементов
        By firstElementXpath = By.xpath("//div[@data-id='product']");
        List<WebElement> firstElements = driver.findElements(firstElementXpath);
        wait.until(ExpectedConditions.elementToBeClickable(firstElements.get(0)));
        // Сделать скриншот
        getScreenShot("screens\\2_ScreenPageSmartphonesSortDearFirst.png");
        // Нажать на первый элемент в списке
        actions.moveToElement(firstElements.get(0), 0, -70)
                .keyDown(Keys.SHIFT)
                .click()
                .keyUp(Keys.SHIFT)
                .perform();*/

        By firstElementXpath = By.xpath("//div[@data-id='product']/a");
        List<WebElement> firstElements = driver.findElements(firstElementXpath);
        fluentWait.until(ExpectedConditions.elementToBeClickable(firstElements.get(0)));
        getScreenShot("screens\\2_ScreenPageSmartphonesSortDearFirst.png");
        firstElements.get(0).click();

        // Переключится на новое окно
        try {
/*            Set<String> newWindowSet = eventDriver.getWindowHandles();
            String newWindow = newWindowSet.iterator().next();
            eventDriver.switchTo().window(newWindow);*/

            String newUrlPage = eventDriver.getCurrentUrl();
            eventDriver.switchTo().newWindow(WindowType.WINDOW);
            eventDriver.manage().window().maximize();
            eventDriver.get(newUrlPage);
        } catch (NoSuchWindowException e) {
            logger.info("окна с заданным именем не найдено!");
            e.printStackTrace();
        } catch (StaleElementReferenceException e) {
            logger.info("Ссылка на элемент больше не действительна!");
            e.printStackTrace();
        }

        // Сравнить ожидаемый заголовок страницы
        try {
            String expectedTitle = "Купить 6.7\" Смартфон Samsung Galaxy Z Flip3 256 ГБ бежевый в интернет магазине DNS. Характеристики, цена Samsung Galaxy Z Flip3 | 4845670";
            String title = eventDriver.getTitle();
            Assertions.assertEquals(title, expectedTitle, "Заголовок страници не соответствует ожидаемому!");
            logger.info("Заголовок соответствует ожидаемому.");
        } catch (StaleElementReferenceException e) {
            logger.info("Ссылка на элемент больше не действует!");
            e.printStackTrace();
        }

        // Ожидать пока страница прогру
        WebElement description = driver.findElement(By.xpath("//div[@class='price-item-description']"));
        wait.until(driver->description);

        // Сделать скриншот
        getScreenShot("screens\\3_ScreenNewPage.png");

        // Пролистать стрраницу вниз до элемента
        js.executeScript("arguments[0].scrollIntoView(true);", description);
        // Открыть пункт характеристики
        By characteristicsXpath = By.xpath("//a[text()='Характеристики']");
        WebElement characteristics = eventDriver.findElement(characteristicsXpath);
        wait.until(ExpectedConditions.elementToBeClickable(characteristics));
        characteristics.click();

        // Сравнить ожидаемый объем ОЗУ
        try {
            String expectedRamSize = "8 Гб";
            By rumSizeTextXpath = By.xpath("//div[text()=' 8 Гб']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(rumSizeTextXpath));
            WebElement rumSizeText = eventDriver.findElement(rumSizeTextXpath);
            Assertions.assertEquals(expectedRamSize, rumSizeText.getText(), "Значение ОЗУ не ноответствует выбраному!");
            logger.info("Значение ОЗУ соответствует ождаемому.");
        } catch (StaleElementReferenceException e) {
            logger.info("Ссылка на элемент больше не действует!");
            e.printStackTrace();
        }

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
            logger.info("Скриншот сохранен в " + path);
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
