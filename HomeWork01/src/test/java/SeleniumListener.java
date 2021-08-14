import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class SeleniumListener implements WebDriverListener {
    private final Logger logger = LogManager.getLogger(SeleniumListener.class);

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        logger.info("Перед поиском элемента " + locator);
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        logger.info("После поиска элемента " + locator + " Элемент <" + result.getTagName() + ">");
    }

    @Override
    public void beforeClick(WebElement element) {
        logger.info("Перед нажатием на элемент <" + element.getTagName() + ">");
    }

    @Override
    public void afterClick(WebElement element) {
        logger.info("После нажатия по элементу <" + element.getTagName() + ">");
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        logger.info("До получения URL " + url);
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        logger.info("После получения URL " + url);
    }
}
