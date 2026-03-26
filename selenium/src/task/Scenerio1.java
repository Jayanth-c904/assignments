package task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.*;

public class Scenerio1 {

    public static void main(String[] args) throws IOException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.get("https://www.flipkart.com");

        // Close login popup
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='✕']"))).click();
        } catch (Exception e) {}

        // Search
        WebElement searchBox = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.name("q")));
        searchBox.sendKeys("Bluetooth Speakers", Keys.ENTER);

        // Apply filters
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[text()='Brand']"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[text()='boAt']"))).click();
        } catch (Exception e) {}

        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'4★ & above')]"))).click();
        } catch (Exception e) {}

        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Price -- Low to High')]"))).click();
        } catch (Exception e) {}

        // Open first product
        WebElement product = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("(//a[contains(@href,'/p/')])[1]")));

        String parent = driver.getWindowHandle();

        ((JavascriptExecutor) driver).executeScript(
            "window.open(arguments[0])",
            product.getAttribute("href"));

        for (String win : driver.getWindowHandles()) {
            if (!win.equals(parent)) {
                driver.switchTo().window(win);
            }
        }

        // Get offers
        List<WebElement> offers = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//span[text()='Available offers']/ancestor::div/following-sibling::div//li")));

        System.out.println("Number of offers: " + offers.size());

        // Add to cart
        wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//button[@type='button' and contains(.,'Add to Cart')]"))).click();

        System.out.println("Added to cart");

        // Go to cart
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@href,'viewcart')]"))).click();


        // Screenshot of Place Order
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        File dest = new File("./screenshot/cart_result.png");   // ✅ simple path
        FileHandler.copy(src, dest);

        System.out.println("Screenshot saved successfully");

        driver.quit();
    }
}