package tests;

import com.example.components.DataTable;
import com.example.components.VirtualDataTable;
import com.example.enums.City;
import com.example.enums.Ingredient;
import com.example.pages.PrimeVueDataTablePage;
import com.example.pages.PrimeVueDropdownPage;
import com.example.pages.PrimeVueSelectPage;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PrimeVueTest {

  private WebDriver driver;
  private PrimeVueSelectPage primeVueSelectPage;
  private PrimeVueDropdownPage primeVueDropdownPage;
  private PrimeVueDataTablePage primeVueDataTablePage;

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

    Assert.assertEquals(primeVueDropdownPage.getSelectedCity(), City.PARIS.getDisplayName());
  }

  @Test
  public void tableTest() {

    driver.get("https://v3.primevue.org/datatable/#multiple_sort");
    primeVueDataTablePage = new PrimeVueDataTablePage(driver);

    DataTable table = primeVueDataTablePage.getTable();

    int nameColIndex = table.getColumnIndex("Name"); // 1
    boolean isNameSorted = table.isColumnSorted("Name"); // true
    String sortOrder = table.getColumnSortOrder("Name"); // "descending"

    WebElement row = table.getRowByColumnValue("Code", "h456wer53");
    String category = table.getCellText(0, "Category"); // "Accessories"
  }

  @Test
  public void virtualTableTest() {

    driver.get("https://v3.primevue.org/datatable/#virtualscroll");
    primeVueDataTablePage = new PrimeVueDataTablePage(driver);

    VirtualDataTable table = primeVueDataTablePage.getVirtualTable();

    int nameColIndex = table.getColumnIndex("Vin"); // 1
    boolean isNameSorted = table.isColumnSorted("Brand"); // true
    String sortOrder = table.getColumnSortOrder("Brand"); // "descending"

//    WebElement row = table.getRowByColumnValueInVirtualScroll("Id", "90");
    table.scrollToRow(70);
    String category = table.getCellText(2, "Color"); // "Accessories"
  }

  //  @AfterClass
  public void tearDown() {

    if (driver != null) {
      driver.quit();
    }
  }
}
