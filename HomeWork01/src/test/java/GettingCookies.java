import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class GettingCookies {
    private static final Logger logger = LogManager.getLogger(GettingCookies.class);

    public static void creationOfCookies(WebDriver driver) {
        logger.info("Куки, которые добавили мы");
        driver.manage().addCookie(new Cookie("Cookie 1", "This Is Cookie 1"));
        Cookie cookie = driver.manage().getCookieNamed("Cookie 1");
        logger.info(String.format("Domain %s", cookie.getDomain()));
        logger.info(String.format("Expiry %s", cookie.getExpiry()));
        logger.info(String.format("Name %s", cookie.getName()));
        logger.info(String.format("Path %s", cookie.getPath()));
        logger.info(String.format("Value %s", cookie.getValue()));
        logger.info("------------------------------------------------------------------");
    }

    public static void getCookieOutput(WebDriver driver) {
        logger.info("Куки, который добавил " + driver.getCurrentUrl());
        Set<Cookie> cookies = driver.manage().getCookies();
        for (Cookie cookie : cookies) {
            logger.info(String.format("Domain %s", cookie.getDomain()));
            logger.info(String.format("Expiry %s", cookie.getExpiry()));
            logger.info(String.format("Name %s", cookie.getName()));
            logger.info(String.format("Path %s", cookie.getPath()));
            logger.info(String.format("Value %s", cookie.getValue()));
            logger.info("--------------------------------------------------------------");
        }
    }
}
