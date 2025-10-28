package tests;

import com.example.enums.Ingredient;
import com.example.pages.PrimeVuePage;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PrimeVueTest {

  private WebDriver driver;
  private PrimeVuePage primeVuePage;

  @BeforeClass
  public void setup() {
    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    driver.get("https://primevue.org/radiobutton/");
    primeVuePage = new PrimeVuePage(driver);
  }

  @Test
  public void shouldSelectOnion() {
    primeVuePage.selectIngredient(Ingredient.ONION);

    Assert.assertEquals(primeVuePage.getSelectedIngredient(), Ingredient.ONION);
  }

  //  @AfterClass
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }
}
