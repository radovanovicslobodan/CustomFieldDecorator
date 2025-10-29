package tests;

import com.example.enums.Ingredient;
import com.example.pages.PrimeVuePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

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

		primeVuePage.selectIngredient(Ingredient.MUSHROOM);

		Assert.assertEquals(primeVuePage.getSelectedIngredient(), Ingredient.MUSHROOM);
	}

	//  @AfterClass
	public void tearDown() {

		if (driver != null) {
			driver.quit();
		}
	}
}
