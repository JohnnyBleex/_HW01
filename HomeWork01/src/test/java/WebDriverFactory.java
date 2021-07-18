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
        switch (browserName) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                WebDriverManager.chromedriver().setup();
                chromeOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
                chromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
                chromeOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, false);
                chromeOptions.setAcceptInsecureCerts(false);
                switch (option) {
                    case "normal":
                        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        break;
                    case "eager":
                        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                        break;
                    case "none":
                        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                        break;
                    default:
                        logger.info("Стратегия не назначена! (по умолчанию)");
                }

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
                firefoxOptions.setAcceptInsecureCerts(false);
                switch (option) {
                    case "normal":
                        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        break;
                    case "eager":
                        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                        break;
                    case "none":
                        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                        break;
                    default:
                        logger.info("Стратегия не назначена! (NORMAL по умолчанию)");
                }
                firefoxOptions.addArguments("-private");

                logger.info("Драйвер для браузера Firefox");
                return new FirefoxDriver(firefoxOptions);

            default:
                throw new RuntimeException("Браузер не выбран или введен не коректно!");
        }
    }
}
