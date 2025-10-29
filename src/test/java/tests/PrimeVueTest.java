package tests;

import com.example.enums.City;
import com.example.enums.Ingredient;
import com.example.pages.PrimeVueDropdownPage;
import com.example.pages.PrimeVueSelectPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class PrimeVueTest {

	private WebDriver driver;
	private PrimeVueSelectPage primeVueSelectPage;
	private PrimeVueDropdownPage primeVueDropdownPage;

	@BeforeClass
	public void setup() {

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	@Test
	public void shouldSelectOnion() {

		driver.get("https://primevue.org/radiobutton/");
		primeVueSelectPage = new PrimeVueSelectPage(driver);

		primeVueSelectPage.selectIngredient(Ingredient.MUSHROOM);

		Assert.assertEquals(primeVueSelectPage.getSelectedIngredient(), Ingredient.MUSHROOM);
	}

	@Test
	public void shouldSelectCity() {

		driver.get("https://v3.primevue.org/dropdown/");
		primeVueDropdownPage = new PrimeVueDropdownPage(driver);

		primeVueDropdownPage.selectCity(City.PARIS);

		//		Assert.assertEquals(primeVueSelectPage.getSelectedIngredient(), Ingredient.MUSHROOM);
	}

	//  @AfterClass
	public void tearDown() {

		if (driver != null) {
			driver.quit();
		}
	}
}
