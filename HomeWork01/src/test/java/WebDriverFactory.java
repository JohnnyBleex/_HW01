import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;

public class WebDriverFactory {
    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);

    public static WebDriver getDriver(String browserName, String option) {
        PageLoadStrategy strategy = null;

        switch (option){
            case "normal":
                strategy = PageLoadStrategy.NORMAL;
                break;
            case "eager":
                strategy = PageLoadStrategy.EAGER;
                break;
            case "none":
                strategy = PageLoadStrategy.NONE;
                break;
            default:
                strategy = PageLoadStrategy.NORMAL;
                logger.info("Стратегии загрузки страницы с таким названием нет. Будет включена стратегия по умолчанию - normal");
                break;
        }

        switch (browserName) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                WebDriverManager.chromedriver().setup();
                chromeOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
                chromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
                chromeOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, false);
                chromeOptions.setPageLoadStrategy(strategy);
                chromeOptions.setAcceptInsecureCerts(false);

                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--incognito");

                logger.info("Драйвер для браузера Google Chrome");
                return new ChromeDriver(chromeOptions);

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                WebDriverManager.firefoxdriver().setup();
                firefoxOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
                firefoxOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
                firefoxOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, false);
                firefoxOptions.setPageLoadStrategy(strategy);
                firefoxOptions.setAcceptInsecureCerts(false);

                firefoxOptions.addArguments("-kiosk");
                firefoxOptions.addArguments("-private");

                logger.info("Драйвер для браузера Firefox");
                return new FirefoxDriver(firefoxOptions);

            default:
                throw new RuntimeException("Браузер не выбран или введен не коректно!");
        }
    }
}
