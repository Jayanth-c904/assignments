package task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.*;

public class Sceneraio2 {

    public static void main(String[] args) throws IOException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Open Flipkart
        driver.get("https://www.flipkart.com");

        // Close login popup
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='✕']"))).click();
        } catch (Exception e) {}

        // Search
        driver.findElement(By.name("q")).sendKeys("Bluetooth Speakers", Keys.ENTER);

        // Apply filters
	        driver.findElement(By.xpath("//div[.='Brand']")).click();
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='boAt']"))).click();
        } catch (Exception e) {}

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'4★ & above')]"))).click();
        } catch (Exception e) {}
        

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Low to High')]"))).click();
        } catch (Exception e) {}

        // Open first product
        WebElement product = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("(//a[contains(@href,'/p/')])[1]")));

        String parent = driver.getWindowHandle();

        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0])",
                product.getAttribute("href"));

        for (String win : driver.getWindowHandles()) {
            if (!win.equals(parent)) {
                driver.switchTo().window(win);
            }
        }

        // Check availability
        boolean available = false;

        try {
            WebElement addToCart = driver.findElement(By.xpath("//button[contains(text(),'Add to cart')]"));

            if (addToCart.isDisplayed() && addToCart.isEnabled()) {
                available = true;
            }

        } catch (Exception e) {
            available = false;
        }

        // If NOT available
        if (!available) {
            System.out.println("Product unavailable — could not be added to cart.");

            // Screenshot (safe path)
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            File dest = new File("./screenshot/result.png");   // ✅ simple path
            FileHandler.copy(src, dest);

            System.out.println("Screenshot saved successfully");
        }

        driver.quit();
    }

}