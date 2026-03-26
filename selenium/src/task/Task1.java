package task;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Task1 {

	public static void main(String[] args) {

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
        List<WebElement> offers = driver.findElements(
            By.xpath("//span[text()='Available offers']/ancestor::div/following-sibling::div//li"));

        System.out.println("Number of offers: " + offers.size());	     
        driver.quit();
        }
	}

