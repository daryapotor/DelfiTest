import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DelfiTest {

    private static ChromeDriver driver;
    private static void setup() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://rus.delfi.lv/");
    }

    public static void acceptCookiePolicy() {
        driver.findElement(By.cssSelector("a[class='close cookie']")).click();
    }

    public static void clickOnFoodSection() {
        driver.findElement(By.cssSelector("a[href*='eda']")).click();
    }

    public static String clickOnRecipeAndGetName() {
        WebElement content = driver.findElement(By.cssSelector("div[class='col-content pull-left']"));
        WebElement recipe = content.findElement(By.cssSelector("a[href*='recepty/']"));
        recipe.click();
        String recipeName = driver.findElement(By.cssSelector("h1[class='article-title'")).getText();
        return recipeName;
    }

    public static void clickOnEachIngredientAndCheck(String recipeName) {
        List<WebElement> items = driver.findElements(By.cssSelector("a[class='ing-title']"));
        List<String> links = new ArrayList<>();
        for (WebElement item: items) {
            links.add(item.getAttribute("href"));
        }

        for (int i = 0; i < items.size(); i++) {
            String link = links.get(i);
            if(link.contains("ingredienty")) {
                driver.navigate().to(links.get(i));
                WebElement ingredientContent = driver.findElement(By.cssSelector("section[class='row article-collection']"));
                WebElement elem = ingredientContent.findElement(By.linkText(recipeName));
                assert elem != null : "Element with recipe title should be on the page";
                driver.navigate().back();
            }
        }
    }

    public static void main(String[] args) {
        setup();

        acceptCookiePolicy();

        clickOnFoodSection();

        acceptCookiePolicy();

        String recipeName = clickOnRecipeAndGetName();

        clickOnEachIngredientAndCheck(recipeName);

        driver.close();
    }

}