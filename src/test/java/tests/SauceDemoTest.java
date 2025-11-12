package tests;

import com.example.pages.LoginPage;
import com.example.pages.ProductsPage;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SauceDemoTest {

  private WebDriver driver;
  private LoginPage loginPage;

  @BeforeClass
  public void setup() {
    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    driver.get("https://www.saucedemo.com");
    loginPage = new LoginPage(driver);
  }

  @Test()
  public void testLoginAndHeaderInProductsPage() {
    ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");

    Assert.assertEquals(productsPage.getPageTitle(), "Products");
    Assert.assertEquals(productsPage.getHeader().getLogoText(), "Swag Labs");
    productsPage.getHeader().openCart();

    WebElement radioElement = driver.findElement(By.id("impressiveRadio"));
    boolean selectState = radioElement.isSelected();
  }

  //  @AfterClass
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }
}
