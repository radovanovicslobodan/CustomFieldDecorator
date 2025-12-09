package tests;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TableTest {

  private WebDriver driver;

  @BeforeClass
  public void setup() {

    driver = new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
  }

  @Test
  public void testTableFiltering() {
    driver.get("https://primevue.org/datatable/#advanced_filter");

    WebElement section = driver.findElement(By.xpath("//section[12]"));
    WebElement tableHeader = section.findElement(By.cssSelector(".p-datatable-thead"));
    WebElement firstColumnHeader = tableHeader.findElements(By.cssSelector("th")).get(0);

    WebElement filterButton = firstColumnHeader.findElement(By.cssSelector(".p-datatable-filter"));
    filterButton.click();

    WebElement filterPopup = driver.findElement(By.cssSelector(".p-datatable-filter-overlay"));
    WebElement filterTrigger = filterPopup.findElement(By.cssSelector(".p-select-dropdown"));
    filterTrigger.click();

    WebElement selectList = driver.findElement(By.cssSelector(".p-select-list-container ul"));
    WebElement matchAll = selectList.findElement(By.cssSelector("[aria-posinset='1']"));
    WebElement matchAny = selectList.findElement(By.cssSelector("[aria-posinset='2']"));

    matchAll.click();
  }
}
